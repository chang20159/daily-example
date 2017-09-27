package utils;

import link.java.net.config.WikiConfig;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by chang on 16/3/25.
 */
public class BasicAuthUtil {
    /**
     *  Build a string of the form username:password
     *  Base64 encode the string
     *  Supply an "Authorization" header with content "Basic " followed by the encoded string.
     */
    public static String getBasicAuth(){

        String str = new StringBuilder(WikiConfig.USERNAME).append(":").append(WikiConfig.PASSWORD).toString();
        return "Basic " + Base64.encodeBase64String(str.getBytes());
    }
}
