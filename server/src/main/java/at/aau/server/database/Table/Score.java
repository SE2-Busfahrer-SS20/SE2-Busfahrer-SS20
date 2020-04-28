package at.aau.server.database.Table;

import java.sql.Connection;

public class Score extends Table{
    private int userid;
    private int score;

    public Score(int userid, int score) {
        super();
        this.userid = userid;
        this.score = score;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
