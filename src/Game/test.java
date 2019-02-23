package Game;

import REST.GameApi;
import com.google.gson.Gson;


public class test {
    public static void main(String[] args) {
        GameApi s = new GameApi();
//        int current = 0;
//        GamePlayInfo t = new GamePlayInfo();
//        t.setQuestions();
//        t.getWelcomeQuizMessage();
//        t.assignAnswers(current);
//        t.outputQuestion(t.getQuestion(current));
//        System.out.println(t.checkAnswer("B"));
//        t.assignAnswers(1);
        Token token = new Token();
        Score score = new Score();
        score.setScore(1);
        score.setAccountId("Abdul");
        score.setQuizId(Long.valueOf(1));
        Gson gson = new Gson();
        String scoreRequest = gson.toJson(score);
        s.sendScore(scoreRequest);
        token.setAccountId("abdul");
        String tokenRequest = gson.toJson(token);
        s.sendToken(tokenRequest);

    }
}
