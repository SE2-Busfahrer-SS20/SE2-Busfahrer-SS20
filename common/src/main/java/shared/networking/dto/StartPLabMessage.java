package shared.networking.dto;

import java.util.List;
import java.util.Set;

import shared.model.Card;
import shared.model.Player;

public class StartPLabMessage extends BaseMessage {

    // 10 cards for the "Pyramidenrunde".
    private Card[] plabCards;
    private List<String> playerNames;

    /**
     * Kryonet needs Constructor without parameters.
     */
    public StartPLabMessage() {}
    /**
     * @param plabCards
     */
    public StartPLabMessage(Card[] plabCards, List<String> playerNames) {
        this.plabCards = plabCards;
        this.playerNames = playerNames;
    }

    public StartPLabMessage(Card[] plabCards) {
        this.plabCards = plabCards;
    }

    public Card[] getPlabCards() {
        return plabCards;
    }

    public void setPlabCards(Card[] plabCards) {
        this.plabCards = plabCards;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
    }
}
