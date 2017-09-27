package link.java.net.config;

/**
 *
 * 使用签名请求时，HTTP Header里必须包含Date和Authorization字段，如：
 * Date: Fri, 17 Feb 2012 23:34:53 GMT
 * Authorization：xxx
 *
 * 约定好加密用的secret   双方用同样的方式，请求方加密，服务方验证
 */
public class ChatConfig {

    public static String AUTH_METHOD = "MWS";

    public static String APP_KEY = "xxx";

    public static String APP_SECRET = "xxx";

    public static String TENANT = "xxx";

    public static String UID_REQUEST_URL = "xxx";

    public static String ENCRYPT_METHOD = "HmacSHA1";

    public static String PASSPORT = "xxx";

    public static String REQUEST_METHOD = "POST";
}
