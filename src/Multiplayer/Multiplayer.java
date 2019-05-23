package Multiplayer;

import Models.Match;
import SinglePlayer.SinglePlayer;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

import java.io.IOException;

public class Multiplayer extends SinglePlayer {

    private MultiplayerGameFunctionaility multiplayerGameFunctionaility = new MultiplayerGameFunctionaility();
    private Match match = new Match();
    private String CURRENT = "Current";
    private String SCORE1 = "Score1";
    private String SCORE2 = "Score2";
    private String PLAYER1 = "Player1";
    private String PLAYER2 = "Player1";
    private String gameMode = "GAMEMODE";
    private int MAX_QUESTIONS = 0;

    public Multiplayer() throws IOException {
    }

    public SpeechletResponse startMultiplayer(Intent intent, Session session) throws IOException {
        String speechText = "";
        session.setAttribute(gameMode, 1);
        match = multiplayerGameFunctionaility.enterPlayer(session);
        if (match.getPlayerOne() != null && match.getPlayerTwo() != null) {
            startGameSetUp(session);
            speechText += "Opponent found, Get ready, The quiz chosen is:"
                    + multiplayerGameFunctionaility.getQuizname()
                    + multiplayerGameFunctionaility.questionSingleOutput(multiplayerGameFunctionaility.getQuestion(0));


        } else {
            //add a timer and stuff
            match = multiplayerGameFunctionaility.enterPlayer(session);


        }
        return createResponse(speechText);

    }


    public void startGameSetUp(Session session) throws IOException {
        session.setAttribute(CURRENT, 0);
        session.setAttribute(SCORE1, 0);
        session.setAttribute(SCORE2, 0);
        session.setAttribute(PLAYER1, match.getPlayerOne());
        session.setAttribute(PLAYER2, match.getPlayerTwo());
        multiplayerGameFunctionaility.GameSetupMode();
        MAX_QUESTIONS = multiplayerGameFunctionaility.getNumofQuestions();
        multiplayerGameFunctionaility.assignAnswers(0);
    }

}
