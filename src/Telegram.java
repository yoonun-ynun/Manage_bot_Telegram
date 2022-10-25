import org.json.JSONArray;
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
            JSONArray jArray = jObject.getJSONObject("message").getJSONArray("entities");
            JSONObject obj = jArray.getJSONObject(0);
            String type = obj.getString("type");
            String message = jObject.getJSONObject("message").getString("text");

            if (type.equals("bot_command")) {
                Command cmd = new Command(jObject);
                String command = message.split(" ")[0];
                if(command.equals("/hitomi"))
                    cmd.sendHitomi(message.split(" ")[1]);

                if(command.equals("/mute"))
                    cmd.mute();
            }

            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
