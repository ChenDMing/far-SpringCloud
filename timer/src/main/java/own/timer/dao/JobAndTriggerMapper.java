package own.timer.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import own.timer.entity.vo.JobAndTrigger;
import own.timer.entity.vo.UserSummaryVo;

import java.util.List;

@Repository
public interface JobAndTriggerMapper extends BaseMapper<JobAndTrigger> {

    List<JobAndTrigger> getDataByPaging();

    List<UserSummaryVo> testPaging(IPage<UserSummaryVo> iPage, @Param(Constants.WRAPPER)
            QueryWrapper<UserSummaryVo> queryWrapper, @Param("userId") Integer userId);
}
