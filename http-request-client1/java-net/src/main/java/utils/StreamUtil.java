package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chang on 16/3/26.
 */
public class StreamUtil {
    public static String transformStreamToStr(InputStream inputStream) throws IOException {
        String result =  null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try{
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                result= outputStream.toString();
            }
        }finally {
            outputStream.close();
        }

        return result;
    }



}
