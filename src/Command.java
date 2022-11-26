import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Command {
    static ArrayList<Thread> download = new ArrayList<>();
    static HashMap<Long, Chatinfo> info = new HashMap<>();
    static HashMap<Long, ArrayList<String>> banned = Action.Read_banned();
    JSONObject jObject;
    Command(JSONObject jObject){
        this.jObject = jObject;
    }
    void sendHitomi(String number){
        try {
            File file = new File(new Info().your_path + "/hitomi/", "hitomi.webp");
            JSONObject chat = jObject.getJSONObject("message").getJSONObject("chat");
            Long chat_id = chat.getLong("id");
            Action action = new Action();

            String address = "https://hitomi.la/reader/" + number + ".html";
            System.out.println(address);

            try {
                gethitomi get = new gethitomi();
                get.getimage(number, 1,file);
            }catch (FileNotFoundException e){
                action.SendMessage(chat_id, "일치하는 번호가 없습니다.");
                return;
            }

            action.SendMessage(chat_id, address);
            action.SendPhoto(chat_id, file);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void sendHitomiZip(String number)throws Exception{
        long chat_id = jObject.getJSONObject("message").getJSONObject("chat").getLong("id");
        Thread th = new Thread(new Hitomizip(number, chat_id));
        th.start();
        download.add(th);
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
        if(Command.info.get(chat_id) == null) {
            Chatinfo info = new Chatinfo();
            info.saveUserid(user_name, user_id);
            Command.info.put(chat_id, info);
        }
    }
    void banChat(String text) throws Exception{
        long chat_id = jObject.getJSONObject("message").getJSONObject("chat").getLong("id");
        Action ac = new Action();
        long usage_id = jObject.getJSONObject("message").getJSONObject("from").getLong("id");
        String status = ac.getChatMember(chat_id, usage_id).getJSONObject("result").getString("status");
        if(!(status.equals("creator") || status.equals("administrator"))){
            ac.SendMessage(chat_id, "관리자 이상의 등급만 사용할 수 있습니다.");
            return;
        }
        if(banned.get(chat_id) == null){
            ArrayList<String> ban_list = new ArrayList<>();
            ban_list.add(text);
            banned.put(chat_id, ban_list);
        }else{
            ArrayList<String> ban_list = banned.get(chat_id);
            banned.remove(chat_id);
            ban_list.add(text);
            banned.put(chat_id, ban_list);
        }
        Action.Write_banned();
        ac.SendMessage(chat_id, "성공");
    }
    void unbanChat(String text) throws Exception{
        long chat_id = jObject.getJSONObject("message").getJSONObject("chat").getLong("id");
        Action ac = new Action();
        long usage_id = jObject.getJSONObject("message").getJSONObject("from").getLong("id");
        String status = ac.getChatMember(chat_id, usage_id).getJSONObject("result").getString("status");
        if(!(status.equals("creator") || status.equals("administrator"))){
            ac.SendMessage(chat_id, "관리자 이상의 등급만 사용할 수 있습니다.");
            return;
        }
        ArrayList<String> banned_list = banned.get(chat_id);
        banned_list.remove(text);
        banned.remove(chat_id);
        if(!banned_list.isEmpty()) {
            banned.put(chat_id, banned_list);
        }
        ac.SendMessage(chat_id, "성공");
        Action.Write_banned();
    }
    void check_banned(String text, long message_id) throws Exception{
        long chat_id = jObject.getJSONObject("message").getJSONObject("chat").getLong("id");
        ArrayList<String> ban_list = banned.get(chat_id);
        Action ac = new Action();
        try {
            for (String banned_text : ban_list) {
                if (text.contains(banned_text)) {
                    ac.delete_massage(chat_id, message_id);
                }
            }
        }catch (NullPointerException E){
            return;
        }
    }
    void banned_list(){
        long chat_id = jObject.getJSONObject("message").getJSONObject("chat").getLong("id");
        ArrayList<String> ban_list = banned.get(chat_id);
        Action ac = new Action();
        StringBuilder sb = new StringBuilder();
        sb.append("금지어 목록").append("\n");
        sb.append("chat id: ").append(chat_id).append("\n");
        try{
            for(String banned_text:ban_list){
                sb.append(banned_text).append("\n");
            }
        }catch (NullPointerException e){
            ac.SendMessage(chat_id, "금지어가 없습니다.");
        }
        System.out.println(sb.toString());
        ac.SendMessage(chat_id, sb.toString());
    }
}
