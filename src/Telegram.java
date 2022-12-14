import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet("/Telegram")
public class Telegram extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws IOException {
        PrintWriter pw = response.getWriter();
        pw.println("<!DOCTYPE html><head><title>bot_site</title></head><body>Hello World!!</body></html>");

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {

            StringBuilder sb = new StringBuilder();

            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb);
            JSONObject jObject = new JSONObject(sb.toString());
            Command cmd = new Command(jObject);
            long key;
            if(jObject.has("edited_message")){
                System.out.println("check");
                key = jObject.getJSONObject("edited_message").getLong("message_id");
                String message = jObject.getJSONObject("edited_message").getString("text");
                long chat_id = jObject.getJSONObject("edited_message").getJSONObject("chat").getLong("id");
                cmd.check_banned(message, key, chat_id);
            }
            key = jObject.getJSONObject("message").getLong("message_id");
            long chat_id = jObject.getJSONObject("message").getJSONObject("chat").getLong("id");
            System.out.println(key);
            String message = jObject.getJSONObject("message").getString("text");
            try {
                JSONArray jArray = jObject.getJSONObject("message").getJSONArray("entities");
                JSONObject obj = jArray.getJSONObject(0);
                String type = obj.getString("type");
                if (type.equals("bot_command")) {
                    String command = message.split(" ")[0];
                    if (command.equals("/hitomi"))
                        cmd.sendHitomi(message.split(" ")[1]);

                    if (command.equals("/mute"))
                        cmd.mute(message.split(" ")[1]);
                    if (command.equals("/unmute"))
                        cmd.unmute(message.split(" ")[1]);
                    if (command.equals("/getinfo")) {
                        cmd.getChat(message.split(" ")[1]);
                    }
                    if (command.equals("/gethitomi"))
                        cmd.sendHitomiZip(message.split(" ")[1]);
                    if (command.equals("/banchat")) {
                        cmd.banChat(message.split(" ")[1]);
                        return;
                    }
                    if (command.equals("/unbanchat"))
                        cmd.unbanChat(message.split(" ")[1]);
                    if(command.equals("/getbanchat"))
                        cmd.banned_list();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            cmd.check_banned(message, key, chat_id);
            JSONObject userdata = jObject.getJSONObject("message").getJSONObject("from");
            cmd.Saveinfo(userdata);

            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
