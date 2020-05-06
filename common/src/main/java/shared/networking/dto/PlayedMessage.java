package shared.networking.dto;


//Dieses DTO wird von dem Spieler, der gerade dran ist, an den Server gesendet nachdem er seine Entscheidung getroffen hat.
public class PlayedMessage extends BaseMessage {

    int lap;
    int tempID; //must be equal with Index in playerList.(ArrayList in Game Object)
    boolean scored; //Ob der Client Richtig geraten hat
    //int earnedPoints; // can be determined by guess variable

    public PlayedMessage(){}

    public PlayedMessage(int lap, int tempID, boolean scored){
        this.lap=lap;
        this.tempID=tempID;
        this.scored=scored;

    }

    public boolean scored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }

    public int getTempID() {
        return tempID;
    }

    public void setTempID(int tempID) {
        this.tempID = tempID;
    }

    public int getLap() {
        return lap;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }
}


/*
lap:
1 = Guess 1: black or red
2 = Guess 2:
3 = Guess 3:
4 = Guess 4:
5 = ...Pyramid
 */
