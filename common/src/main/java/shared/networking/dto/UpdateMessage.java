package shared.networking.dto;

import shared.model.PlayerDTO;

import java.util.ArrayList;
import java.util.List;

//Dieses DTO wird nach jedem Zug der Raterunden an alle Clients gesendet
public class UpdateMessage extends BaseMessage {

    private int currentPlayer;  //in next round, it is this players turn
    private List<PlayerDTO> playerList; //Points of all players

    public UpdateMessage(){}

    public UpdateMessage(int currentPlayer, List<PlayerDTO> playerList) {
        this.currentPlayer = currentPlayer;
        this.playerList= playerList;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<PlayerDTO> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerDTO> playerList) {
        this.playerList = playerList;
    }
}

