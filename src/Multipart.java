import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Multipart {
    void start(Long id,String Address, File file, String Content_Type, String name) throws Exception{
        final String two_hyphen = "--";
        final String end = "\r\n";
        final String boundary = "yoonun_botjdkfldjsaf";

        URL url = new URL(Address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(two_hyphen + boundary + end);

        out.writeBytes("Content-Disposition: form-data; name=\"chat_id\"" + end);
        out.writeBytes(end);
        out.writeBytes(id.toString());
        out.writeBytes(end);

        out.writeBytes(two_hyphen + boundary + end);
        out.writeBytes("Content-Disposition: form-data; name=\"" + name +  "\"; filename=\"" + file.getName() + "\"" + end);
        out.writeBytes("Content-Type:" + Content_Type + "/" + file.getName().split("\\.")[1] + end);
        out.writeBytes(end);

        FileInputStream in = new FileInputStream(file);
        int BUFFER_SIZE = 4096;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1){
            out.write(buffer, 0, bytesRead);
        }
        out.writeBytes(end);
        out.writeBytes(two_hyphen + boundary + two_hyphen + end);
        out.flush();
        out.close();

        String line;
        StringBuilder result = new StringBuilder();
        BufferedReader br;

        int status = connection.getResponseCode();
        if(status == HttpURLConnection.HTTP_OK){
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = br.readLine()) != null)
                result.append(line).append('\n');
            System.out.println(result);
        }else{
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while((line = br.readLine()) != null)
                result.append(line).append('\n');
            System.out.println(result);
        }
    }
}
