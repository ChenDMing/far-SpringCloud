package own.common.utils.auth;

import lombok.Data;

@Data
public class AuthTimePojo {

    private String userId;

    private String result;

    private long start;

    @Override
    public String toString() {
        return "TimePojo [start=" + start + ", result=" + result + "]";
    }

}
