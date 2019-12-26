package own.auth.jwt;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {

    private String token;
    private String username;
    private String password;
    private Boolean needRefreshToken;

    public JWTToken(String token) {
        this.token = token;
    }

    public JWTToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 重写的 getPrincipal() 和 getCredentials() 方法需要返回值是 token
     * getPrincipal() 和 subject.getPrincipal() 不是一个概念, 这个重写的 getPrincipal() 仅仅是实现的接口中
     * 的接口方法描述就是返回用户的标识, 也就是 token, 重写的 getCredentials() 方法同理
     */
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getNeedRefreshToken() {
        return needRefreshToken;
    }

    public void setNeedRefreshToken(Boolean needRefreshToken) {
        this.needRefreshToken = needRefreshToken;
    }
}
