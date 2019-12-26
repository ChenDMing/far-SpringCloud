package own.common.enumerate;

public enum Msg {

    Success(5200, "Success!"),
    ServerError(5500, "Error!"),
    ParamError(3000, "Parameter is Error!"),

    AccountNotExists(3100, "Account does not exist!"), // 账号不存在
    PwdError(6103, "PassWord Error!"), //密码错误
    TokenInvalid(6112, "Token is invalid!"), //Token无效!
    TokenCheckError(6113, "Token verification failed!"), //Token校验失败!


    ;

    private final int code;

    private final String msg;

    Msg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static Msg getMsgByMsg(String msgStr) {
        for (Msg msg : Msg.values()) {
            if (msg.msg.equals(msgStr)) {
                return msg;
            }
        }
        return null;
    }

}
