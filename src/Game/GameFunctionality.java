package Game;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.*;

import java.io.IOException;
import java.util.Map;

public class GameFunctionality {
    private GamePlayInfo game = new GamePlayInfo();
    private String CURRENT = "Current";
    private String FINALSCORE = "Score";
    private String QuizDiffculty = "difficulty chosen";
    private String CHECK = "Check";
    private String userID;
    private static final String LETTERS = "letters";
    private int current = 0;
    private int score = 0;
    private int MAX_QUESTIONS = 0;
    private boolean gameSesssion = false;
    private static String REPLIES[] = {" You have scored ", " out of " + "<break time=\"0.3s\" /> ", ", Would you like to play this quiz again," + "<break time=\"0.3s\" /> " + " play another random quiz," + "<break time=\"0.3s\" /> " + " play quiz of the day, " + "<break time=\"0.3s\" /> " + "Multiplayer" + "<break time=\"0.3s\" /> " + "or quit?" + "<break time=\"0.5s\" /> "};
    private boolean optionsGiven = false;
    private boolean difficultyChoosen = false;

    public boolean isDifficultyChoosen() {
        return difficultyChoosen;
    }

    public void setDifficultyChoosen(boolean difficultyChoosen) {
        this.difficultyChoosen = difficultyChoosen;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getQuizDiffculty() {
        return QuizDiffculty;
    }

    public void setGameSesssion(boolean gameSesssion) {
        this.gameSesssion = gameSesssion;
    }

    public String getCHECK() {
        return CHECK;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }


    public int getCurrent() {
        return current;
    }


    public int getScore() {
        return score;
    }

    public int getMAX_QUESTIONS() {
        return MAX_QUESTIONS;
    }

    public boolean isGameSesssion() {
        return gameSesssion;
    }

    public boolean isOptionsGiven() {
        return optionsGiven;
    }

    public GameFunctionality() throws IOException {
    }


    public void resetGame(Session session) throws IOException {
        game.setQuestions();
        session.setAttribute(CURRENT, 0);
        session.setAttribute(FINALSCORE, 0);
        score = 0;
        current = 0;
        game.assignAnswers(current);

    }

    public void modeSetUp(Session session) throws IOException {
        game.setQuestions();
        MAX_QUESTIONS = game.getNumofQuestions();
        session.setAttribute(CHECK, "true");
        session.setAttribute(CURRENT, 0);
        session.setAttribute(FINALSCORE, 0);
        game.assignAnswers(0);

    }


    public SpeechletResponse startRandomGame(Intent intent, Session session) throws IOException {
        String speechText = " ";
        String choice = intent.getSlot("choice").getResolutions().getResolutionsPerAuthority().get(0).getValueWrappers().get(0).getValue().getName();
        game = new GamePlayInfo();
        String message = null;
        if (choice.equalsIgnoreCase("Hard")) {
            game.HardQuiz();
            message = " Someone is feeling brave! ";
        } else if (choice.equalsIgnoreCase("Medium")) {
            game.MediumQuiz();
            message = " Whooooop!  ";
        } else if (choice.equalsIgnoreCase("Easy")) {
            game.EasyQuiz();
            message = " Wise Choice! ";

        }
        gameSesssion = true;
        difficultyChoosen = true;
        modeSetUp(session);
        current = (int) session.getAttribute(CURRENT);
        speechText += message + game.getWelcomeQuizMessage() + "<break time=\"0.5s\" /> " + " before we begin," + "<break time=\"0.5s\" /> " + " there are  " + MAX_QUESTIONS + "  questions in total. " + "<break time=\"0.6s\" /> " + game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));
        return createResponse(speechText);

    }

    //quiz of the day
    public SpeechletResponse playQuizOD(Intent intent, Session session) throws IOException {
        String speechText = "Hmmmm Ok.... You have chosen to play quiz of the Day ";
        game.QuizOfTheDay();
        modeSetUp(session);
        gameSesssion = true;
        current = (int) session.getAttribute(CURRENT);
        speechText += game.getWelcomeQuizMessage() + "<break time=\"0.5s\" /> " + " before we begin," + "<break time=\"0.5s\" /> " + " there are  " + MAX_QUESTIONS + "  questions in total. " + "<break time=\"0.6s\" /> " + game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        card.setContent(speechText);
        return createResponse(speechText);
    }

    public SpeechletResponse getToken(Session session) {
        String speechText = " ";
        userID = session.getUser().getUserId();
        String token = game.getToken(userID);
        speechText += token;
        return createResponse(speechText);

    }

    public SpeechletResponse playNextQuiz(Intent intent, Session session) throws IOException {
        String speechText = " ";
        game.chooseQuiz();
        resetGame(session);
        gameSesssion = true;
        speechText += " You have chosen to play another random Quiz,  " + "<break time=\"0.3s\" /> " + game.getWelcomeQuizMessage() + "<break time=\"0.5s\" /> " + game.questionSingleOutput(game.getQuestion(current));
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        card.setContent(speechText);

        return createResponse(speechText);

    }

    public SpeechletResponse getMyScore(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = REPLIES[0] + session.getAttribute(FINALSCORE) + " so far " + REPLIES[1] + session.getAttribute(CURRENT);
        return createResponse(speechText);
    }

    public SpeechletResponse skipQuestion(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText;
        speechText = " <p> You have skipped this question. </p> " + "<break time=\"0.8s\" /> " + game.outputIncorrectAnswer(game.getAnswer((Integer) session.getAttribute(CURRENT)));
        current++;
        session.setAttribute(CURRENT, current);
        current = (int) session.getAttribute(CURRENT);
        speechText += checkReachedEnd(session);


        card.setContent(speechText);
        return createResponse(speechText);
    }

    public SpeechletResponse repeatQuestion(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText;
        if (session.getAttribute(CHECK).equals("false")) {
            speechText = "<p> Welcome to Abdul's Quiz Trivia game. </p>" +
                    "<break time=\"0.3s\" /> " + "<p> Ask me at any time to generate a code for registration </p>" + "<break time=\"0.6s\" /> " +
                    "To answer choose a letter. Would you like to play a random quiz or play quiz of the day? " + "<break time=\"0.3s\" /> ";
        } else {
            speechText = game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));
            current = (int) session.getAttribute(CURRENT);

        }
        card.setContent(speechText);

        return createResponse(speechText);
    }

    public SpeechletResponse endQuiz(Intent intent, Session session) {
        int finalScore = (int) session.getAttribute(FINALSCORE);
        game.sendScore(game.getQuizId(), userID, finalScore);
        session.setAttribute(CURRENT, MAX_QUESTIONS);
        current = MAX_QUESTIONS;
        gameSesssion = false;
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = " You have ended the quiz! " + "<break time=\"0.3s\" /> " + REPLIES[0] + session.getAttribute(FINALSCORE) + REPLIES[1] + MAX_QUESTIONS + REPLIES[2];
        card.setContent(speechText);
        return createResponse(speechText);
    }

    public SpeechletResponse startAgain(Intent intent, Session session) throws IOException {
        resetGame(session);
        gameSesssion = true;
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = "You have started again, " + "<break time=\"0.7s\" /> " + game.questionSingleOutput(game.getQuestion((Integer) session.getAttribute(CURRENT)));
        card.setContent(speechText);

        return createResponse(speechText);
    }

    public SpeechletResponse startQuizRandom(Intent intent, Session session) {
        String speechText = "";
//        if (current == 0) {
        optionsGiven = true;
        speechText += " You have chosen a random quiz, now choose a difficulty level, Easy, Medium or hard";
        session.setAttribute(QuizDiffculty, true);
//        } else {
//            getHelpResponse();
//        }

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
    public SpeechletResponse getWelcomeResponse() {
//        // setting up sample questions
        String speechText = "<p> Welcome to Abdul's Quiz Trivia game. </p>" +
                "<break time=\"0.3s\" /> " + "<p> Ask me at any time to generate a code for registration </p>" + "<break time=\"0.3s\" /> " +
                "To answer a question choose a letter." + "<break time=\"0.3s\" /> " + "Would you like to play a random quiz , Multi-player or play quiz of the day? " + "<break time=\"0.3s\" /> ";
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
    public SpeechletResponse gameMode(final Intent intent, final Session session) {
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
                    String user_input = intent.getSlot(LETTERS).getResolutions().getResolutionsPerAuthority().get(0).getValueWrappers().get(0).getValue().getName();
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

    private String checkReachedEnd(Session session) {

        if ((Integer) session.getAttribute(CURRENT) < MAX_QUESTIONS) {
            game.assignAnswers((Integer) session.getAttribute(CURRENT));
            return game.outputQuestion(game.getQuestion((Integer) session.getAttribute(CURRENT)));

        } else {
            int finalScore = (int) session.getAttribute(FINALSCORE);
            current = MAX_QUESTIONS;
            game.sendScore(game.getQuizId(), userID, finalScore);
            gameSesssion = false;
            return "<break time=\"0.8s\" /> " + "You have reached the end of the quiz. " + "<break time=\"0.8s\" /> " + REPLIES[0] + session.getAttribute(FINALSCORE) + REPLIES[1] + MAX_QUESTIONS + REPLIES[2];

        }
    }


    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    public SpeechletResponse getHelpResponse() {
        String speechText;
        speechText = "<p>Can you repeat that again please ? </p>";
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Help");
        card.setContent(speechText);
        return createResponse(speechText);
    }

    protected SpeechletResponse createResponse(String speechText) {
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

}

