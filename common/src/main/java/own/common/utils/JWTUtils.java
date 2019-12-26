package own.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JWTUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(JWTUtils.class);

    /**
     * 从token中获取用户名, 不需要解密
     * @param token token
     * @return username
     */
    public static String getUsername(String token) {
        String username = null;
        // jwt加密部分只有签名, 用来判断jwt数据是否被修改, 头部和载荷只用了 base64 编码
        try {
            DecodedJWT jwt = JWT.decode(token);
            username = jwt.getClaim("username").asString();
        } catch (Exception e) {
            LOGGER.error("解码获取用户名错误...", e);
            e.printStackTrace();
        }
        return username;
    }

    /**
     * 生成签名
     * @param username username
     * @param password pwd
     * @return token
     */
    public static String sing(String username, String password) {
        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            token = JWT.create().withClaim("username", username).withExpiresAt(new Date()).sign(algorithm);
        } catch (Exception e) {
            LOGGER.error("生成token错误...", e);
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 校验token是否正确
     * @param token token
     * @param username username
     * @param password pwd
     * @return b
     */
    public static boolean verify(String token, String username, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
