package at.aau.server.database.Table;

public class Score implements Table{
    private int userid;
    private int scoreValue;

    public Score(int userid, int score) {
        super();
        this.userid = userid;
        this.scoreValue = score;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getScore() {
        return scoreValue;
    }

    public void setScore(int score) {
        this.scoreValue = score;
    }
}
