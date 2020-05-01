package shared.networking.dto;

import java.util.Set;

import shared.model.Card;

public class StartPLabMessage extends BaseMessage {

    // 10 cards for the "Pyramidenrunde".
    private Set<Card> plabCards;

    /**
     * Kryonet needs Constructor without parameters.
     */
    public StartPLabMessage() {}
    /**
     * @param plabCards
     */
    public StartPLabMessage(Set<Card> plabCards) {
        this.plabCards = plabCards;
    }
}
