package own.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import own.auth.service.TestService;
import own.common.enumerate.Msg;
import own.common.utils.ResponseEntity;
import own.common.utils.ResultUtils;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping(value = "/get_admin_user", method = RequestMethod.GET)
    public ResponseEntity test(String id) {
        return ResultUtils.resultMsg(Msg.Success, testService.getAdminUserById(id));
    }

}
