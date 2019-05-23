package test;

import SinglePlayer.SinglePlayer;
import Multiplayer.Multiplayer;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;

import java.io.IOException;

public class SayHelloSpeechlet extends SinglePlayer implements SpeechletV2 {


    private SinglePlayer functionality;
    private Multiplayer multiplayer;
    // 0 for single, 1 for multiplayer
    private String gameMode = "GAMEMODE";


    public SayHelloSpeechlet() throws IOException {
        functionality = new SinglePlayer();
        multiplayer = new Multiplayer();
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

        IntentRequest request = requestEnvelope.getRequest();
        System.out.println("onIntent requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        System.out.println("intentName : " + intentName);

        if ("SelectQuiz".equals(intentName) & !functionality.isGameSesssion() & session.getAttribute(gameMode).equals(0)) {
            return functionality.startQuizRandom(intent, session);
        } else if ("Answer".equals(intentName) & functionality.isGameSesssion() & session.getAttribute(gameMode).equals(0)) {
            return functionality.gameMode(intent, session);
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
//            functionality.setGameSesssion(true);
            try {
                return multiplayer.startMultiplayer(intent, session);
            } catch (IOException e) {
                e.printStackTrace();
            }

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
}

