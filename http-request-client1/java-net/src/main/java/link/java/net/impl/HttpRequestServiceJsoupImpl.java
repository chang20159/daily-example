package link.java.net.impl;

import link.java.net.HttpRequestService;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.BasicAuthUtil;

import java.net.URLEncoder;

/**
 * Created by chang on 16/3/25.
 */
public class HttpRequestServiceJsoupImpl implements HttpRequestService {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestServiceJsoupImpl.class);


    String result;
    public String get(String url,String param) {
        try{
            String requestUrl = url + URLEncoder.encode(param, "utf-8");
            result = Jsoup.connect(requestUrl)
                    .header("Authorization", BasicAuthUtil.getBasicAuth())
                    .ignoreContentType(true)
                    .timeout(3000)
                    .execute()
                    .body();   Jsoup.connect(url).data();
        }catch(Exception e){
            logger.error("url :" + url + ">  request error: ", e);
        }

        return result;
    }

    public String post(String url) {
        return null;
    }
}
