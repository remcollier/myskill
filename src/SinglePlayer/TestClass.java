package SinglePlayer;

import Models.Token;
import REST.MultiplayerAPI;
import com.google.gson.Gson;

import java.io.IOException;

public class TestClass {
    public static void main(String[] args) throws IOException {
        MultiplayerAPI s = new MultiplayerAPI();
        Token s1 = new Token();
        s1.setTokenId("1");
        s1.setAccountId("AB");
        Gson gson = new Gson();
        String ss = gson.toJson(s1);
        s.getPlayers(ss);

    }


}
