package own.timer.server;

import com.baomidou.mybatisplus.core.metadata.IPage;
import own.timer.entity.vo.JobAndTrigger;
import own.timer.entity.vo.UserSummaryVo;

import java.util.List;

public interface JobAndTriggerService {

    IPage<UserSummaryVo> getDataByPaging(Integer page, Integer size, Integer userId);

}
