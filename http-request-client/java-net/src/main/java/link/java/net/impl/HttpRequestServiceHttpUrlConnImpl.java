package link.java.net.impl;

import com.alibaba.fastjson.JSONObject;
import link.java.net.HttpRequestService;
import link.java.net.config.ChatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.BasicAuthUtil;
import utils.SignAuthUtil;
import utils.StreamUtil;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by chang on 16/3/26.
 */
public class HttpRequestServiceHttpUrlConnImpl implements HttpRequestService {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestServiceHttpUrlConnImpl.class);

    /**
     * basic auth
     * @param url
     * @param param
     * @return
     */
    public String get(String url,String param) {
        HttpURLConnection conn = null;
        String result = null;
        try {
            String requestUrl = url + URLEncoder.encode(param, "utf-8");
            URL cUrl = new URL(requestUrl);
            conn = (HttpURLConnection)cUrl.openConnection();

            conn.setRequestMethod("GET");
            conn.setDefaultUseCaches(true);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestProperty("Authorization", BasicAuthUtil.getBasicAuth());

            if(200 != conn.getResponseCode()){
                throw new RuntimeException("response code is not 200  > " + StreamUtil.transformStreamToStr(conn.getInputStream()));
            }

            InputStream inputStream = conn.getInputStream();
            result = StreamUtil.transformStreamToStr(inputStream);
            inputStream.close();

            logger.info("url :" + url + ">  response success :" + result);

        } catch (Exception e) {
            logger.error("url :" + url + ">  request error: " ,e);
        } finally {
            if(conn != null){
                conn.disconnect();
            }
        }
        return result;
    }

    /**
     * 签名认证
     * @param url
     * @return
     */
    public String post(String url) {

        String result = null;
        HttpURLConnection conn = null;
        try {
            URL cUrl = new URL(url);
            conn = (HttpURLConnection)cUrl.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);    //默认false  post方式必须配置
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("Content-type", "application/x-java-serialized-object");  //必须配置，否则会抛IOException
            Map<String,String> header = SignAuthUtil.getHeader(url, ChatConfig.REQUEST_METHOD);
            for(Map.Entry<String,String> entry:header.entrySet()){
                conn.setRequestProperty(entry.getKey(),entry.getValue() );
            }

            OutputStream outputStream = conn.getOutputStream();

            outputStream.write(getJsonData().getBytes());

            outputStream.flush();
            outputStream.close();

            if(200 != conn.getResponseCode()){
                throw new RuntimeException("response code is not 200  >   " + StreamUtil.transformStreamToStr(conn.getInputStream()) );
            }

            InputStream inputStream = conn.getInputStream();
            result = StreamUtil.transformStreamToStr(inputStream);
            inputStream.close();

            logger.info("url :" + url + ">  response success :" + result);

        } catch (Exception e) {
            logger.error("url :" + url + ">  request error: " ,e);
        } finally {
            if(conn != null){
                conn.disconnect();
            }
        }

        return result;
    }

    private String getJsonData() {
        JSONObject jsonData = new JSONObject();
        jsonData.put("t", ChatConfig.TENANT);
        jsonData.put("tu", ChatConfig.PASSPORT);
        return jsonData.toString();
    }
}
