package REST;

import Models.Match;
import REST.Utils.RequestObject;
import REST.Utils.URL;
import REST.Utils.WebResponse;
import REST.Utils.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class MultiplayerAPI {
    private ObjectMapper mapper = new ObjectMapper();

    private RequestObject obj = new RequestObject();

    public MultiplayerAPI() {
        obj.type = "application/json";
        obj.method = "POST";
    }

    public Match getPlayers(String object) throws IOException {
        URL url = new URL("/online");
        obj.url = url.getUrl();
        obj.content = object;
        Match match = new Match();
        WebResponse rest = WebUtils.sendRequest(obj);
        try {
            match = mapper.readValue(rest.getContent(), Match.class);
        } catch (NullPointerException e) {

            System.out.println(e.getMessage());
        }
        return match;

    }

    public String sendToken(String object) {
        URL url = new URL("/token");
        obj.url = url.getUrl();
        obj.content = object;
        WebResponse rest = WebUtils.sendRequest(obj);
        return rest.getContent();
    }
}
