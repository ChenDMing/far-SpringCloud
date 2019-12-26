package own.timer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import own.common.enumerate.Msg;
import own.common.utils.ResponseEntity;
import own.common.utils.ResultUtils;
import own.timer.entity.vo.UserSummaryVo;
import own.timer.server.JobAndTriggerService;

@RestController
@RequestMapping(value = "/job")
public class JobController {

    @Autowired
    public JobAndTriggerService jobAndTriggerService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity getDataByPaging() {
        IPage<UserSummaryVo> dataByPaging = jobAndTriggerService.getDataByPaging(2, 2, 21602);
        return ResultUtils.resultMsg(Msg.Success, dataByPaging);
    }

}
