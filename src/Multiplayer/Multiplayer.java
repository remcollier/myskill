package Multiplayer;

import Models.Match;
import Models.Player;
import SinglePlayer.SinglePlayer;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.interfaces.system.SystemInterface;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.amazon.speech.speechlet.services.DirectiveEnvelope;
import com.amazon.speech.speechlet.services.DirectiveEnvelopeHeader;
import com.amazon.speech.speechlet.services.DirectiveService;
import com.amazon.speech.speechlet.services.SpeakDirective;

import java.io.IOException;

public class Multiplayer extends SinglePlayer {

    private MultiplayerGameFunctionaility multiplayerGameFunctionaility = new MultiplayerGameFunctionaility();
    private Match match = new Match();
    private String PLAYER1 = "Player1";
    private String PLAYER2 = "Player2";
    private String gameMode = "GAMEMODE";
    private int MAX_QUESTIONS = 0;
    private DirectiveService directiveService;

    public Multiplayer() throws IOException {
    }

    public SpeechletResponse startMultiplayer(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) throws IOException {
        String speechText = "";
        IntentRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();
        SystemState systemState = getSystemState(requestEnvelope.getContext());
        String apiEndpoint = systemState.getApiEndpoint();
        session.setAttribute(gameMode, 1);

        dispatchProgressiveResponse(request.getRequestId(), "Searching for a user", systemState, apiEndpoint);

//        match = multiplayerGameFunctionaility.enterPlayer(session);
//        if (match.getPlayerOne() != null && match.getPlayerTwo() != null) {
//            startGameSetUp(session);
//            speechText += "Opponent found, Get ready, The quiz chosen is:"
//                    + multiplayerGameFunctionaility.getQuizname()
//                    + multiplayerGameFunctionaility.questionSingleOutput(multiplayerGameFunctionaility.getQuestion(0));
//
//
//        } else {
//            //add a timer and stuff
//            match = multiplayerGameFunctionaility.enterPlayer(session);
//
//
//        }
        return createResponse(speechText);

    }


    public void startGameSetUp(Session session) throws IOException {
        Player player1 = new Player(match.getPlayerOne());
        Player player2 = new Player(match.getPlayerTwo());
        session.setAttribute(PLAYER1, player1);
        session.setAttribute(PLAYER2, player2);
        multiplayerGameFunctionaility.GameSetupMode();
        MAX_QUESTIONS = multiplayerGameFunctionaility.getNumofQuestions();
        multiplayerGameFunctionaility.assignAnswers(0);
    }


    public SpeechletResponse PlayerResponse(Intent intent, Session session) throws IOException {
        String speechText = "";
        return createResponse(speechText);

    }


    private void dispatchProgressiveResponse(String requestId, String text, SystemState systemState, String apiEndpoint) {
        DirectiveEnvelopeHeader header = DirectiveEnvelopeHeader.builder().withRequestId(requestId).build();
        SpeakDirective directive = SpeakDirective.builder().withSpeech(text).build();
        DirectiveEnvelope directiveEnvelope = DirectiveEnvelope.builder()
                .withHeader(header).withDirective(directive).build();

        if (systemState.getApiAccessToken() != null && !systemState.getApiAccessToken().isEmpty()) {
            String token = systemState.getApiAccessToken();
            try {
                directiveService.enqueue(directiveEnvelope, apiEndpoint, token);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Helper method that retrieves the system state from the request context.
     *
     * @param context request context.
     * @return SystemState the systemState
     */
    private SystemState getSystemState(Context context) {
        return context.getState(SystemInterface.class, SystemState.class);
    }


}
