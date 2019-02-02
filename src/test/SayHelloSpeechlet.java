package test;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import java.util.Map;

public class SayHelloSpeechlet implements Speechlet {
    private static final String DECISION = "QuizDecision";
    private String CURRENT = "Current";
    private String FINALSCORE = "Score";
    private static final String LETTERS = "letters";
    private int current = 0;
    private int score = 0;
    private int MAX_QUESTIONS = 4;
    private static String REPLIES[] = {" You have scored ", " out of ", " ...would you like to play again or end game"};
    private GamePlayInfo game = new GamePlayInfo(1, MAX_QUESTIONS);

    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        System.out.println("onLaunch requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());

        return getWelcomeResponse();
    }

    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        System.out.println("onIntent requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        System.out.println("intentName : " + intentName);

        if ("SelectQuiz".equals(intentName)) {
            return startQuizGame(intent, session);
        } else if ("Answer".equals(intentName)) {
            return gameMode(intent, session);
        } else if ("StartAgain".equals(intentName)) {
            return startAgain(intent, session);
        } else if ("End".equals(intentName)) {
            return endQuiz(intent, session);
        } else if ("Repeat".equals(intentName)) {
            return repeatQuestion(intent, session);
        } else if ("Skip".equals(intentName)) {
            return skipQuestion(intent, session);
        } else if ("Myscore".equals(intentName)) {
            return getMyScore(intent, session);
        } else {
            return getHelpResponse();
        }
    }

    private SpeechletResponse getMyScore(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = REPLIES[0] + session.getAttribute(FINALSCORE) + " so far " + REPLIES[1] + session.getAttribute(CURRENT);
        return createResponse(card, speechText);
    }

    private SpeechletResponse skipQuestion(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        current++;
        session.setAttribute(CURRENT, current);
        String speechText = " You have skipped this question " + checkReachedEnd(session);

        return createResponse(card, speechText);
    }

    private SpeechletResponse repeatQuestion(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));
        return createResponse(card, speechText);
    }

    private SpeechletResponse endQuiz(Intent intent, Session session) {
        session.setAttribute(CURRENT, MAX_QUESTIONS);
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = " You have ended the quiz " + REPLIES[0] + session.getAttribute(FINALSCORE) + REPLIES[1] + MAX_QUESTIONS + ", Would you like to play this quiz again, play a different quiz or quit";
        return createResponse(card, speechText);
    }

    private SpeechletResponse startAgain(Intent intent, Session session) {
        game.setQuestions();
        session.setAttribute(CURRENT, 0);
        session.setAttribute(FINALSCORE, 0);
        score = 0;
        current = 0;
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        game.assignAnswers(current);
        String speechText = "You have started again, " + game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));
        return createResponse(card, speechText);
    }

    //decided if quiz is right if not move to next
    private SpeechletResponse startQuizGame(Intent intent, Session session) {
        Map<String, Slot> slots = intent.getSlots();
        Slot s = slots.get(DECISION);
        String speechText = " ";
        if (current == 0) {
            if (s.getValue().equalsIgnoreCase("Accept")) {
                game.setQuestions();
                //setting up sessions
                session.setAttribute(CURRENT, 0);
                session.setAttribute(FINALSCORE, 0);
                game.assignAnswers(0);
                speechText += game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));

            } else if (s.getValue().equalsIgnoreCase("Deny")) {
                //add random quiz selector here
                speechText += " You have chosen a different Quiz, " + game.getWelcomeQuizMessage();

            } else {

                getWelcomeResponse();
            }
        } else {
            getHelpResponse();
        }

        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        return createResponse(card, speechText);
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent try get it to generate random quizzes rather than the user choosing next quiz
     */
    private SpeechletResponse getWelcomeResponse() {
//        // setting up sample questions
        game.setQuizname("Example");
        String speechText = "Welcome to Abdul's Quiz Trivia game. To answer a question select one of the letters. Get Ready,  a quiz will be chosen at random. " + game.getWelcomeQuizMessage();
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Welcome");
        return createResponse(card, speechText);

    }

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse gameMode(final Intent intent, final Session session) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = " ";
        if (game.getRandomQuestions().isEmpty()) {
            return getWelcomeResponse();
        } else {
            current = (int) session.getAttribute(CURRENT);
            score = (int) session.getAttribute(FINALSCORE);
            Map<String, Slot> slots = intent.getSlots();
            if (intent.getSlot(LETTERS).getName().equalsIgnoreCase(LETTERS)) {
                Slot s = slots.get(LETTERS);
                if (!s.getValue().isEmpty() && (Integer) session.getAttribute(CURRENT) < MAX_QUESTIONS) {
                    String user_input = s.getValue();
                    if (game.checkAnswer(user_input)) {
                        speechText = game.getCongrats();
                        score++;
                        session.setAttribute(FINALSCORE, score);
                        current++;
                        session.setAttribute(CURRENT, current);
                        speechText += checkReachedEnd(session);
                    } else {
                        speechText = game.outputIncorrectAnswer(game.getAnswer((Integer) session.getAttribute(CURRENT)));
                        current++;
                        session.setAttribute(CURRENT, current);
                        speechText += checkReachedEnd(session);
                    }

                } else {
                    speechText += "I do not understand what you are saying, Can you repeat that again Please";
                }

            } else {
                speechText += "I do not understand what you are saying, Can you repeat that again Please";
            }
        }
        return createResponse(card, speechText);
    }

    public String checkReachedEnd(Session session) {

        if ((Integer) session.getAttribute(CURRENT) < MAX_QUESTIONS) {
            game.assignAnswers((Integer) session.getAttribute(CURRENT));
            return game.outputQuestion(game.getQuestion((Integer) session.getAttribute(CURRENT)));

        } else {
            return REPLIES[0] + session.getAttribute(FINALSCORE) + REPLIES[1] + MAX_QUESTIONS + REPLIES[2];

        }
    }

    public SpeechletResponse createResponse(SimpleCard card, String speechText) {
        card.setContent(speechText);
        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
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
        String speechText = "Hello user, I did not understand what you meant! You can accept or deny the quiz you have choosen. You can also ask me to repeat the question, skip the question, end quiz, check score, start again or play a different quiz. Dont forget you can also add quizzes and question using my website";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Help");
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
        current = 0;
        score = 0;

    }
}