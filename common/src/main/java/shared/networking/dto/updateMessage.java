package shared.networking.dto;

import java.util.ArrayList;

//Dieses DTO wird nach jedem Zug der Raterunden an alle Clients gesendet
public class updateMessage extends BaseMessage {
    int currentPlayer;  //ID des Spielers der jetzt dran ist
    ArrayList<Integer> score; //Punkte aller Spieler (index=id)
}

