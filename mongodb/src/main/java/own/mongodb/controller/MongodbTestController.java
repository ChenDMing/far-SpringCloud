package own.mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import own.common.enumerate.Msg;
import own.common.utils.ResponseEntity;
import own.common.utils.ResultUtils;
import own.mongodb.service.MongodbTestService;

@RestController
@RequestMapping("/db/test")
public class MongodbTestController {

    @Autowired
    private MongodbTestService mongodbTestService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ResponseEntity testInsert() {
        mongodbTestService.testInsert();
        return ResultUtils.resultMsg(Msg.Success);
    }

}
