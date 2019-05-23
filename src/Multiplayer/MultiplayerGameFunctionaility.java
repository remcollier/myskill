package Multiplayer;

import Models.Match;
import Models.Question;
import Models.Quiz;
import Models.Token;
import REST.MultiplayerAPI;
import REST.QuestionsApi;
import REST.QuizApi;
import com.amazon.speech.speechlet.Session;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MultiplayerGameFunctionaility {

    private MultiplayerAPI gameApi = new MultiplayerAPI();
    private Quiz gameQuiz = new Quiz();
    private Long quizId;
    private List<Question> questions;
    private QuestionsApi questionsApi = new QuestionsApi();
    private String letter = null;
    private Random r = new Random();
    private HashMap<String, String> answers = new HashMap<String, String>();
    private QuizApi quiz = new QuizApi();


    public void setGameQuiz(Quiz gameQuiz) {
        this.gameQuiz = gameQuiz;
    }

    public String getQuizname() {

        return gameQuiz.getTitle();
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public void setQuestions() throws IOException {
        questions = questionsApi.getQuizQuestions(Long.valueOf(quizId));
    }

    public String getQuestion(int i) {
        return questions.get(i).getQuestion();
    }

    public int getNumofQuestions() {
        return questions.size();
    }

    public void assignAnswers(int i) {

        String a[] = {"A", "B", "C", "D"};
        Random ren = new Random();  // Random number generator
        for (int n = 0; n < a.length; n++) {
            int randomPosition = ren.nextInt(a.length);
            String temp = a[n];
            a[n] = a[randomPosition];
            a[randomPosition] = temp;
        }
        this.letter = a[0];
        answers.put(a[0], questions.get(i).getRight());
        answers.put(a[1], questions.get(i).getWrong1());
        answers.put(a[2], questions.get(i).getWrong2());
        answers.put(a[3], questions.get(i).getWrong3());

    }


    public String outputQuestion(String question) {
        String string = " ";
        int low = 1;
        int high = 4;
        int random = r.nextInt(high - low) + low;
        switch (random) {
            case 1:
                string = " <p>The next question is</p> " + question + ".... ";
                break;
            case 2:
                string = " <p>The following question is</p> " + question + "...... ";
                break;
            case 3:
                string = "<p> On to the next question </p> " + question + "..... ";
                break;
            case 4:
                string = question;
                break;
        }
        string += " <p> Is it ? </p>" + "<break time=\"0.5s\" /> " + " <p> <say-as interpret-as=\"characters\">A</say-as> </p>" + answers.get("A") + "<p> <say-as interpret-as=\"characters\">B</say-as> </p>" + answers.get("B") + "<p> <say-as interpret-as=\"characters\">C</say-as> </p>" + answers.get("C") + " <p> or <say-as interpret-as=\"characters\">D</say-as> </p>" + answers.get("D");
        return string;
    }


    public String questionSingleOutput(String question) {
        String string = " ";
        string += question + " <p> Is it ? </p>" + "<break time=\"0.5s\" /> " + " <p> <say-as interpret-as=\"characters\">A</say-as> </p>" + answers.get("A") + "<p> <say-as interpret-as=\"characters\">B</say-as> </p>" + answers.get("B") + "<p> <say-as interpret-as=\"characters\">C</say-as> </p>" + answers.get("C") + " <p> or <say-as interpret-as=\"characters\">D</say-as> </p>" + answers.get("D");
        return string;
    }


    public boolean checkAnswer(String letter) {
        boolean check = false;
        if (this.letter.equalsIgnoreCase(String.valueOf(letter))) {
            check = true;
        }
        return check;
    }

    public Match enterPlayer(Session session) throws IOException {
        String userID;
        Gson gson = new Gson();
        Token token = new Token();
        userID = session.getUser().getUserId();
        token.setAccountId(userID);
        String tokenRequest = gson.toJson(token);
        String tokenGiven = gameApi.sendToken(tokenRequest);
        token.setTokenId(tokenGiven);
        String tokenRequest2 = gson.toJson(token);
        Match match = gameApi.getPlayers(tokenRequest2);
        return match;

    }

    public void GameSetupMode() throws IOException {
        setGameQuiz(quiz.getQTD());
        setQuizId(quiz.getQTD().getId());
        setQuestions();

    }

}



