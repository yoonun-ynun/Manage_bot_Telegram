import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Action{
    String Token = "your_token";
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