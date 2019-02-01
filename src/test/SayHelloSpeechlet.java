package test;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import java.util.Map;

public class SayHelloSpeechlet implements Speechlet {
    private GamePlayInfo game = new GamePlayInfo(1, 10);
    private String CURRENT = "Current";
    private String FINALSCORE = "Score";
    private static final String LETTERS = "letters";
    private int current = 0;
    private int score = 0;
    private int MAX_QUESTIONS = 4;
    private static String REPLIES[] = {" You scored, Quiz Finnished would you like to play again or end game"};

    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        System.out.println("onLaunch requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());

        return getWelcomeResponse(session);
    }

    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        System.out.println("onIntent requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        System.out.println("intentName : " + intentName);

        if ("Answer".equals(intentName)) {
            return gameMode(intent, session);
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            return getHelpResponse();
        }
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent try get it to generate random quizzes rather than the user choosing next quiz
     */
    private SpeechletResponse getWelcomeResponse(Session session) {
        // setting up sample questions
        game.setQuestions();
        //setting up sessions
        session.setAttribute(CURRENT, 0);
        session.setAttribute(FINALSCORE, 0);
        game.assignAnswers(0);
        String speechText = game.getWelcomeMessage();

        // Create the Simple card content.

        SimpleCard card = new SimpleCard();
        card.setTitle("Answer");
        return createResponse(card, speechText);

    }

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    // now task is to set up a game where max number is invovled
    private SpeechletResponse gameMode(final Intent intent, final Session session) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Answer");
        current = (int) session.getAttribute(CURRENT);
        score = (int) session.getAttribute(FINALSCORE);
        String speechText;
        Map<String, Slot> slots = intent.getSlots();
        Slot s = slots.get(LETTERS);
        String user_input = s.getValue();
//        speechText = String.valueOf(game.checkAnswer("A"));

        if (game.checkAnswer(user_input)) {
            speechText = "Well Done ";
            score++;
            session.setAttribute(FINALSCORE, score);
            current++;
            session.setAttribute(CURRENT, current);
            speechText += checkReachedEnd(session);
            
        } else {
            speechText = game.output_wrong(game.getAnswer((Integer) session.getAttribute(CURRENT)));
            current++;
            session.setAttribute(CURRENT, current);
            speechText += checkReachedEnd(session);
        }


        return createResponse(card, speechText);


    }

    public String checkReachedEnd(Session session) {

        if ((Integer) session.getAttribute(CURRENT) < MAX_QUESTIONS) {
            game.assignAnswers(current);
            return game.output_question(game.getQuestion((Integer) session.getAttribute(CURRENT)));

        } else {
            return REPLIES[0];

        }
    }

    public SpeechletResponse createResponse(SimpleCard card, String speechText) {
        card.setContent(speechText);
        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
//        SpeechletResponse.setNullableShouldEndSession(false);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);
        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }


    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "Hello user, You can say hello to me!";

        // Create the Simple card content.

        SimpleCard card = new SimpleCard();
        card.setTitle("Answer");

        return createResponse(card, speechText);
    }

    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        System.out.println("onSessionStarted requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());

    }

    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        System.out.println("onSessionEnded requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());

    }
}