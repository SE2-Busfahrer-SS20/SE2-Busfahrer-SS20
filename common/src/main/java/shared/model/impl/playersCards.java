package shared.model.impl;

import shared.model.Card;

public class playersCards {

    //this class is only used to store the players cards in common because they can not be stored in app
    // since the listener which receives them from the server is in NetwerkClientKryo.java and from there
    //it is not possible to access objects in the app

    private static Card[] cards;

    public static Card[] getCards() {
        return cards;
    }

    public static void setCards(Card[] cards) {
        playersCards.cards = cards;
    }
}
