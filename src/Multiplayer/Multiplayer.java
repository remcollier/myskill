package Multiplayer;

import Models.Match;
import Models.Player;
import SinglePlayer.SinglePlayer;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.SimpleCard;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.Map;

public class Multiplayer extends SinglePlayer {

    private MultiplayerGameFunctionaility multiplayerGameFunctionaility = new MultiplayerGameFunctionaility();
    private Match match = new Match();
    private String PLAYER1 = "Player1";
    private String PLAYER2 = "Player2";
    private String gameMode = "GAMEMODE";
    private int MAX_QUESTIONS = 0;
    private static final String LETTERS = "letters";
    private int current;
    private int score;
    static ObjectMapper mapper = new ObjectMapper();
    private String CHECK = "Check";

    public Multiplayer() throws IOException {

    }

    public SpeechletResponse startMultiplayer(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        final String[] speechText = {""};
        int count = 0;
        final Session session = requestEnvelope.getSession();
        session.setAttribute(gameMode, 1);
        try {
            match = multiplayerGameFunctionaility.enterPlayer(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (match.getPlayerOne() == null && match.getPlayerTwo() == null && count != 15) {

            try {
                match = multiplayerGameFunctionaility.enterPlayer(session);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (match.getPlayerOne() != null && match.getPlayerTwo() != null) {
            try {
                startGameSetUp(session);
            } catch (IOException e) {
                e.printStackTrace();
            }
            speechText[0] += "Opponent found, Get ready, The quiz chosen is:"
                    + multiplayerGameFunctionaility.getQuizname()
                    + multiplayerGameFunctionaility.questionSingleOutput(multiplayerGameFunctionaility.getQuestion(0));
        } else {
            speechText[0] += "No one was found, please try again later";
        }
        return createResponse(speechText[0]);
    }


    public void startGameSetUp(Session session) throws IOException {
        session.setAttribute(PLAYER1, new Player(match.getPlayerOne(), 0, 0));
        session.setAttribute(PLAYER2, new Player(match.getPlayerTwo(), 0, 0));
        multiplayerGameFunctionaility.GameSetupMode();
        match.setQuizId(multiplayerGameFunctionaility.getQuizId());
        MAX_QUESTIONS = multiplayerGameFunctionaility.getNumofQuestions();
        multiplayerGameFunctionaility.assignAnswers(0);
    }


    public SpeechletResponse playerResponse(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText = " ";
        if (multiplayerGameFunctionaility.getQuestions().isEmpty()) {
            return getWelcomeResponse();
        } else {

            String currentPlayer = multiplayerGameFunctionaility.getCurrentPlayer(session.getUser().getUserId(), match.getPlayerOne());
            speechText += getResponse(session, intent, currentPlayer);
        }
        return createResponse(speechText);

    }

    public String getResponse(Session session, Intent intent, String cPlayer) {
        Object q = session.getAttribute(cPlayer);
        Player player = mapper.convertValue(q, Player.class);
        String speechText = "";
        current = player.getCount();
        System.out.println(current);
        score = player.getScore();
        Map<String, Slot> slots = intent.getSlots();
        if (intent.getSlot(LETTERS).getName().equalsIgnoreCase(LETTERS)) {
            Slot s = slots.get(LETTERS);
            if (!s.getValue().isEmpty() && current < MAX_QUESTIONS) {
                String user_input = intent.getSlot(LETTERS).getResolutions().getResolutionsPerAuthority().get(0).getValueWrappers().get(0).getValue().getName();
                if (multiplayerGameFunctionaility.checkAnswer(user_input)) {
                    speechText += multiplayerGameFunctionaility.getCongrats();
                    score++;
                    current++;
                    player.setCount(current);
                    player.setScore(score);
                    session.setAttribute(cPlayer, player);
                    speechText += checkReachedEnd(session, player, cPlayer);
                } else {
                    speechText += multiplayerGameFunctionaility.outputIncorrectAnswer(multiplayerGameFunctionaility.getAnswer(current));
                    current++;
                    player.setCount(current);
                    session.setAttribute(cPlayer, player);
                    speechText += checkReachedEnd(session, player, cPlayer);
                }

            } else {
                speechText += "I do not understand what you are saying, Can you repeat that again Please?";
            }

        } else {
            speechText += "I do not understand what you are saying, Can you repeat that again Please?";
        }


        return speechText;
    }

    //fix to send requests to db "scores"
    public String checkReachedEnd(Session session, Player player, String cPlayer) {
        int current = player.getCount();
        String opponent;
        if (player.getCount() < MAX_QUESTIONS) {
            multiplayerGameFunctionaility.assignAnswers(current);
            return multiplayerGameFunctionaility.outputQuestion(multiplayerGameFunctionaility.getQuestion(current));

        } else {
            if (cPlayer.equalsIgnoreCase(PLAYER1)) {
                opponent = PLAYER2;
                Object q = session.getAttribute(opponent);
                Player player2 = mapper.convertValue(q, Player.class);
                if (player2.getCount() < MAX_QUESTIONS) {
                    match.setPlayerOneScore(player.getScore());
                    multiplayerGameFunctionaility.sendScore(match);

                    return "<say-as interpret-as=\"interjection\">righto </say-as> <break time=\"0.8s\" /> "
                            + "You have reached the end of the quiz. " + "<break time=\"0.8s\" /> you have scored "
                            + player.getScore() + " out of " + MAX_QUESTIONS + " <break time=\"0.8s\" /> And your opponent so far has scored " + player2.getScore() + " out of " + player2.getCount();
                } else {
                    match.setCompleted("done");
                    match.setPlayerOneScore(player.getScore());
                    multiplayerGameFunctionaility.sendScore(match);
                    return "<say-as interpret-as=\"interjection\">righto </say-as> <break time=\"0.8s\" /> "
                            + "You have reached the end of the quiz. " + "<break time=\"0.8s\" /> you have scored "
                            + player.getScore() + " out of " + MAX_QUESTIONS + " <break time=\"0.8s\" /> And your opponent scored " + player2.getScore() + " out of " + MAX_QUESTIONS;

                }
            } else {
                opponent = PLAYER1;
                Object q = session.getAttribute(opponent);
                Player player2 = mapper.convertValue(q, Player.class);
                if (player2.getCount() < MAX_QUESTIONS) {
                    match.setPlayerTwoScore(player.getScore());
                    multiplayerGameFunctionaility.sendScore(match);
                    return " <say-as interpret-as=\"interjection\">righto </say-as> <break time=\"0.8s\" /> "
                            + "You have reached the end of the quiz. " + "<break time=\"0.8s\" /> you have scored "
                            + player.getScore() + " out of " + MAX_QUESTIONS + " <break time=\"0.8s\" /> And your opponent so far has scored " + player2.getScore() + " out of " + player2.getCount();
                } else {
                    match.setPlayerTwoScore(player.getScore());
                    match.setCompleted("done");
                    multiplayerGameFunctionaility.sendScore(match);
                    return " <say-as interpret-as=\"interjection\">righto </say-as> <break time=\"0.8s\" /> "
                            + "You have reached the end of the quiz. " + "<break time=\"0.8s\" /> you have scored "
                            + player.getScore() + " out of " + MAX_QUESTIONS + " <break time=\"0.8s\" /> And your opponent scored " + player2.getScore() + " out of " + MAX_QUESTIONS;

                }


            }


        }
    }

    public SpeechletResponse skipQuestion(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText;
        speechText = " <p> You have skipped this question. </p> " + "<break time=\"0.8s\" /> ";
        String currentPlayer = multiplayerGameFunctionaility.getCurrentPlayer(session.getUser().getUserId(), match.getPlayerOne());
        Object q = session.getAttribute(currentPlayer);
        Player player = mapper.convertValue(q, Player.class);
        int current = player.getCount();
        current++;
        player.setCount(current++);
        session.setAttribute(currentPlayer, player);
        speechText += checkReachedEnd(session, player, currentPlayer);
        card.setContent(speechText);
        return createResponse(speechText);
    }

    public SpeechletResponse endQuiz(Intent intent, Session session) {
        SimpleCard card = new SimpleCard();
        card.setTitle(intent.getName());
        String speechText;
        speechText = " <p> You have forfeited the game. </p> " + "<break time=\"0.8s\" />  Your opponent automatically wins";
        String currentPlayer = multiplayerGameFunctionaility.getCurrentPlayer(session.getUser().getUserId(), match.getPlayerOne());
        Object q = session.getAttribute(currentPlayer);
        Player player = mapper.convertValue(q, Player.class);
        player.setCount(MAX_QUESTIONS);
        player.setScore(0);
        match.setCompleted(currentPlayer);
        session.setAttribute(currentPlayer, player);
        speechText += checkReachedEnd(session, player, currentPlayer);
        card.setContent(speechText);
        return createResponse(speechText);
    }


}
