package shared.networking.dto;


//Dieses DTO wird von dem Spieler, der gerade dran ist, an den Server gesendet nachdem er seine Entscheidung getroffen hat.
public class playedMessage extends BaseMessage {
    boolean guess; //Ob der Client Richtig geraten hat
    int earnedPoints;
}
