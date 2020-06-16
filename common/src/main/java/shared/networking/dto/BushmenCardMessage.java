package shared.networking.dto;

import shared.model.Card;
import shared.model.PlayerDTO;

import java.util.List;

public class BushmenCardMessage extends BaseMessage {

    private int CardId;
    private Card card;
    private List<PlayerDTO> playerList;



    public BushmenCardMessage() {
    }

    public BushmenCardMessage(int cardId, Card card, List<PlayerDTO> playerList) {
        CardId = cardId;
        this.card=card;
        this.playerList=playerList;
    }
    public List<PlayerDTO> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerDTO> playerList) {
        this.playerList = playerList;
    }

    public int getCardId() {
        return CardId;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setCardId(int cardId) {
        CardId = cardId;
    }
}
