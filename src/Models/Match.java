package Models;

public class Match {

    private Long id;
    private String playerOne;
    private String playerTwo;
    private String playerOneScore;
    private String playerTwoScore;
    private String outcome;

    public Match() {
    }

    public Match(Long id, String playerOne, String playerTwo, String playerOneScore, String playerTwoScore, String outcome) {
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerOneScore = playerOneScore;
        this.playerTwoScore = playerTwoScore;
        this.outcome = outcome;
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

    public String getPlayerOneScore() {
        return playerOneScore;
    }

    public void setPlayerOneScore(String playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public String getPlayerTwoScore() {
        return playerTwoScore;
    }

    public void setPlayerTwoScore(String playerTwoScore) {
        this.playerTwoScore = playerTwoScore;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

}
