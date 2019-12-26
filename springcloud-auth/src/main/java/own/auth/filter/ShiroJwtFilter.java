package own.auth.filter;

import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import own.auth.jwt.JWTToken;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Shiro过滤器
 */
public class ShiroJwtFilter extends BasicHttpAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroJwtFilter.class);

    /**
     * 判断用户是否想要登入。访问登陆页面，或游客访问时请求头中Token为空
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        /*
        * 1. 公司逻辑: 登录时直接请求controller中的登录方法, 方法内部手动调用subject.login(token)进行认证登录
        *             登录请求以外的其他请求每个都会携带token, 此方法判断出存在token返回true, 然后再次进行认证
        * 2. controller中的登录接口会响应的数据有: access_token + 角色权限, access_token 用来作为请求头中的信息, 前台所
        *    展示的页面根据角色权限进行展示, 没有权限的页面不进行展示
        */
        LOGGER.info("isLoginAttempt...");
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("access_token");
        return !StringUtils.isEmpty(authorization);
    }

    /**
     * 登录方法
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws UnsupportedTokenException, AuthorizationException {
        /*
        * subject.login(token): 方法内部会经过一层层调用最后到项目中实现了Realm中的 (认证)doGetAuthenticationInfo(){} 方法
        */
        LOGGER.info("executeLogin...");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("access_token");
        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 请求进入过滤器后首先执行此方法, 返回true则继续, 返回false则会调用onAccessDenied()
     * 这个方法在返回 false 时还调用了 isPermissive()方法
     * @param request request
     * @param response response
     * @param mappedValue ...
     * @return boolean
     * @throws UnsupportedTokenException
     * @throws AuthorizationException
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnsupportedTokenException, AuthorizationException {
        LOGGER.info("isAccessAllowed...");
        /*
        * isLoginAttempt() 方法是判断请求是否通过, 父类中有自己的一套校验规则, 所以这里需要重写此方法用自己的校验规则校验请求是否允许执行
        */
        if (isLoginAttempt(request, response)) {
            executeLogin(request, response);
        }
        return true;
    }

    /**
     * 如果上面的 isAccessAllowed 返回 false 则会执行此方法
     * 此方法里返回 true 请求会继续执行
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        LOGGER.info("onAccessDenied");
        return false;
    }

    /*
     * 此方法不是父类 BasicHttpAuthenticationFilter 中的方法, 是自定义过滤器继承另一种父类过滤器可以重写的方法
     *
     * demo 逻辑如下
     * 如果Shiro Login认证成功，会进入该方法，等同于用户名密码登录成功，我们这里还判断了是否要刷新Token
     */
//    @Override
//    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
//        return true;
//    }

//  1.

    /*
     * 此方法不是父类 BasicHttpAuthenticationFilter 中的方法
     *
     * demo 逻辑如下
     * 如果调用shiro的login认证失败，会回调这个方法，这里我们什么都不做，因为逻辑放到了onAccessDenied（）中。
     */
//    @Override
//    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
//        return false;
//    }

}
