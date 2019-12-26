package own.timer.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import own.timer.dao.JobAndTriggerMapper;
import own.timer.entity.vo.UserSummaryVo;
import own.timer.server.JobAndTriggerService;

import java.util.List;

@Service
public class JobAndTriggerServiceImpl implements JobAndTriggerService {

    private final JobAndTriggerMapper jobAndTriggerMapper;

    @Autowired
    JobAndTriggerServiceImpl(JobAndTriggerMapper jobAndTriggerMapper) {
        this.jobAndTriggerMapper = jobAndTriggerMapper;
    }

    /**
     * 测试 mybatis-plus 的多表联查分页
     * @param page 页码
     * @param size 一页的条数
     * @return result
     */
    @Override
    public IPage<UserSummaryVo> getDataByPaging(Integer page, Integer size, Integer userId) {
        QueryWrapper<UserSummaryVo> queryWrapper = new QueryWrapper<>();
        IPage<UserSummaryVo> iPage = new Page<>(page, size);
        List<UserSummaryVo> resultList = jobAndTriggerMapper.testPaging(iPage, queryWrapper, userId);
        iPage.setRecords(resultList);
        return iPage;
    }
}
