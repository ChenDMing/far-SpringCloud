package own.auth.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import own.auth.entity.po.AdminUser;
import own.common.enumerate.Msg;
import own.common.utils.ResponseEntity;
import own.common.utils.ResultUtils;

/**
 * Shiro用于做登录登出的controller
 */
@RestController
@RequestMapping(value = "/shiro")
public class ShiroLoginController {

    /**
     * 登录
     * @param userName username
     * @param password password
     * @return response
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(String userName, String password) {
        Subject subject = SecurityUtils.getSubject();
        // subject.login() 这一整个步骤内部在实现的realm认证后, 内部会调用方法刷新 session 中的 AdminUser 对象, session是Shiro的SessionManage
        subject.login(null);
        AdminUser adminUser = (AdminUser)subject.getPrincipal();
        return null;
    }

    /**
     * 登出
     * @return response
     */
    public ResponseEntity logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultUtils.resultMsg(Msg.Success);
    }

}
