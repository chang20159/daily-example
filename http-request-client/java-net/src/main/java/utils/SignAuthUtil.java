package utils;

import link.java.net.config.ChatConfig;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chang on 16/3/26.
 */
public class SignAuthUtil {

    public static Map<String,String> getHeader(String url,String requestMethod) throws Exception{
        Map<String,String> headerMap = new HashMap<String, String>();

        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        String dateGMTStr = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).format(new Date());

        String strToSign = String.format("%s %s\n%s", requestMethod, new URL(url).getPath(), dateGMTStr);
        String signature = hmacSHA1(ChatConfig.APP_SECRET, strToSign);

        headerMap.put("Date",dateGMTStr);
        headerMap.put("Authorization",String.format("%s %s:%s",ChatConfig.AUTH_METHOD,ChatConfig.APP_KEY,signature));

        return headerMap;

    }

    /**
     * 生成签名数据
     * @param key    加密使用的key
     * @param data   待加密的数据
     * @return
     */
    public static String hmacSHA1(String key,String data) throws Exception{
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ChatConfig.ENCRYPT_METHOD);

        Mac mac = Mac.getInstance(ChatConfig.ENCRYPT_METHOD);
        mac.init(secretKey);

        byte[] rawHmac = mac.doFinal(data.getBytes("utf-8"));

        //必须使用 commons-codec 1.5及以上版本，否则base64加密后会出现换行问题
        return Base64.encodeBase64String(rawHmac);

    }
}
