package test;


import com.amazonaws.services.dynamodbv2.xspec.B;

import java.util.*;

public class GamePlayInfo {

    private long quiz_id;


    private String quizname;
    private int num;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int score;
    private List<Question> question = new ArrayList<>();
    //    private Question question;
    private HashMap<String, String> answers = new HashMap<String, String>();
    private String letter = null;

    public GamePlayInfo(long quiz_id, int num) {
        this.quiz_id = quiz_id;
        this.num = num;
    }

    public String getQuizname() {
        return quizname;
    }


    public void setQuestions() {
        Question h = new Question(1, "Who are you1", "abdul", "peter", "mike", "john");
        Question i = new Question(2, "do you love me2", "yes", "maybe", "dont know", "not sure");
        Question u = new Question(3, "Who are you3", "abdul", "peter", "mike", "john");
        Question r = new Question(4, "do you love me4", "yes", "maybe", "dont know", "not sure");
        question.add(h);
        question.add(i);
        question.add(u);
        question.add(r);

    }

    //use this to get  questions from database
    public List<Question> getRandomQuestions() {
//get sql query to get the quiz
        return question;
    }

    public String getQuestion(int current) {
        return question.get(current).getQuestion();
    }

    public String getWelcomeQuizMessage() {
        String s = "The quiz chosen is called  " + getQuizname() + "..... say accept to to continue with this quiz or deny to be given another quiz!  ";
        return s;
    }

    public String getAnswer(int current) {
        return question.get(current).getAnswer();


    }

    public void assignAnswers(int current) {

        String array[] = {"A", "B", "C", "D"};
        Random rgen = new Random();  // Random number generator

        for (int i = 0; i < array.length; i++) {
            int randomPosition = rgen.nextInt(array.length);
            String temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }
        this.letter = array[0];
        answers.put(array[0], question.get(current).getAnswer());
        answers.put(array[1], question.get(current).getWronganswer1());
        answers.put(array[2], question.get(current).getWronganswer2());
        answers.put(array[3], question.get(current).getWronganswer3());

    }

    //needs to check for A. a.*********
    public boolean checkAnswer(String letter) {
        boolean check = false;
        if (this.letter.equalsIgnoreCase(letter)) {
            check = true;
        }

        return check;
    }


    public String output_question(String question, int current) {
        String s = null;
//change this so it is random
        int random = 1;
        switch (random) {
            case 1:
                s = "The next question is " + question + ".... ";
                break;
            case 2:
                s = "The following question is +" + question + "...... ";
                break;
            case 3:
                s = "On to the next question  " + question + "..... ";
                break;
            case 4:
                s = question;
                break;
        }

//        s += "Is it ?...." + "  ......A.   " + getKey(answers, this.question.get(current).getAnswer()) + " ........B.    " + getKey(answers, answers.get(this.question.get(current).getWronganswer1())) + "  ........C.    " + getKey(answers, answers.get(this.question.get(current).getWronganswer2())) + " or ..........D.    " + getKey(answers, answers.get(this.question.get(current).getWronganswer1()));
        s += "Is it ?...." + "  ......A.   " + answers.get("A") + " ........B.    " + answers.get("B") + "  ........C.    " + answers.get("C") + " or ..........D.    " + answers.get("D");

        return s;
    }


    public String output_wrong(String answer) {
        String s = null;
//change this so it is random
        int random = 1;
        switch (random) {
            case 1:
                s = "..... Unlucky!!!  The answer was..... " + letter + ".....   " + answer + ". ";
                break;
            case 2:
                s = "..... Hard luck ! The answer was..... " + letter + "....   " + answer + " . ";
                break;
            case 3:
                s = "..... Better luck next time!..... " + letter + " ....  " + answer + " was the answer.  ";
                break;
        }

        return s;
    }
//
//    public static void main(String[] args) {
//        GamePlayInfo t = new GamePlayInfo(1, 3);
//        t.setQuestions();
//        t.assignAnswers(0);
//        System.out.println(t.output_question(t.getQuestion(0),0));
////        System.out.println(t.checkAnswer("B"));
//        t.assignAnswers(1);
//        System.out.println(t.output_question(t.getQuestion(1),1));
//
//
//    }


}
