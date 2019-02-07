package Game;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        int current = 0;
        GamePlayInfo t = new GamePlayInfo();
        t.setQuestions();
        t.getWelcomeQuizMessage();
        t.assignAnswers(current);
        t.outputQuestion(t.getQuestion(current));
//        System.out.println(t.checkAnswer("B"));
//        t.assignAnswers(1);


    }
}
