package own.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import own.common.enumerate.Msg;
import own.common.utils.ResponseEntity;
import own.common.utils.ResultUtils;

@RestController
@RequestMapping("/test_redis")
public class TestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/cache")
    public ResponseEntity testRedis() {
        String key = "lopdeals:user:id";
        Object o = redisTemplate.opsForHash().get(key, "21350");
        return ResultUtils.resultMsg(Msg.Success, o);
    }

}
