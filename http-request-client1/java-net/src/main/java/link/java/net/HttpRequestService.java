package link.java.net;



/**
 * Created by chang on 16/3/24.
 */
public interface HttpRequestService {

    String get(String url, String param);

    String post(String url);

}
