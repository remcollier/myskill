package Game;

import Models.Token;
import REST.GameApi;
import com.google.gson.Gson;

import java.io.IOException;

public class TestClass {
    public static void main(String[] args) throws IOException {
        GameApi s = new GameApi();
        Token s1 = new Token();
        s1.setTokenId("dsss");
        s1.setAccountId("ABBBB");
        Gson gson = new Gson();
        String ss = gson.toJson(s1);
        System.out.println(s.sendPlayer(ss));
    }


}
