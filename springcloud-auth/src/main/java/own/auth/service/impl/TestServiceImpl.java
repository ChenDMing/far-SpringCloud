package own.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import own.auth.dao.TestMapper;
import own.auth.entity.po.AdminUser;
import own.auth.service.TestService;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public AdminUser getAdminUserById(String id) {
        Boolean flag = redisTemplate.hasKey("goods_view_num");
        List<Object> goods_view_num = redisTemplate.opsForHash().values("goods_view_num");
        return testMapper.selectById(id);
    }
}
