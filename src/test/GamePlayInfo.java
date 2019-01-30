package test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GamePlayInfo {

    private long quiz_id;


    private String quizname;
    private int num;

    public int getScore() {
        return score;
    }

    public void addScore() {
        this.score++;
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
        Question h = new Question(1, "hello", "hello", "hello1", "hello2", "hello3");
        question.add(h);
    }

    //use this to get  questions from database
    public List<Question> getRandomQuestions() {
//get sql query to get the quiz
        return question;
    }

    public String getQuestion(int current) {
        return question.get(current).getQuestion();
    }
    public String getAnswer(int current)
    {
        return question.get(current).getAnswer();


    }

    public void assignLettersToAns(int current) {

        String array[] = {"A", "B", "C", "D"};
        Random rgen = new Random();  // Random number generator

        for (int i = 0; i < array.length; i++) {
            int randomPosition = rgen.nextInt(array.length);
            String temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }
        this.letter = array[0];
        answers.put(question.get(current).getAnswer(), array[0]);
        answers.put(question.get(current).getWronganswer1(), array[1]);
        answers.put(question.get(current).getWronganswer2(), array[2]);
        answers.put(question.get(current).getWronganswer3(), array[3]);
        System.out.println("Correct one " + this.letter);
        System.out.println(answers.get(question.get(current).getAnswer()));
        System.out.println(answers.get(question.get(current).getWronganswer1()));
        System.out.println(answers.get(question.get(current).getWronganswer2()));
        System.out.println(answers.get(question.get(current).getWronganswer3()));

    }


    public boolean checkAnswer(String letter, int current) {
//        if this.letter.equalsIgnoreCase(letter))
        if (answers.get(question.get(current).getAnswer()).equalsIgnoreCase(letter)) {
            return true;
        }
        return false;

    }


    public String output_question(String question) {
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

//    public static void main(String[] args) {
//        GamePlayInfo t = new GamePlayInfo(1, 3);
//        t.setQuestions();
//        t.assignLettersToAns(0);
//        System.out.println(t.output_question(t.getQuestion(0)));
//        System.out.println(t.checkAnswer("B", 0));
//
//
//    }


}
