package REST;
import REST.Utils.RequestObject;
import REST.Utils.WebResponse;
import REST.Utils.WebUtils;

public class GameApi {

    public void sendScore(String object) {
        RequestObject obj = new RequestObject();
        obj.type="application/json";
        obj.method = "POST";
        obj.url = "http://alexa-cs.ucd.ie:8080/score";
//        obj.url = "http://localhost:80/score";
        obj.content= object;
        WebResponse rest = WebUtils.sendRequest(obj);
        System.out.println(rest.getContent());
    }
    public String sendToken (String object) {
        RequestObject obj = new RequestObject();
        obj.type="application/json";
        obj.method = "POST";
        obj.url = "http://alexa-cs.ucd.ie:8080/token";
//        obj.url = "http://localhost:80/token";
        obj.content= object;
        WebResponse rest = WebUtils.sendRequest(obj);
        System.out.println(rest.getContent());
        return rest.getContent();
    }
    }




