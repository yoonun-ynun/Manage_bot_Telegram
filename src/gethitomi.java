
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class gethitomi {
    void getimage(String key) throws Exception{
        String URL = "https://hitomi.la/galleries/" + key + ".html";

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

        url = new URL("https://btn.hitomi.la/webpbigtn/" + postfix.charAt(2) + "/" + postfix.charAt(0) + postfix.charAt(1) +"/" + hash + ".webp");
        con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Referer", URL);
        con.setRequestMethod("GET");

        InputStream is = con.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(new File("/root/server/apache-tomcat-9.0.68/webapps/ROOT","hitomi.png"));

        final int BUFFER_SIZE = 4096;
        int bytesRead;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = is.read(buffer)) != -1){
            outputStream.write(buffer, 0, bytesRead);
        }


        is.close();
        outputStream.close();

    }
}
