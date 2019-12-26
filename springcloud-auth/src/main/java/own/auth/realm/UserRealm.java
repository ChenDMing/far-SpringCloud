package own.auth.realm;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import own.auth.entity.po.AdminMenu;
import own.auth.entity.po.AdminUser;
import own.auth.jwt.JWTToken;
import own.auth.service.AdminUserService;
import own.common.enumerate.Msg;
import own.common.utils.JWTUtils;

import java.util.*;

/**
 * Shiro 认证授权的实现方法
 * 表结构设计:
 * 1. dsc_admin_menu, dsc_admin_role, dsc_admin_role_menu
 *    dsc_admin_user, dsc_admin_user_role
 * 2. 菜单权限表, 角色表, 权限-角色中间表  这三张表决定了角色对应的权限
 *    用户表, 用户-角色中间表  决定了用户对应的那个角色
 * 3. 当用户登录时根据用户查询出角色, 再根据角色查询出权限
 */
public class UserRealm extends AuthorizingRealm {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserRealm.class);
    private final static long expireTime = 1800;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 需要允许使用JWTToken, 返回 true 说明允许
     * Shiro 中的 Realm 接口里有 supports 方法, 返回 true 就是支持使用此 token
     *
     * 可能有项目需要多个自定义的 JWTToken 实现多种 Shiro 规定的 Token 类型, 此时可以多次实现 supports 方法
     * 这个方法可用于区分多个 token, 运行时当前 supports 返回 false 会继续执行其他的 supports 方法, 如果全部不支持则报异常,
     * 支持的则认证和授权会执行此自定义的 realm 中的认证和授权方法
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        // 形参采用的是父类接口接受子类实现, 所以接受到的是自定义的 JWTToken, return 的是 true
        boolean isJWTToken = token instanceof JWTToken;
        LOGGER.info("isJWTToken: {}", isJWTToken);
        return isJWTToken;
    }

    /**
     * 授权
     *  - @RequiresPermissions("normalGoods") 方法上使用此注解则会根据授权返回的 AuthorizationInfo 中是否包含 "normalGoods" 权限
     * @param principals principals
     * @return 权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LOGGER.info("------权限认证------");
        Subject subject = SecurityUtils.getSubject();
        /*
         * subject.getPrincipals().getPrimaryPrincipal() 其中 getPrimaryPrincipal() 获取的第一个
         * subject.getPrincipal() 与上面获取的结果一样
         */
        // 获取当前登录的用户
        AdminUser adminUser = (AdminUser) subject.getPrincipals().getPrimaryPrincipal();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> set = new HashSet<>();
        for (AdminMenu adminMenu : adminUser.getPre()) {
            // 登录时调用的认证方法里已经将 adminUser 数据拼接完整, 这里直接取出用户对应所有角色的权限
            set.add(adminMenu.getCode());
        }
        info.setStringPermissions(set); //设置用户权限信息
        return info;
    }

    /**
     * 认证
     * @param authToken token - token 就是 subject.login(jwtToken) 中的参数 jwtToken, jwtToken 继承了 Shiro 中的 Token 加上上面的 supports 方法于是可以正常使用并作为参数传递到这里
     * @return authenticationInfo
     * @throws AuthenticationException eee
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        // 此token来源于过滤器的executeLogin()方法中
        boolean isLogin = false;
        JWTToken jwtToken = (JWTToken)authToken;
        String token = (String)jwtToken.getPrincipal();// 没什么意义, getPrincipal 就是获取token

        String username = "";
        String password = "";

        if (StringUtils.isEmpty(token)) {
            // token 为空说明是登录 - 登录接口只将用户名和h5加密后的密码放入了JWTToken中, 其他请求走过滤器的也只将请求头中携带的token放入了JWTToken中
            isLogin = true;
            username = jwtToken.getUsername();
            password = jwtToken.getPassword();
            if (StringUtils.isEmpty(password) || StringUtils.isEmpty(username)) {
                throw new AccountException(Msg.ParamError.getMsg());
            }
        } else {
            username = JWTUtils.getUsername(token);
        }

        if (!isLogin) {
            // 除登录外的请求 - 将从缓存中取出的token做校验并做刷新
            String value = redisTemplate.opsForValue().get("Shiro:token:key:" + token);
            if (!StringUtils.isEmpty(value)) {
                AdminUser adminUser = JSONObject.parseObject(value, AdminUser.class);
                // 这里不用校验账号, 因为value是从redis中取出的, 登录时已经校验过是正确的
                token = refreshToken(token, adminUser);
                return new SimpleAuthenticationInfo(adminUser, token, this.getName());
            }
        }

        // 获取用户
        if (!isLogin && StringUtils.isEmpty(username)) {
            throw new UnsupportedTokenException(Msg.TokenInvalid.getMsg());
        }
        AdminUser adminUser = adminUserService.getAdminUserByUsername(username);

        if (isLogin) {
            if (adminUser == null) {
                throw new AuthenticationException(Msg.AccountNotExists.getMsg());
            }
            // 校验密码
            token = JWTUtils.sing(username, password);
            if (!JWTUtils.verify(token, username, adminUser.getPassword())) {
                throw new AuthenticationException(Msg.PwdError.getMsg());
            }
        } else {
            if (!JWTUtils.verify(token, username, adminUser.getPassword())) {
                throw new UnsupportedTokenException(Msg.TokenCheckError.getMsg());
            }
            token = refreshToken(token, adminUser);
        }
        adminUser.setAccessToken(token);
        getPermissions(adminUser);

        redisTemplate.opsForValue().set("Shiro:token:key:" + token, adminUser.parseLoginCache());
        return new SimpleAuthenticationInfo(adminUser, token, this.getName());
    }

    private String refreshToken(String token, AdminUser adminUser) {
        redisTemplate.delete("Shiro:token:key:" + token);
        token = JWTUtils.sing(adminUser.getUsername(), adminUser.getPassword());
        adminUser.setAccessToken(token);
        redisTemplate.opsForValue().set("Shiro:token:key:" + token, adminUser.parseLoginCache());
        return token;
    }

    private void getPermissions(AdminUser adminUser) {
        List<AdminMenu> permsList = new ArrayList<>();
        //系统管理员，拥有最高权限
        if(adminUser.getUsername().equals("超管")){
//            permsList = adminMenuService.queryAllMenu(); 查询所有权限
            permsList.add(null);
        }else{
//            permsList = adminMenuService.queryAllPerms(adminUser.getUsername()); 查询出角色对应的权限
        }
        List<AdminMenu> menuList = new ArrayList<>();
        Map<Integer,Set<String>> buttonList = new HashMap<>();
        for(AdminMenu adminMenu : permsList){
            if(adminMenu != null){
                if(adminMenu.getType() == 2){
                    //如果是按钮
                    if(buttonList.containsKey(adminMenu.getParentId())){
                        //如果已经存在菜单 则 直接add
                        buttonList.get(adminMenu.getParentId()).add(adminMenu.getName());
                    }else{
                        Set<String> buttonSet = new HashSet<>();
                        buttonSet.add(adminMenu.getName());
                        buttonList.put(adminMenu.getParentId(),buttonSet);
                    }
                }
                //如果是菜单
                menuList.add(adminMenu);
            }
        }
        if(menuList.size()>0){
            adminUser.setPre(menuList);
        }
        if(buttonList.size()>0){
            adminUser.setAnniu(buttonList);
        }
    }
}
