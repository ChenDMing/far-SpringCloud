package own.auth.interceptor;

import org.apache.shiro.authc.AccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import own.common.enumerate.Msg;
import own.common.utils.RequestUtils;
import own.common.utils.ResponseEntity;
import own.common.utils.ResultUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

// @RestControllerAdvice和@ControllerAdvice的区别就是方法上不用加上@ResponseBody
/**
 * auth服务全局异常拦截器
 */
@RestControllerAdvice("own.auth")
public class AuthExceptionHandler {

    // final 和 static 两个关键字没有前后顺序
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity overallSituationException(HttpServletRequest request, Exception exception) {
        printLog(request, exception);
        return ResultUtils.resultMsg(Msg.ServerError);
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity accountException(HttpServletRequest request, Exception exception) {
        printLog(request, exception);
        Msg msgByMsg = Msg.getMsgByMsg(exception.getMessage());
        if (msgByMsg == null) {
            msgByMsg = Msg.ServerError;
        }
        return ResultUtils.resultMsg(msgByMsg);
    }

    private void printLog(HttpServletRequest request, Exception exception) {
        Map<String, String> paramMap = RequestUtils.getRequestUtils(request);
        LOGGER.error("Auth Url:{} params:{} Exception Msg:{}", request.getRequestURL(), paramMap, exception.getMessage());
        exception.printStackTrace();
    }

}
