package SinglePlayer;

import Models.Match;
import Models.Token;
import REST.MultiplayerAPI;
import com.google.gson.Gson;

import java.io.IOException;


public class TestClass {
    public static void main(String[] args) {
        MultiplayerAPI gameApi = new MultiplayerAPI();
//        Token s1 = new Token();
//        s1.setTokenId("1");
//        s1.setAccountId("AB");
//        Gson gson = new Gson();
//        String ss = gson.toJson(s1);
//        s.getPlayers(ss);
        Match match = new Match();
        int count = 0;
        String userID;
        Gson gson = new Gson();
        Token token = new Token();
        userID = "teddfff";
        token.setAccountId(userID);
        String tokenRequest = gson.toJson(token);
        String tokenGiven = gameApi.sendToken(tokenRequest);
        token.setTokenId(tokenGiven);
        String tokenRequest2 = gson.toJson(token);
        try {
            match = gameApi.getPlayers(tokenRequest2);
        } catch (IOException e) {

        }

        System.out.println(match.getPlayerTwo());


    }


}
