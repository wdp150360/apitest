package base.exception;

public class CSVException extends RuntimeException {
    public static final String CSV_FILE_NAME_IS_BLANK = "CSV文件名称不能为空！";
    public static final String CSV_FILE_NOT_EXISTS = "CSV文件不存在！";
    public static final String CSV_FILE_LOAD_FAIL = "CSV文件加载失败！";
    public static final String CSV_FILE_READ_FAIL = "CSV文件读取失败！";
    public static final String CSV_FILE_READER_CLOSE_FAIL = "CSV文件读取流关闭失败！";
    public static final String CSV_FILE_PARSE_FAIL = "CSV文件解析失败";
    public static final String CSV_FILE_PARSE_FAIL1 = "CSV文件格式解析失败";
    private static final long serialVersionUID = -6765191474796807624L;

    public CSVException(String message) {
        super(message);
    }

    public CSVException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
