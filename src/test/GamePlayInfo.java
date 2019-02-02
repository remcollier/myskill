package test;

import java.util.*;

public class GamePlayInfo {
    public GamePlayInfo(long quiz_id, int num) {
        this.quiz_id = quiz_id;
        this.num = num;
    }

    private long quiz_id;
    private String quizname;
    private int num;
    private int score;
    private List<Question> question = new ArrayList<>();
    private HashMap<String, String> answers = new HashMap<String, String>();
    private String letter = null;
    private Random r = new Random();

    public int getScore() {
        return score;
    }

    //save to database
    public void setScore(int score) {
        this.score = score;
    }

    public void setQuizname(String quizname) {
        this.quizname = quizname;
    }

    public long getQuiz_id() {
        return quiz_id;
    }

    public String getQuizname() {
        return quizname;
    }

    //retrieve sql randomly
    public void setQuestions() {
        Question sample1 = new Question(1, " Who won the World Cup last year? ", "France", "Germany", "Brazil", "Scotland");
        Question sample2 = new Question(2, " Who won the golden boot in the World Cup? ", "Harry Kane", "Messi", "Pogba", "Ronaldo");
        Question sample3 = new Question(3, " Who plays for Juventus at the moment? ", "Ronaldo", "Higuain", "Messi", "Surez");
        Question sample4 = new Question(4, " What colour is the sea? ", "blue", "red", " purple", "white");
        question.add(sample1);
        question.add(sample2);
        question.add(sample3);
        question.add(sample4);
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

    //double check this
    public boolean checkAnswer(String letter) {
        boolean check = false;
        if (this.letter.equalsIgnoreCase(String.valueOf(letter.charAt(0)))) {
            check = true;
        }
        return check;
    }

    public String outputQuestion(String question) {
        String string = " ";
        int low = 1;
        int high = 4;
        int random = r.nextInt(high - low) + low;
        switch (random) {
            case 1:
                string = " The next question is " + question + ".... ";
                break;
            case 2:
                string = " The following question is " + question + "...... ";
                break;
            case 3:
                string = " On to the next question  " + question + "..... ";
                break;
            case 4:
                string = question;
                break;
        }
        string += " Is it ?...." + "  ......A.   " + answers.get("A") + " ........B.    " + answers.get("B") + "  ........C.    " + answers.get("C") + " or ..........D.    " + answers.get("D");
        return string;
    }


    public String questionSingleOutput(String question) {
        String string = " ";
        string += question + "  Is it ?...." + "  ......A.   " + answers.get("A") + " ........B.    " + answers.get("B") + "  ........C.    " + answers.get("C") + " or ..........D.    " + answers.get("D");
        return string;
    }

    public String getCongrats() {

        String string = " ";
        int low = 1;
        int high = 6;
        int random = r.nextInt(high - low) + low;
        switch (random) {
            case 1:
                string = "..... Fantastic!!! Well done.... ";
                break;
            case 2:
                string = "..... You are correct ! You are smart after all..... ";
                break;
            case 3:
                string = "..... Hmmmm..... That is correct did you cheat ... ? ";
                break;
            case 4:
                string = "..... Good answer, You are Correct ";
                break;
            case 5:
                string = ".....Well done that is correct ";
                break;
            case 6:
                string = ".....Great Job... ? ";
                break;
        }

        return string;
    }

    public String outputIncorrectAnswer(String answer) {
        String string = " ";
        int low = 1;
        int high = 3;
        int random = r.nextInt(high - low) + low;
        switch (random) {
            case 1:
                string = "..... Unlucky!!!  The answer was..... " + letter + ".....   " + answer + ". ";
                break;
            case 2:
                string = "..... Hard luck ! The answer was..... " + letter + "....   " + answer + " . ";
                break;
            case 3:
                string = "..... Better luck next time!..... " + letter + " ....  " + answer + " was the answer.  ";
                break;
        }

        return string;
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
