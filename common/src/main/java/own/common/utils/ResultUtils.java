package own.common.utils;

import com.alibaba.fastjson.JSON;
import own.common.enumerate.Msg;
import own.common.utils.auth.AuthThreadLocal;

public class ResultUtils {

    public static ResponseEntity resultMsg(Msg msg) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setCode(msg.getCode());
        responseEntity.setMsg(msg.getMsg());
        return responseEntity;
    }

    public static ResponseEntity resultMsg(Msg msg, Object result) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setCode(msg.getCode());
        responseEntity.setMsg(msg.getMsg());
        responseEntity.setResult(result);
//        AuthThreadLocal.setResult(JSON.toJSONString(result));
        return responseEntity;
    }

}
