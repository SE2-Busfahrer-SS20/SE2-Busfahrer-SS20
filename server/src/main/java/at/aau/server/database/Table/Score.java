package at.aau.server.database.Table;

public class Score implements Table{
    private int userId;
    private int scoreValue;

    public Score(int userid, int score) {
        super();
        this.userId = userid;
        this.scoreValue = score;
    }

    public int getUserid() {
        return userId;
    }

    public void setUserid(int userid) {
        this.userId = userid;
    }

    public int getScore() {
        return scoreValue;
    }

    public void setScore(int score) {
        this.scoreValue = score;
    }
}
