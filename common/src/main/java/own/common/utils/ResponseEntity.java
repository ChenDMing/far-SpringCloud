package own.common.utils;

import lombok.Data;

@Data
public class ResponseEntity {

    private Integer code;

    private String msg;

    private Object result;

}
