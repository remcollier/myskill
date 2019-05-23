package REST;

import REST.Utils.RequestObject;
import REST.Utils.URL;
import REST.Utils.WebResponse;
import REST.Utils.WebUtils;

public class SinglePlayerApi {
    RequestObject obj = new RequestObject();

    public SinglePlayerApi() {
        obj.type = "application/json";
        obj.method = "POST";
    }

    public void sendScore(String object) {
        URL url = new URL("/score");
        obj.url = url.getUrl();
        obj.content = object;
        WebResponse rest = WebUtils.sendRequest(obj);
        System.out.println(rest.getContent());
    }

    public String sendToken(String object) {
        URL url = new URL("/token");
        obj.url = url.getUrl();
        obj.content = object;
        WebResponse rest = WebUtils.sendRequest(obj);
        System.out.println(rest.getContent());
        return rest.getContent();
    }


}




