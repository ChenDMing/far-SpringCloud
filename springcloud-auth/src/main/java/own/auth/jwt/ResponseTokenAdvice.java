package own.auth.jwt;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import own.auth.entity.po.AdminUser;
import own.common.utils.ResponseEntity;

@ControllerAdvice
public class ResponseTokenAdvice implements ResponseBodyAdvice<ResponseEntity> {

    /**
     * 返回 true 执行 beforeBodyWrite() 方法
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Subject subject = SecurityUtils.getSubject();
        AdminUser adminUser = (AdminUser) subject.getPrincipals();
        return adminUser != null;
    }

    @Override
    public ResponseEntity beforeBodyWrite(ResponseEntity responseEntity, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // response 中追加 access_token
        return null;
    }
}
