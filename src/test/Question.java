package test;

public class Question {


    public Question(long question_id) {
        this.question_id = question_id;
    }

    private long question_id;
    private String question;
    private String answer;
    private String wronganswer1;
    private String wronganswer2;
    private String wronganswer3;

    public Question(long question_id, String question, String answer, String wronganswer1, String wronganswer2, String wronganswer3) {
        this.question_id = question_id;
        this.question = question;
        this.answer = answer;
        this.wronganswer1 = wronganswer1;
        this.wronganswer2 = wronganswer2;
        this.wronganswer3 = wronganswer3;
    }


    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getWronganswer1() {
        return wronganswer1;
    }

    public void setWronganswer1(String wronganswer1) {
        this.wronganswer1 = wronganswer1;
    }

    public String getWronganswer2() {
        return wronganswer2;
    }

    public void setWronganswer2(String wronganswer2) {
        this.wronganswer2 = wronganswer2;
    }

    public String getWronganswer3() {
        return wronganswer3;
    }

    public void setWronganswer3(String wronganswer3) {
        this.wronganswer3 = wronganswer3;
    }


}
