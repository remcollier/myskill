package test;

import Game.GamePlayInfo;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.*;

import java.io.IOException;
import java.util.Map;

public class SayHelloSpeechlet implements Speechlet {
    //    private GamePlayInfo game = null;
    private GamePlayInfo game = null;

    //    private static final String DayQuiz = "DayQuiz";
    private String CURRENT = "Current";
    private String FINALSCORE = "Score";
    private String QuizDiffculty = "difficulty chosen";
    private String SelectQuiz = "SelectQuizz";
    private String CHECK = "Check";
    private String userID;
    private static final String LETTERS = "letters";
    private int current = 0;
    private int score = 0;
    private int MAX_QUESTIONS = 0;
    private boolean gameSesssion= false;
    private static String REPLIES[] = {" You have scored ", " out of " + "<break time=\"0.3s\" /> ", ", Would you like to play this quiz again," + "<break time=\"0.3s\" /> " + " play another random quiz," + "<break time=\"0.3s\" /> " + " play quiz of the day, " + "<break time=\"0.3s\" /> "  + "Multiplayer"+ "<break time=\"0.3s\" /> "  + "or quit?" + "<break time=\"0.5s\" /> "};

    public SayHelloSpeechlet() throws IOException {

    }

    public void resetGame(Session session) throws IOException {
        game.setQuestions();
        session.setAttribute(CURRENT, 0);
        session.setAttribute(FINALSCORE, 0);
        score = 0;
        current = 0;
        game.assignAnswers(current);

    }

    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        System.out.println("onLaunch requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());
        session.setAttribute(CHECK, "false");
        session.setAttribute(QuizDiffculty, false);

        return getWelcomeResponse();
    }


    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        System.out.println("onIntent requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        System.out.println("intentName : " + intentName);

//add functionality for stupid shit
        if ("SelectQuiz".equals(intentName)) {
            try {
                return startQuizGame(intent, session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("Answer".equals(intentName)) {
            return gameMode(intent, session);
        } else if ("StartAgain".equals(intentName) & gameSesssion) {
            try {
                return startAgain(intent, session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("End".equals(intentName) & current != MAX_QUESTIONS & gameSesssion) {
            return endQuiz(intent, session);
        } else if ("Token".equals(intentName)) {
            return getToken(intent, session);
        } else if ("Repeat".equals(intentName) ) {
            return repeatQuestion(intent, session);
        } else if ("Skip".equals(intentName) & gameSesssion) {
            return skipQuestion(intent, session);
        } else if ("Myscore".equals(intentName) & gameSesssion) {
            return getMyScore(intent, session);
        } else if ("Multiplayer".equals(intentName)) {
            return startMultiplayer(intent, session);
//        } else if ("Difficulty".equals(intentName) & session.getAttribute(QuizDiffculty).equals(true)) {
        } else if ("Difficulty".equals(intentName) & gameSesssion) {

            try {
                return startRandomGame(intent, session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("PlayNext".equals(intentName) & current == MAX_QUESTIONS & gameSesssion) {
            try {
                return playNextQuiz(intent, session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("PlayQuizOD".equals(intentName) & current == MAX_QUESTIONS & gameSesssion) {
            return playQuizOD(intent, session);
        } else {
            return getHelpResponse();
        }

        return getHelpResponse();
    }

    private SpeechletResponse startMultiplayer(Intent intent, Session session) {

        String speechText = "Searching for someone to play with online ";

        return createResponse(speechText);

    }

    private SpeechletResponse startRandomGame(Intent intent, Session session) throws IOException {
        String speechText = " ";
        game = new GamePlayInfo();
        game.setQuestions();
        //setting up sessions
        MAX_QUESTIONS = game.getNumofQuestions();
        session.setAttribute(CHECK, "true");
        session.setAttribute(CURRENT, 0);
        session.setAttribute(FINALSCORE, 0);
        game.assignAnswers(0);
        speechText += " Wise Choice! " + game.getWelcomeQuizMessage()+ "<break time=\"0.5s\" /> " + " before we begin," + "<break time=\"0.5s\" /> " + " there are  " + MAX_QUESTIONS + "  questions in total. " + "<break time=\"0.6s\" /> " + game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));
        return createResponse(speechText);

    }

    private SpeechletResponse playQuizOD(Intent intent, Session session) {
        String speechText = " worked";
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        card.setContent(speechText);
        return createResponse(speechText);
    }

    private SpeechletResponse getToken(Intent intent, Session session) {
        String speechText = " ";
        userID = session.getUser().getUserId();
        String token = game.getToken(userID);
        speechText += token;
        return createResponse(speechText);

    }

    private SpeechletResponse playNextQuiz(Intent intent, Session session) throws IOException {
        String speechText = " ";
        game.chooseQuiz();
        resetGame(session);
//      MAX_QUESTIONS = game.getNumofQuestions();
        speechText += " You have chosen to play another random Quiz,  " + "<break time=\"0.3s\" /> " + game.getWelcomeQuizMessage() + "<break time=\"0.5s\" /> " + game.questionSingleOutput(game.getQuestion(current));
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        card.setContent(speechText);

        return createResponse(speechText);

    }

    private SpeechletResponse getMyScore(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = REPLIES[0] + session.getAttribute(FINALSCORE) + " so far " + REPLIES[1] + session.getAttribute(CURRENT);
        return createResponse(speechText);
    }

    private SpeechletResponse skipQuestion(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText;
        speechText = " <p> You have skipped this question. </p> " + "<break time=\"0.8s\" /> " + game.outputIncorrectAnswer(game.getAnswer((Integer) session.getAttribute(CURRENT)));
        current++;
        session.setAttribute(CURRENT, current);
        speechText += checkReachedEnd(session);


        card.setContent(speechText);
        return createResponse(speechText);
    }

    private SpeechletResponse repeatQuestion(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText;
        if (session.getAttribute(CHECK).equals("false")) {
            speechText = "<p> Welcome to Abdul's Quiz Trivia game. </p>" +
                    "<break time=\"0.3s\" /> " + "<p> Ask me at any time to generate a code for registration </p>" + "<break time=\"0.6s\" /> " +
                    "To answer choose a letter. Would you like to play a random quiz or play quiz of the day? " + "<break time=\"0.3s\" /> ";
        } else {
            speechText = game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));


        }
        card.setContent(speechText);

        return createResponse(speechText);
    }

    //need to add intent where player chooses a different quiz
    private SpeechletResponse endQuiz(Intent intent, Session session) {
        int finalScore = (int) session.getAttribute(FINALSCORE);
        game.sendScore(game.getQuizId(), userID, finalScore);
        session.setAttribute(CURRENT, MAX_QUESTIONS);
        current = MAX_QUESTIONS;
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = " You have ended the quiz! " + "<break time=\"0.3s\" /> " + REPLIES[0] + session.getAttribute(FINALSCORE) + REPLIES[1] + MAX_QUESTIONS + REPLIES[2];
        card.setContent(speechText);
        return createResponse(speechText);
    }

    private SpeechletResponse startAgain(Intent intent, Session session) throws IOException {
        resetGame(session);
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = "You have started again, " + "<break time=\"0.7s\" /> " + game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));
        card.setContent(speechText);

        return createResponse(speechText);
    }

    private SpeechletResponse startQuizGame(Intent intent, Session session) throws IOException {
        userID = session.getUser().getUserId();
        String s = intent.getSlot(SelectQuiz).getResolutions().getResolutionsPerAuthority().get(0).getValueWrappers().get(0).getValue().getName();
        String speechText = "";
        if (current == 0) {
            if (s.equalsIgnoreCase("Random")) {
                gameSesssion = true;

//                game = new GamePlayInfo();
//                game.setQuestions();
//                //setting up sessions
//                MAX_QUESTIONS = game.getNumofQuestions();
//                session.setAttribute(CHECK, "true");
//                session.setAttribute(CURRENT, 0);
//                session.setAttribute(FINALSCORE, 0);
//                game.assignAnswers(0);
//                speechText += " Wise Choice! " + "<break time=\"0.3s\" /> " + " before we begin," + "<break time=\"0.5s\" /> " + " there are  " + MAX_QUESTIONS + "  questions in total. " + "<break time=\"0.6s\" /> " + game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));
                speechText += " You have chosen a random quiz, now choose a difficulty level, Easy, Medium or hard";
                session.setAttribute(QuizDiffculty, true);
            } else if (s.equalsIgnoreCase("DayQuiz")) {
                session.setAttribute(QuizDiffculty, false);

                speechText += " Worked well done ";

            } else {

                getWelcomeResponse();
            }
        } else {
            getHelpResponse();
        }

        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        card.setContent(speechText);

        return createResponse(speechText);
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent try get it to generate random quizzes rather than the user choosing next quiz
     */
    private SpeechletResponse getWelcomeResponse() {
//        // setting up sample questions
        String speechText = "<p> Welcome to Abdul's Quiz Trivia game. </p>" +
                "<break time=\"0.3s\" /> " + "<p> Ask me at any time to generate a code for registration </p>" + "<break time=\"0.3s\" /> " +
                "To answer a question choose a letter."+ "<break time=\"0.3s\" /> " + "Would you like to play a random quiz , Multi-player or play quiz of the day? " + "<break time=\"0.3s\" /> ";
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Welcome");
        card.setContent(speechText);
        return createResponse(speechText);

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
        if (game.getQuestions().isEmpty()) {
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
                    speechText += "I do not understand what you are saying, Can you repeat that again Please?";
                }

            } else {
                speechText += "I do not understand what you are saying, Can you repeat that again Please?";
            }
        }
        card.setContent(speechText);
        return createResponse(speechText);
    }

    public String checkReachedEnd(Session session) {

        if ((Integer) session.getAttribute(CURRENT) < MAX_QUESTIONS) {
            game.assignAnswers((Integer) session.getAttribute(CURRENT));
            return game.outputQuestion(game.getQuestion((Integer) session.getAttribute(CURRENT)));

        } else {
            int finalScore = (int) session.getAttribute(FINALSCORE);
            current = MAX_QUESTIONS;
            game.sendScore(game.getQuizId(), userID, finalScore);
            return "<break time=\"0.8s\" /> " + "You have reached the end of the quiz. " + "<break time=\"0.8s\" /> " + REPLIES[0] + session.getAttribute(FINALSCORE) + REPLIES[1] + MAX_QUESTIONS + REPLIES[2];

        }
    }

    public SpeechletResponse createResponse(String speechText) {
        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);
        //changing voice
//        String name = "Emma";
//        return newAskResponse("<speak>" + "<voice name='" + name + "'>" + speechText + "</voice>" + "</speak>", true, speechText, false);
        return newAskResponse("<speak>" + speechText + "</speak>", true, "<speak>" + speechText + "</speak>", true);
    }


    /**
     * Wrapper for creating the Ask response from the input strings.
     *
     * @param stringOutput   the output to be spoken
     * @param isOutputSsml   whether the output text is of type SSML
     * @param repromptText   the reprompt for if the user doesn't reply or is misunderstood.
     * @param isRepromptSsml whether the reprompt text is of type SSML
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
                                             String repromptText, boolean isRepromptSsml) {
        OutputSpeech outputSpeech, repromptOutputSpeech;
        if (isOutputSsml) {
            outputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
        } else {
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
        }

        if (isRepromptSsml) {
            repromptOutputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
        } else {
            repromptOutputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
        }
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }


    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText;
//        speechText = "<p>Hello user, I did not understand what you meant!</p>  <p>You can accept or deny the quiz you have been given. You can also ask me to repeat the question, skip the question, end quiz, check score, start again or play a different quiz. Dont forget you can also add quizzes and question using my website</p>";
        speechText = "<p>Can you repeat that again please ? </p>";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Help");
        card.setContent(speechText);
        return createResponse(speechText);
    }

    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        System.out.println("onSessionStarted requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());

        session.setAttribute(CHECK, "false");

    }

    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        System.out.println("onSessionEnded requestId={}, sessionId={} " + request.getRequestId()
                + " - " + session.getSessionId());
        current = 0;
        score = 0;

    }
}

