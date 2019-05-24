package Models;

public class Player {
    public Player(String id) {
        this.id = id;
    }

    private String id;
    private int score = 0;
    private int count = 0;
    private String outcome;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
