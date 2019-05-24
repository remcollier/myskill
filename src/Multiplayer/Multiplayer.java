package Multiplayer;

import Models.Match;
import Models.Player;
import SinglePlayer.SinglePlayer;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

import java.io.IOException;

public class Multiplayer extends SinglePlayer {

    private MultiplayerGameFunctionaility multiplayerGameFunctionaility = new MultiplayerGameFunctionaility();
    private Match match = new Match();
    private String PLAYER1 = "Player1";
    private String PLAYER2 = "Player2";
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
        Player player1 = new Player(match.getPlayerOne());
        Player player2 = new Player(match.getPlayerTwo());
        session.setAttribute(PLAYER1, player1);
        session.setAttribute(PLAYER2, player2);
        multiplayerGameFunctionaility.GameSetupMode();
        MAX_QUESTIONS = multiplayerGameFunctionaility.getNumofQuestions();
        multiplayerGameFunctionaility.assignAnswers(0);
    }


    public SpeechletResponse PlayerResponse(Intent intent, Session session) throws IOException {
        String speechText = "";
        return createResponse(speechText);

    }

}
