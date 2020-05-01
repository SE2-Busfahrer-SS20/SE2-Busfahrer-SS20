package shared.networking.dto;

import shared.model.Card;

public class StartPLabMessage extends BaseMessage {

    // 10 cards for the "Pyramidenrunde".
    private Card[] plabCards;

    /**
     * Kryonet needs Constructor without parameters.
     */
    public StartPLabMessage() {}
    /**
     * @param plabCards
     */
    public StartPLabMessage(Card[] plabCards) {

    }
}
