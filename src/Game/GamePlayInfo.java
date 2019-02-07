package Game;

import Models.Question;
import Models.Quiz;
import REST.questionsApi;
import REST.quizApi;

import java.io.IOException;
import java.util.*;

public class GamePlayInfo {

    private int numOfQuiz;
    private List<Question> allquestions = new ArrayList<>();
    private List<Quiz> allquizzes = new ArrayList<>();
    private HashMap<String, String> answers = new HashMap<String, String>();
    private String letter = null;
    private Random r = new Random();
    private quizApi quizzies = new quizApi();
    private questionsApi questions = new questionsApi();
    private Quiz chosenQuiz = new Quiz();
    private int currentQuiz = 0;
    private Long quizId;

    public GamePlayInfo() throws IOException {
        this.allquizzes = quizzies.getQuizzies();
        this.numOfQuiz = allquizzes.size();
        this.setShuffledQuizzies();
        chooseQuiz();
        System.out.println("number of quizzes: " + numOfQuiz);

    }

    public int getNumofQuestions() {
        return allquestions.size();
    }

    public void chooseQuiz() {

        if (numOfQuiz < currentQuiz) {
            setShuffledQuizzies();
            currentQuiz = 0;
        } else {
            chosenQuiz = allquizzes.get(currentQuiz);
            this.quizId = chosenQuiz.getId();
            this.currentQuiz++;
            System.out.println("chosen quiz: " + chosenQuiz.getTitle());

        }


    }

    public void setShuffledQuizzies() {
        Collections.shuffle(this.allquizzes);
        System.out.println("Shuffling here: " + allquizzes.get(0).getTitle());


    }

    public List<Quiz> allSQuizzies() {
        return allquizzes;
    }


    public void setQuestions() throws IOException {
        System.out.println("Quiz id" + quizId);
        allquestions = questions.getQuizQuestions(Long.valueOf(quizId));
        System.out.println(allquestions.get(0).getQuestion());
//        System.out.println(allquestions.get(1).getQuestion());

    }

    public List<Question> getQuestions() {
        return allquestions;
    }

    public String getQuestion(int current) {
        return allquestions.get(current).getQuestion();
    }

    public String getWelcomeQuizMessage() {
        String s = "<p> The quiz chosen is called  " + chosenQuiz.getTitle() + ".</p> " + "<break time=\"0.3s\" /> " + " Accept to continue with this quiz" + "<break time=\"0.5s\" /> " + "or deny to be given another quiz!  ";
        System.out.println(s);
        return s;
    }

    public String getAnswer(int current) {
        return allquestions.get(current).getRight();
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
        answers.put(array[0], allquestions.get(current).getRight());
        answers.put(array[1], allquestions.get(current).getWrong1());
        answers.put(array[2], allquestions.get(current).getWrong2());
        answers.put(array[3], allquestions.get(current).getWrong3());
//        System.out.println(answers.get("A"));
//        System.out.println(answers.get("B"));
//        System.out.println(answers.get("C"));
//        System.out.println(answers.get("D"));

    }

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
//        System.out.println(string);
        return string;
    }


    public String questionSingleOutput(String question) {
        String string = " ";
        string += question + " <p> Is it ? </p>" + "<break time=\"0.5s\" /> " + " <p> <say-as interpret-as=\"characters\">A</say-as> </p>" + answers.get("A") + "<p> <say-as interpret-as=\"characters\">B</say-as> </p>" + answers.get("B") + "<p> <say-as interpret-as=\"characters\">C</say-as> </p>" + answers.get("C") + " <p> or <say-as interpret-as=\"characters\">D</say-as> </p>" + answers.get("D");
        return string;
    }

    public String getCongrats() {

        String string = " ";
        int low = 1;
        int high = 6;
        int random = r.nextInt(high - low) + low;
        switch (random) {
            case 1:
                string = " <p><prosody volume=\"x-loud\"> Fantastic </prosody> Well done</p>";
                break;
            case 2:
                string = " <p> You are correct ! You are smart after all</p> ";
                break;
            case 3:
                string = " <p>  <prosody volume=\"x-loud\">Hmmmm </prosody> </p> That is correct did you cheat ... ? ";
                break;
            case 4:
                string = " <p> Good answer, You are Correct </p> ";
                break;
            case 5:
                string = "<p> Well done that is correct </p> ";
                break;
            case 6:
                string = "<p> Great Job </p> ";
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
                string = " <p> Unlucky!!! </p>   The answer was " + "<break time=\"0.5s\" /> " + letter + "<break time=\"0.3s\" /> " + answer + ". ";
                break;
            case 2:
                string = "<p> Hard luck ! The answer was</p> " + "<break time=\"0.5s\" /> " + letter + "<break time=\"0.3s\" /> " + answer + " . ";
                break;
            case 3:
                string = "<p> Better luck next time!</p> " + "<break time=\"0.5s\" /> " + letter + "<break time=\"0.3s\" /> " + answer + " was the answer.  ";
                break;
        }

        return string;
    }


}
