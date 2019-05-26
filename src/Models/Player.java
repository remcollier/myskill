package Models;

import java.io.Serializable;

public class Player implements Serializable {
    public Player() {
    }

    public Player(String id, int score, int count) {
        this.id = id;
        this.score = score;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private int score;
    private int count;
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
