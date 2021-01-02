package lau.NovelDL;

import javax.xml.ws.http.HTTPException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * NovelDL, the best application for Chinese readers
 *
 * @author layou233
 */

public class Weber {
    // If there is a network error, an IOException will be thrown to remind the user
    public static String getWebString(String url, String charset) throws IOException {
        URL realUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        /*
        Use timestamp to ensure that the UA posted is different each time
        Also use the UA of the Baiduspider to disguised itself
        Also, you can use Googlebot's UA instead of Baiduspider

        Googlebot UA= Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)
         */
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)" + System.currentTimeMillis());
        connection.connect();
        if (connection.getResponseCode() == 200) {
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[10485760]; // 10MiB Buffer
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            return baos.toString(charset);
        } else {
            throw new HTTPException(connection.getResponseCode());
        }
    }
}
