package Game;

import REST.GameApi;
import com.google.gson.Gson;

import java.io.IOException;
//
//
public class TestClass {
    public static void main(String[] args) throws IOException {
        GameApi s = new GameApi();
//        int current = 2;
        GamePlayInfo t = new GamePlayInfo();

       System.out.println(t.getCongrats());
//        t.assignAnswers(current);
//        System.out.println( t.());
//        t.assignAnswers(1);
//        Token token = new Token();
////        Score score = new Score();
//////        score.setScore(2);
//////        score.setAccountId("abdul");
//////        score.setQuizId(Long.valueOf(1));
//        Gson gson = new Gson();
//////        s.sendScore(scoreRequest);
//        token.setAccountId("pekun");
//        String tokenRequest = gson.toJson(token);
//        s.sendToken(tokenRequest);
//        score.setScore(2);
//        score.setAccountId("pekun");
//        score.setQuizId(Long.valueOf(1));
//        String scoreRequest = gson.toJson(score);
//
//        s.sendScore(scoreRequest);
    }
}
