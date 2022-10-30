import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

public class Action{
    String Token = new Info().your_token;
    String Address;
    Action(){
        Address = "https://api.telegram.org/bot" + Token + "/";
    }
    Action(String Token){
        this.Token = Token;
        Address = "https://api.telegram.org/bot" + this.Token + "/";
    }

    void SendMessage(Long id, String text){
        try {
            String Address = this.Address + "sendMessage" + "?chat_id=" + id + "&text=" + text;
            URL url = new URL(Address);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            InputStream result = con.getInputStream();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void SendPhoto(Long id, String image){
        try {
            String Address = this.Address + "sendPhoto" + "?chat_id=" + id + "&photo=" + image;
            URL url = new URL(Address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            InputStream result = con.getInputStream();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void SendPhoto(Long id, File file) throws Exception{
        final String two_hyphen = "--";
        final String end = "\r\n";
        final String boundary = "yoonun_botjdkfldjsaf";

        String Address = this.Address + "sendPhoto";
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
        out.writeBytes("Content-Disposition: form-data; name=\"photo\"; filename=\"" + file.getName() + "\"" + end);
        out.writeBytes("Content-Type: image/" + file.getName().split("\\.")[1] + end);
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

    void ChatPermissions
            (Long chat_id, Long mute_id,Boolean can_send_messages, Boolean can_send_media_messages, Boolean can_send_polls, Boolean can_send_other_messages, Boolean can_add_web_page_previews, Boolean can_change_info,Boolean can_invite_users, Boolean can_pin_messages) throws Exception{
        JSONObject sending = new JSONObject();
        sending.append("{can_send_messages:",can_send_messages).append("can_send_media_messages",can_send_media_messages).append("can_send_polls",can_send_polls).append("can_send_other_messages", can_send_other_messages).append("can_add_web_page_previews",can_add_web_page_previews).append("can_change_info",can_change_info).append("can_invite_users",can_invite_users).append("can_pin_messages",can_pin_messages);


        String Address = this.Address + "restrictChatMember?" + "chat_id=" + chat_id + "&user_id=" + mute_id + "&permissions=" + sending;
        Address = Address.replaceAll("[\\[\\[\\]]","");

        URL url = new URL(Address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        InputStream result = con.getInputStream();

    }

    JSONObject getChatMember(Long chat_id, Long user_id) throws Exception{

        String Address = this.Address + "getChatMember?" + "chat_id=" + chat_id + "&user_id=" + user_id;
        URL url = new URL(Address);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null){
            sb.append(line).append('\n');
        }
        return new JSONObject(sb.toString());
    }
    JSONObject getChat(String chat_id) throws Exception{
        String Address = this.Address + "getChat?" + "chat_id=" + chat_id;
        URL url = new URL(Address);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null){
            sb.append(line);
        }
        return new JSONObject(sb.toString());
    }
}