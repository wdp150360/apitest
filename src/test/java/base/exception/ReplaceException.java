package base.exception;


public class ReplaceException extends RuntimeException {

    public static final String REPLACE_NO_PARAMS_FOUND = "找不到参数";
    public static final String REPLACE_PARAMS_TYPE_ERROR = "参数类型不正确";
    private static final long serialVersionUID = -6618342393567673860L;

    public ReplaceException(String message) {
        super(message);
    }

    public ReplaceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
