package shared.networking.dto;

import shared.model.Card;

public class BushmenMessage extends BaseMessage {
    private Card[] cards;

    public BushmenMessage(Card[] cards) {
        this.cards = cards;
    }

    public BushmenMessage(){

    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }
}
