import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class gethitomisub {
    public String code;
    char check(int id) throws Exception{
        URL url = new URL("https://ltn.hitomi.la/gg.js");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            sb.append(line).append('\n');
        }

        code = sb.substring((sb.length()-16), (sb.length())-6);

        String jscode = sb.substring(24, (sb.length()-115)).replace("function(g) {", "function get(g){");

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        engine.eval(jscode);

        Invocable invoc = (Invocable) engine;
        Object object = invoc.invokeFunction("get", id);
        Integer num = Integer.parseInt(object.toString().substring(2,3));

        if(num == 0)
            return 'b';
        else
            return 'a';
    }
}