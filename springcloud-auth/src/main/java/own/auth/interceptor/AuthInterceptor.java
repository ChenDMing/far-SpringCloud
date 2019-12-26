package own.auth.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import own.common.utils.RequestUtils;
import own.common.utils.auth.AuthThreadLocal;
import own.common.utils.auth.AuthTimePojo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

// ThreadLocal不调用remove后的影响: 当web服务请求达到饱和, 此时线程池中的线程达到最大数量, 并且不会销毁, 每个线程之间不会有影响, 但是
// 下一个请求使用了上一个请求的线程会有ThreadLocal中的数据残留, 如果第一步是set方法不会有影响, 是get就会出现数据问题
/**
 * 权限服务拦截器
 */
public class AuthInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    // handler 参数就是此次请求的url具体映射到的方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> paramMap = RequestUtils.getRequestUtils(request);
        logger.info("请求url:" + request.getRequestURL() + ", 参数:" + paramMap);
        AuthTimePojo authTimePojo = new AuthTimePojo();
        authTimePojo.setStart(System.currentTimeMillis());
        AuthThreadLocal.setThreadLocal(authTimePojo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis();
        AuthTimePojo threadLocal = AuthThreadLocal.getThreadLocal();
        AuthThreadLocal.removeThreadLocal();
        logger.info("请求url:" + request.getRequestURL() + ", 执行时间:" + (endTime - threadLocal.getStart()) +
                "ms, 执行结果:" + threadLocal.getResult());
    }
}
