package test;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import java.util.Map;

public class SayHelloSpeechlet implements Speechlet {
    GamePlayInfo game = new GamePlayInfo(1,2);
    int current = 0;
    private static final String LETTERS = "letters";


    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException
    {
        System.out.println("onLaunch requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());
        return getWelcomeResponse();
    }

    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException
    {
        System.out.println("onIntent requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        System.out.println("intentName : " + intentName);

        if ("Answer".equals(intentName)) {
            return gameMode(intent,session);
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            return getHelpResponse();
        }
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse()
    {
        // setting up sample questions
        game.setQuestions();
        game.assignLettersToAns(current);
//        session.setAttribute(COLOR_KEY, favoriteColor);

        String speechText = "Welcome to Abdul's Trivia game," + "The Quiz we will be playing is called  "+ game.getQuizname()+
                "The first question is "+ game.getQuestion(0);

        // Create the Simple card content.

        SimpleCard card = new SimpleCard();
        card.setTitle("Answer");
        card.setContent(speechText);

        // Create the plain text output.

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse gameMode(final Intent intent, final Session session)
    {
        String speechText= null;
        Map<String, Slot> slots = intent.getSlots();
        Slot s = slots.get(LETTERS);
        String user_input = s.getValue();
        if(game.checkAnswer(user_input,current))
        {

             speechText ="Well Done ";
             game.addScore();

        }
        else
        {
             speechText = game.output_wrong(game.getAnswer(current));
        }

        speechText += game.output_question(" Well done it worked mate");
        // Create the Simple card content.

        SimpleCard card = new SimpleCard();
        card.setTitle("Answer");
        card.setContent(speechText);

        // Create the plain text output.

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse()
    {
        String speechText = "Hello user, You can say hello to me!";

        // Create the Simple card content.

        SimpleCard card = new SimpleCard();
        card.setTitle("Answer");
        card.setContent(speechText);

        // Create the plain text output.

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException
    {
        System.out.println("onSessionStarted requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());
    }

    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException
    {
        System.out.println("onSessionEnded requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());

    }
}