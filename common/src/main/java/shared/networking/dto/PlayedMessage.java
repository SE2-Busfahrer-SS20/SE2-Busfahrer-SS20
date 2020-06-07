package shared.networking.dto;


import shared.model.GameState;

//Dieses DTO wird von dem Spieler, der gerade dran ist, an den Server gesendet nachdem er seine Entscheidung getroffen hat.
public class PlayedMessage extends BaseMessage {

    GameState lap;
    int tempID; //must be equal with Index in playerList.(ArrayList in Game Object)
    boolean scored; //stores if the client guessed correct

    public PlayedMessage(){}

    public PlayedMessage(GameState lap, int tempID, boolean scored){
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

    public GameState getLap() {
        return lap;
    }

    public void setLap(GameState lap) {
        this.lap = lap;
    }
}


