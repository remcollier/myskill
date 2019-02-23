package REST;
import REST.WebREST.RequestObject;
import REST.WebREST.WebResponse;
import REST.WebREST.WebUtils;

public class GameApi {

    public void sendScore(String object) {
        RequestObject obj = new RequestObject();
        obj.type="application/json";
        obj.method = "POST";
//        obj.url = "http://alexa-cs.ucd.ie:8080/api/allquizzes";
        obj.url = "http://localhost:80/score";
        obj.content= object;
        WebResponse rest = WebUtils.sendRequest(obj);
        System.out.println(rest.getContent());
    }
    public void sendToken (String object) {
        RequestObject obj = new RequestObject();
        obj.type="application/json";
        obj.method = "POST";
//        obj.url = "http://alexa-cs.ucd.ie:8080/api/allquizzes";
        obj.url = "http://localhost:80/token";
        obj.content= object;
        WebResponse rest = WebUtils.sendRequest(obj);
        System.out.println(rest.getContent());
    }
    }




