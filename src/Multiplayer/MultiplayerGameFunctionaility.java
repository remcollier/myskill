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

    public List<Question> getQuestions() {
        return questions;
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

    public String getCurrentPlayer(String userId, String player1_token) {
        Gson gson = new Gson();
        Token token = new Token();
        token.setAccountId(userId);
        String tokenRequest = gson.toJson(token);
        String tokenGiven = gameApi.sendToken(tokenRequest);
        System.out.println(tokenGiven);
        if (tokenGiven.equalsIgnoreCase(player1_token)) {
            return "Player1";

        } else {
            return "Player2";

        }
    }


    public void GameSetupMode() throws IOException {
        setGameQuiz(quiz.getQTD());
        setQuizId(quiz.getQTD().getId());
        setQuestions();

    }

    public String getCongrats() {

        String string = " ";
        int low = 1;
        int high = 9;
        int random = r.nextInt(high - low) + low;
        switch (random) {
            case 1:
                string = " <audio src='soundbank://soundlibrary/human/amzn_sfx_large_crowd_cheer_03'/>  <p> Fantastic " + "<break time=\"0.5s\" /> " + " Well done</p>";
                break;
            case 2:
                string = "<audio src='soundbank://soundlibrary/human/amzn_sfx_large_crowd_cheer_01'/> <p> You are correct ! You are smart after all</p> ";
                break;
            case 3:
                string = " <audio src='soundbank://soundlibrary/human/amzn_sfx_crowd_boo_01'/> <p>  <prosody volume=\"x-loud\">Hmmmm </prosody> </p> That is correct did you cheat ... ? ";
                break;
            case 4:
                string = " <audio src='soundbank://soundlibrary/human/amzn_sfx_drinking_slurp_01'/>  <p> Good answer, You are Correct </p> ";
                break;
            case 5:
                string = " <p> Well done that is correct </p> <audio src='soundbank://soundlibrary/human/amzn_sfx_crowd_applause_03'/>";
                break;
            case 6:
                string = " <audio src='soundbank://soundlibrary/human/amzn_sfx_crowd_cheer_med_01'/> <p> Great Job, You are really good </p> ";
                break;
            case 7:
                string = " <say-as interpret-as=\"interjection\"> bravo </say-as>   <p> Good answer, You are Correct </p> ";
                break;
            case 8:
                string = " <say-as interpret-as=\"interjection\">Woo hoo</say-as>  <p>  Well done that is correct </p>";
                break;
            case 9:
                string = " <audio src='soundbank://soundlibrary/human/amzn_sfx_crowd_applause_05'/> <p> Great Job, You are really good </p> ";
                break;
        }

        return string;
    }

    public String outputIncorrectAnswer(String answer) {
        String string = " ";
        int low = 1;
        int high = 7;
        int random = r.nextInt(high - low) + low;
        switch (random) {
            case 1:
                string = " <audio src='soundbank://soundlibrary/human/amzn_sfx_laughter_giggle_01'/> <p> Unlucky!!! </p>   The answer was " + "<break time=\"0.5s\" /> " + letter + "<break time=\"0.3s\" /> " + answer + ". ";
                break;
            case 2:
                string = "<audio src='soundbank://soundlibrary/human/amzn_sfx_crowd_boo_02'/> <p> Hard luck ! The answer was</p> " + "<break time=\"0.5s\" /> " + letter + "<break time=\"0.3s\" /> " + answer + " . ";
                break;
            case 3:
                string = " <audio src='soundbank://soundlibrary/human/amzn_sfx_laughter_01'/> <p> Better luck next time!</p> " + "<break time=\"0.5s\" /> " + letter + "<break time=\"0.3s\" /> " + answer + " was the answer.  ";
                break;

            case 4:
                string = " <say-as interpret-as=\"interjection\">aw man</say-as> <p> Better luck next time!</p> " + "<break time=\"0.5s\" /> " + letter + "<break time=\"0.3s\" /> " + answer + " was the answer.  ";
                break;

            case 5:
                string = " <say-as interpret-as=\"interjection\">jeepers creepers</say-as> <p>   The answer was " + "<break time=\"0.5s\" /> " + letter + "<break time=\"0.3s\" /> " + answer + ". </p>";
                break;
            case 6:
                string = "<say-as interpret-as=\"interjection\"> whoops a daisy </say-as><p> Hard luck ! The answer was</p> " + "<break time=\"0.5s\" /> " + letter + "<break time=\"0.3s\" /> " + answer + " . ";
                break;
            case 7:
                string = "<say-as interpret-as=\"interjection\">  mamma mia </say-as><p> Hard luck ! The answer was</p> " + "<break time=\"0.5s\" /> " + letter + "<break time=\"0.3s\" /> " + answer + " . ";
                break;
        }


        return string;
    }
    public String getAnswer(int current) {
        return questions.get(current).getRight();
    }


}



