package shared.networking.dto;


//Dieses DTO wird von dem Spieler, der gerade dran ist, an den Server gesendet nachdem er seine Entscheidung getroffen hat.
public class playedMessage extends BaseMessage {

    int tempID; //must be equal with Index in playerList.(ArrayList in Game Object)
    boolean guess; //Ob der Client Richtig geraten hat
    int earnedPoints;


    public playedMessage(boolean guess, int earnedPoints){
        this.guess=guess;
        this.earnedPoints=earnedPoints;
    }

    public boolean isGuess() {
        return guess;
    }

    public void setGuess(boolean guess) {
        this.guess = guess;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public int getTempID() {
        return tempID;
    }

    public void setTempID(int tempID) {
        this.tempID = tempID;
    }
}
