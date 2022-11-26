import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

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

    int SendMessage(Long id, String text){
        try {
            String Address = this.Address + "sendMessage" + "?chat_id=" + id + "&text=" + text;
            URL url = new URL(Address);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
                sb.append(line).append('\n');
            JSONObject ob = new JSONObject(sb.toString());

            return ob.getJSONObject("result").getInt("message_id");
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
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
        String Address = this.Address + "sendPhoto";
        Multipart multi = new Multipart();
        multi.start(id, Address, file, "image", "photo");
    }

    void SendDocument(long id, File file) throws Exception{
        String Address = this.Address + "sendDocument";
        Multipart multi = new Multipart();
        multi.start(id, Address, file, "document", "document");
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
    void Edittext(long chat_id, int message_id, String text) throws Exception{
        String Address = this.Address + "editMessageText?" + "chat_id=" + chat_id + "&message_id=" + message_id + "&text=" + text;
        URL url = new URL(Address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader result = new BufferedReader(new InputStreamReader(con.getInputStream()));
    }
    void delete_massage(long chat_id, long message_id) throws Exception{
        String Address = this.Address + "deleteMessage?" + "chat_id=" + chat_id + "&message_id=" + message_id;
        URL url = new URL(Address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        InputStream result = con.getInputStream();
    }

    static void Write_banned(){
        try {
            File save = new File(new Info().path, "banchat.txt");
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(save)));
            } catch (FileNotFoundException E) {
                E.printStackTrace();
                return;
            }
            HashMap<Long, ArrayList<String>> banned = Command.banned;
            for (Map.Entry<Long, ArrayList<String>> entry : banned.entrySet()) {
                bw.write(Long.toString(entry.getKey()));
                bw.newLine();
                for(String text:entry.getValue()){
                    bw.write(text);
                    bw.newLine();
                }
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    static HashMap<Long, ArrayList<String>> Read_banned(){
        HashMap<Long, ArrayList<String>> result = new HashMap<>();
        try {
            ArrayList<String> banned = new ArrayList<>();
            File read = new File(new Info().path, "banchat.txt");
            boolean check = false;
            long id = 0L;
            BufferedReader br = null;
            try{
                br = new BufferedReader(new InputStreamReader(new FileInputStream(read)));
            }catch (FileNotFoundException e){
                return result;
            }
            String input = "";
            while((input = br.readLine()) != null){
                if(!input.equals("")){
                    if(!check){
                        id = Long.parseLong(input);
                        check = true;
                    }else{
                        banned.add(input);
                    }
                }else{
                    result.put(id, banned);
                    banned = new ArrayList<>();
                    id = 0L;
                    check = false;
                }
            }
            br.close();
            return result;
        }catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }
}