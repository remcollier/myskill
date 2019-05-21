package test;

import Game.GameFunctionality;
import Game.Multiplayer;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;

import java.io.IOException;

public class SayHelloSpeechlet extends GameFunctionality implements SpeechletV2 {


    GameFunctionality functionality;
    Multiplayer multiplayer;

    public SayHelloSpeechlet() throws IOException {
        functionality = new GameFunctionality();
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

        if ("SelectQuiz".equals(intentName) & !functionality.isGameSesssion()) {
            return functionality.startQuizRandom(intent, session);
        } else if ("Answer".equals(intentName) & functionality.isGameSesssion()) {
            return functionality.gameMode(intent, session);
        } else if ("Token".equals(intentName)) {
            return functionality.getToken(session);
        } else if ("StartAgain".equals(intentName) & !functionality.isGameSesssion()) {
            try {
                return functionality.startAgain(intent, session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("End".equals(intentName) & functionality.getCurrent() != functionality.getMAX_QUESTIONS() & functionality.isGameSesssion()) {
            return functionality.endQuiz(intent, session);
        } else if ("Repeat".equals(intentName)) {
            return functionality.repeatQuestion(intent, session);
        } else if ("Skip".equals(intentName) & functionality.isGameSesssion()) {
            return functionality.skipQuestion(intent, session);
        } else if ("Myscore".equals(intentName) & functionality.isGameSesssion()) {
            return functionality.getMyScore(intent, session);
        } else if ("Multiplayer".equals(intentName) & !functionality.isGameSesssion()) {
            functionality.setGameSesssion(true);
            return multiplayer.startMultiplayer(intent, session);
        } else if ("Difficulty".equals(intentName) & functionality.isOptionsGiven()) {

            try {
                return functionality.startRandomGame(intent, session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("PlayNext".equals(intentName) & functionality.getCurrent() == functionality.getMAX_QUESTIONS() & !functionality.isGameSesssion()) {
            try {
                if (!functionality.isDifficultyChoosen()) {
                    return functionality.startQuizRandom(intent, session);

                } else {
                    return functionality.playNextQuiz(intent, session);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if ("PlayQuizOD".equals(intentName) & !functionality.isGameSesssion()) {
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

