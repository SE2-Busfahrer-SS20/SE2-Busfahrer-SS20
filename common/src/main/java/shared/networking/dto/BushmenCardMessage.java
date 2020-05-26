package shared.networking.dto;

import shared.model.Card;

public class BushmenCardMessage extends BaseMessage {

    private int CardId;
    private Card card;

    public BushmenCardMessage() {
    }

    public BushmenCardMessage(int cardId, Card card) {
        CardId = cardId;
        this.card=card;
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
