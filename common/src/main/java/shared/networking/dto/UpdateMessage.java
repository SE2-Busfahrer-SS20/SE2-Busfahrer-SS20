package shared.networking.dto;

import java.util.ArrayList;

//Dieses DTO wird nach jedem Zug der Raterunden an alle Clients gesendet
public class UpdateMessage extends BaseMessage {

    int currentPlayer;  //in next round, it is this players turn
    ArrayList<Integer> score; //Points of all players

    public UpdateMessage(){}

    public UpdateMessage(int currentPlayer, ArrayList<Integer> score) {
        this.currentPlayer = currentPlayer;
        this.score = score;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<Integer> getScore() {
        return score;
    }

    public void setScore(ArrayList<Integer> score) {
        this.score = score;
    }
}

