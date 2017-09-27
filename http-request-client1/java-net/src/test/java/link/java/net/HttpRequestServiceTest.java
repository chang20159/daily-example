package link.java.net;

import link.java.net.config.ChatConfig;
import link.java.net.config.WikiConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * Created by chang on 16/3/26.
 */
public class HttpRequestServiceTest extends BaseTest {

    @Qualifier("httpRequestHttpClient")
    @Autowired
    private HttpRequestService httpRequestHttpClient;

    @Qualifier("httpRequestJsoup")
    @Autowired
    private HttpRequestService httpRequestJsoup;

    @Qualifier("httpRequestHttpUrlConn")
    @Autowired
    private HttpRequestService httpRequestHttpUrlConn;

    @Test
    public void getTest(){

        String url = WikiConfig.URL;
        String param = WikiConfig.CLAUSE;
        String httpclientResult = httpRequestHttpClient.get(url,param);
        String jsoupResult = httpRequestJsoup.get(url,param);
        String httpUrlConnResult = httpRequestHttpUrlConn.get(url,param);

        System.out.println("httpclient result :" + httpclientResult);
        System.out.println("jsoup result :" + jsoupResult);
        System.out.println("httpUrlConnection result :" + httpUrlConnResult);

    }

    @Test
    public void postTest(){
        String httpclientResult = httpRequestHttpClient.post(ChatConfig.UID_REQUEST_URL);
        String httpUrlConnResult = httpRequestHttpUrlConn.post(ChatConfig.UID_REQUEST_URL);

        System.out.println("httpclient result :" + httpclientResult);
        System.out.println("httpUrlConnection result :" + httpUrlConnResult);


    }
}
