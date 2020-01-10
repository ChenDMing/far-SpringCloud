package own.mongodb.entity.po;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * mongodb测试表格
 */
@Data
@Document(collection = "d_test_user")
public class TestUser {

    @Indexed
    private Long userId;

    private String userName;

    private String password;

    private String email;

    private Integer mobilePhone;

    @Transient
    private String accessToken;

}
