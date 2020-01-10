package own.mongodb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import own.mongodb.entity.po.TestUser;
import own.mongodb.service.MongodbTestService;

@Service
public class MongodbTestServiceImpl implements MongodbTestService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 插入
     */
    @Override
    public void testInsert() {
        TestUser user = new TestUser();
        user.setUserId(System.currentTimeMillis());
        user.setUserName("cdm");
        user.setPassword("cdm123");
        user.setMobilePhone(1111111111);
        user.setEmail("11@163.com");
        user.setAccessToken("FJASLDJFALSDJFKLA");
    }
}
