package at.aau.server.database.table;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScoreTest {
    private Score score;

    @Before
    public void setup(){
        score= new Score(5, 23);
    }

    @Test
    public void getUserIdTest() {
        Assert.assertEquals(5, score.getUserid());
    }
    @Test
    public void setUserIdTest() {
        Assert.assertEquals(5, score.getUserid());
        score.setUserid(2);
        Assert.assertEquals(2, score.getUserid());
    }
    @Test
    public void getScoreTest() {
        Assert.assertEquals(23, score.getScore());
    }
    @Test
    public void setScoreTest() {
        Assert.assertEquals(23, score.getScore());
        score.setScore(34);
        Assert.assertEquals(34, score.getScore());
    }
}
