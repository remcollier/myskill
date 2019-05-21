package Game;

import Models.Token;
import REST.GameApi;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.google.gson.Gson;

import java.io.IOException;

public class Multiplayer extends GameFunctionality {
    private GamePlayInfo game = new GamePlayInfo();
    private GameApi gameApi = new GameApi();
    public Multiplayer() throws IOException {
    }

    public SpeechletResponse startMultiplayer(Intent intent, Session session) {

        String speechText = "Searching for someone to play with online ";
        speechText += sendPlayer(session);
        return createResponse(speechText);

    }



    public String sendPlayer(Session session) {
        String userID;
        Gson gson = new Gson();
        Token token= new Token();
        userID = session.getUser().getUserId();
        token.setAccountId(userID);
        String tokenRequest = gson.toJson(token);
        String tokenGiven = gameApi.sendToken(tokenRequest);
        token.setTokenId(tokenGiven);
        String tokenRequest2 = gson.toJson(token);
        String response = gameApi.sendPlayer(tokenRequest2);
        return response;

    }
    
    
    public String sendMatchScore()
    {
        String response = null;
        return response;
    }


}
