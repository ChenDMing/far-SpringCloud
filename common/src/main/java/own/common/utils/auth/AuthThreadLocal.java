package own.common.utils.auth;

public class AuthThreadLocal {

    private static final ThreadLocal<AuthTimePojo> AUTH_THREAD_LOCAL = new ThreadLocal<>();

    public static AuthTimePojo getThreadLocal() {
        return AUTH_THREAD_LOCAL.get();
    }

    public static void setThreadLocal(AuthTimePojo authTimePojo) {
        AUTH_THREAD_LOCAL.set(authTimePojo);
    }

    public static void removeThreadLocal() {
        AUTH_THREAD_LOCAL.remove(); // 不删除下个请求过来get到的是上一个请求的残留数据, 如果先set不用删除也可以
    }

    public static void setResult(String resultStr) {
        AuthTimePojo threadLocal = getThreadLocal();
        threadLocal.setResult(resultStr);
    }
}
