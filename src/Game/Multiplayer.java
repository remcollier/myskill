package Game;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

import java.io.IOException;

public class Multiplayer extends GameFunctionality {

    public Multiplayer() throws IOException {
    }

    public SpeechletResponse startMultiplayer(Intent intent, Session session) {

        String speechText = "Searching for someone to play with online ";
        return createResponse(speechText);

    }
}
