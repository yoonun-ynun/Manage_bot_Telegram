import java.util.HashMap;

public class Chatinfo {
    static private final HashMap<String, Long> userid = new HashMap<>();
    long getUserid (String name) throws Exception{
        long id = userid.get(name);
        return id;
    }
    void saveUserid (String name, long id){
        if(userid.get(name) != null)
            return;
        userid.put(name, id);
    }
}
