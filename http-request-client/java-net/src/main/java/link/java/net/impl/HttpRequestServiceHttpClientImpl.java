package link.java.net.impl;

import com.alibaba.fastjson.JSONObject;
import link.java.net.HttpRequestService;
import link.java.net.config.ChatConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.BasicAuthUtil;
import utils.SignAuthUtil;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chang on 16/3/24.
 */
public class HttpRequestServiceHttpClientImpl implements HttpRequestService {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestServiceHttpClientImpl.class);

    private static CloseableHttpClient httpClient;

    static{
        RequestConfig config = RequestConfig
                .custom()
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(3000)
                .setConnectTimeout(3000)
                .build();

        httpClient = HttpClients
                .custom()
                .setDefaultRequestConfig(config)
                .setMaxConnTotal(20)
                .setMaxConnPerRoute(20)
                .build();
    }

    public String get(String url,String param) {
        String result = null;
        HttpGet httpGet = null;
        try {
            String requestUrl = url + URLEncoder.encode(param, "utf-8");
            httpGet = new HttpGet(requestUrl);
            httpGet.setHeader("Authorization", BasicAuthUtil.getBasicAuth());

            HttpResponse response = httpClient.execute(httpGet);

            if(HttpStatus.SC_OK != response.getStatusLine().getStatusCode()){
                throw new RuntimeException("reponse code is not 200 >  code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");

            EntityUtils.consume(entity);

            logger.info("url :" + url + ">  response success :" + result);

        } catch (Exception e) {
           logger.error("url :" + url + ">  request error: " ,e);
        } finally {
            if(httpGet != null){
                httpGet.abort();
            }
        }

        return result;
    }

    public String post(String url) {

        String result = null;
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);

            Map<String,String> header = SignAuthUtil.getHeader(url, ChatConfig.REQUEST_METHOD);
            for(Map.Entry<String,String> entry : header.entrySet()) {
                 httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            StringEntity entity = new StringEntity( getJsonData(),"utf-8");
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);

            if(HttpStatus.SC_OK != response.getStatusLine().getStatusCode()){
                throw new RuntimeException("reponse code is not 200 >  code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");

            EntityUtils.consume(entity);

            logger.info("url :" + url + ">  response success :" + result);

        } catch (Exception e) {
            logger.error("url :" + url + ">  request error: " ,e);
        } finally {

            if(httpPost != null){
                httpPost.abort();
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
