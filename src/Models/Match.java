package Models;

public class Match {

    private Long id;
    private String playerOne;
    private String playerTwo;
    private int playerOneScore;
    private int playerTwoScore;
    private long quizId;

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    private String completed;

    public Match() {
    }

    public Match(Long id, String playerOne, String playerTwo, int playerOneScore, int playerTwoScore, long quizId) {
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerOneScore = playerOneScore;
        this.playerTwoScore = playerTwoScore;
        this.quizId = quizId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public int getPlayerOneScore() {
        return playerOneScore;
    }

    public void setPlayerOneScore(int playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    public void setPlayerTwoScore(int playerTwoScore) {
        this.playerTwoScore = playerTwoScore;
    }

    public long getQuizId() {
        return quizId;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }


}
