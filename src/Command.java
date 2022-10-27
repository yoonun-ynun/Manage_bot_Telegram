import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Command {
    static HashMap<Long, Chatinfo> info = new HashMap<>();
    JSONObject jObject;
    Command(JSONObject jObject){
        this.jObject = jObject;
    }
    void sendHitomi(String number){
        try {
            JSONObject chat = jObject.getJSONObject("message").getJSONObject("chat");
            Long chat_id = chat.getLong("id");
            Action action = new Action();
            String address = "https://hitomi.la/reader/" + number + ".html";
            System.out.println(address);

            URL url = new URL("https://koromo.xyz/api/search/ehash?id=" + number);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            JSONObject ex = new JSONObject(new BufferedReader(new InputStreamReader(con.getInputStream())).readLine());
            try {
                String token = ex.getString("result");


            url = new URL("https://api.e-hentai.org/api.php");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;utf-8");
            con.setRequestProperty("Accept", "application/json;utf-8");
            con.setDoOutput(true);

            String jsonInput = "{\"method\": \"gdata\",\"gidlist\": [[" + number + ",\"" + token + "\"]],\"namespace\": 1}";
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            bw.write(jsonInput);
            bw.flush();
            bw.close();

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String list;
            while ((list = br.readLine()) != null){
                sb.append(list).append('\n');
            }

            JSONObject ex_hentai = new JSONObject(sb.toString());
            String image_url = ex_hentai.getJSONArray("gmetadata").getJSONObject(0).getString("thumb");
            action.SendMessage(chat_id, address);
            action.SendPhoto(chat_id, image_url);
            }catch(JSONException e){
                action.SendMessage(chat_id, address);
                action.SendMessage(chat_id, "이미지를 불러올 수 없습니다.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void mute(String name) throws Exception{

        long usage_id = jObject.getJSONObject("message").getJSONObject("from").getLong("id");
        long chat_id = jObject.getJSONObject("message").getJSONObject("chat").getLong("id");
        long mute_id;
        if(name.charAt(0) == '@'){
            mute_id = info.get(chat_id).getUserid(name.replaceAll("@", ""));
        }else {
            mute_id = jObject.getJSONObject("message").getJSONArray("entities").getJSONObject(1).getJSONObject("user").getLong("id");
        }
        Action ac = new Action();

        String status = ac.getChatMember(chat_id, usage_id).getJSONObject("result").getString("status");
        String mute_status = ac.getChatMember(chat_id, mute_id).getJSONObject("result").getString("status");
        if(!(status.equals("creator") || status.equals("administrator"))){
            ac.SendMessage(chat_id, "관리자 이상의 등급만 사용할 수 있습니다.");
            return;
        }
        if(mute_status.equals("creator") || mute_status.equals("administrator")){
            ac.SendMessage(chat_id, "관리자 권한 이상의 등급을 가진 유저는 뮤트가 불가능합니다.");
            return;
        }

        ac.ChatPermissions(chat_id, mute_id, false, false, false, false, false, false, true,false);

        ac.SendMessage(chat_id, new Unicodekor().uniToKor(name) + "님을 뮤트하였습니다.");
    }

    void unmute(String name) throws Exception {
        long usage_id = jObject.getJSONObject("message").getJSONObject("from").getLong("id");
        long chat_id = jObject.getJSONObject("message").getJSONObject("chat").getLong("id");
        long mute_id;
        if(name.charAt(0) == '@'){
            mute_id = info.get(chat_id).getUserid(name.replaceAll("@", ""));
        }else {
            mute_id = jObject.getJSONObject("message").getJSONArray("entities").getJSONObject(1).getJSONObject("user").getLong("id");
        }
        Action ac = new Action();

        String status = ac.getChatMember(chat_id, usage_id).getJSONObject("result").getString("status");
        if(!(status.equals("creator") || status.equals("administrator"))){
            ac.SendMessage(chat_id, "관리자 이상의 등급만 사용할 수 있습니다.");
            return;
        }

        ac.ChatPermissions(chat_id, mute_id, true, true, true, true, true, true, true, true);
        ac.SendMessage(chat_id, new Unicodekor().uniToKor(name) + "님을 뮤트 해제하였습니다.");
    }

    void RSP(String input) throws Exception{

    }
    void getChat(String name) throws Exception{
        Long chat_id = jObject.getJSONObject("message").getJSONObject("chat").getLong("id");
        Action ac = new Action();
        JSONObject object = ac.getChat(name);
        System.out.println(object.toString());
    }
    void Saveinfo(JSONObject data){
        long chat_id = jObject.getJSONObject("message").getJSONObject("chat").getLong("id");
        long user_id = data.getLong("id");
        String user_name = data.getString("username");
        Chatinfo info = new Chatinfo();
        info.saveUserid(user_name, user_id);
        this.info.put(chat_id, info);
    }
}
