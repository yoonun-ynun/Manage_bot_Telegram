
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class gethitomi {
    static int count = 0;
    void getimage(String key) throws Exception{
        String URL = "https://hitomi.la/reader/" + key + ".html#1";

        URL url = new URL("https://ltn.hitomi.la/galleries/" + key + ".js");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        JSONObject info = new JSONObject(sb.toString().substring(18));

        String hash = info.getJSONArray("files").getJSONObject(0).getString("hash");
        String postfix = hash.substring(hash.length()-3);

        int secret = Integer.parseInt(new StringBuilder().append(postfix.charAt(2)).append(postfix.charAt(0)).append(postfix.charAt(1)).toString(), 16);

        String hitomiurl = "https://" + new gethitomisub().check(secret) + "a.hitomi.la/webp/1666922401/" + secret +"/" +  hash + ".webp" ;

        url = new URL(hitomiurl);
        con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Referer", URL);
        con.setRequestMethod("GET");

        InputStream is = con.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(new File("your_path","hitomi0" + count + ".png"));

        final int BUFFER_SIZE = 4096;
        int bytesRead;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = is.read(buffer)) != -1){
            outputStream.write(buffer, 0, bytesRead);
        }
        count++;

        is.close();
        outputStream.close();

    }
}
