package test;

import SinglePlayer.SinglePlayer;
import Multiplayer.Multiplayer;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.interfaces.system.SystemInterface;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.amazon.speech.speechlet.services.*;

import java.io.IOException;

public class SayHelloSpeechlet extends SinglePlayer implements SpeechletV2 {


    private SinglePlayer functionality;
    private Multiplayer multiplayer;
    // 0 for single, 1 for multiplayer
    private String gameMode = "GAMEMODE";
    private DirectiveService directiveService;


    public SayHelloSpeechlet(DirectiveServiceClient directiveServiceClient) throws IOException {
        functionality = new SinglePlayer();
        multiplayer = new Multiplayer();
        this.directiveService = directiveServiceClient;

    }


    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        LaunchRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();
        System.out.println("onLaunch requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());
        return functionality.getWelcomeResponse();
    }


    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        Session session = requestEnvelope.getSession();
        SystemState systemState = getSystemState(requestEnvelope.getContext());
        String apiEndpoint = systemState.getApiEndpoint();
        IntentRequest request = requestEnvelope.getRequest();
        System.out.println("onIntent requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        System.out.println("intentName : " + intentName);

        if ("SelectQuiz".equals(intentName) & !functionality.isGameSesssion() & session.getAttribute(gameMode).equals(0)) {
            return functionality.startQuizRandom(intent, session);
        } else if ("Answer".equals(intentName)) {
            if (functionality.isGameSesssion() & session.getAttribute(gameMode).equals(0)) {
                System.out.println("11");

                return functionality.gameMode(intent, session);

            } else {
                System.out.println("2222");
                return multiplayer.playerResponse(intent, session);
            }
        } else if ("Token".equals(intentName)) {
            return functionality.getToken(session);
        } else if ("StartAgain".equals(intentName) & !functionality.isGameSesssion() & session.getAttribute(gameMode).equals(0)) {
            try {
                return functionality.startAgain(intent, session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("End".equals(intentName) & functionality.getCurrent() != functionality.getMAX_QUESTIONS() & functionality.isGameSesssion() & session.getAttribute(gameMode).equals(0)) {
            return functionality.endQuiz(intent, session);
        } else if ("Repeat".equals(intentName) & session.getAttribute(gameMode).equals(0)) {
            return functionality.repeatQuestion(intent, session);
        } else if ("Skip".equals(intentName) & functionality.isGameSesssion() & session.getAttribute(gameMode).equals(0)) {
            return functionality.skipQuestion(intent, session);
        } else if ("Myscore".equals(intentName) & functionality.isGameSesssion() & session.getAttribute(gameMode).equals(0)) {
            return functionality.getMyScore(intent, session);
        } else if ("Multiplayer".equals(intentName) & !functionality.isGameSesssion()) {
            dispatchProgressiveResponse(request.getRequestId(), "Searching for another player ", systemState, apiEndpoint);
            return multiplayer.startMultiplayer(requestEnvelope);

//        } else if ("Answer".equals(intentName) & session.getAttribute(gameMode).equals(1) & !functionality.isGameSesssion()) {
//        } else if ("Answer".equals(intentName)) {
//
//            return multiplayer.playerResponse(intent, session);

        } else if ("Difficulty".equals(intentName) & functionality.isOptionsGiven() & session.getAttribute(gameMode).equals(0)) {

            try {
                return functionality.startRandomGame(intent, session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("PlayNext".equals(intentName) & functionality.getCurrent() == functionality.getMAX_QUESTIONS() & !functionality.isGameSesssion() & session.getAttribute(gameMode).equals(0)) {
            try {
                if (!functionality.isDifficultyChoosen()) {
                    return functionality.startQuizRandom(intent, session);

                } else {
                    return functionality.playNextQuiz(intent, session);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if ("PlayQuizOD".equals(intentName) & !functionality.isGameSesssion() & session.getAttribute(gameMode).equals(0)) {
            try {
                return functionality.playQuizOD(intent, session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return functionality.getHelpResponse();
        }

        return functionality.getHelpResponse();
    }


    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        SessionStartedRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();
        System.out.println("onSessionStarted requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());
        session.setAttribute(functionality.getQuizDiffculty(), false);
        functionality.setUserID(session.getUser().getUserId());
        functionality.setGameSesssion(false);
        session.setAttribute(gameMode, 0);
        session.setAttribute(functionality.getCHECK(), "false");

    }

    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        SessionEndedRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();
        System.out.println("onSessionEnded requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());
        functionality.setScore(0);
        functionality.setCurrent(0);

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

