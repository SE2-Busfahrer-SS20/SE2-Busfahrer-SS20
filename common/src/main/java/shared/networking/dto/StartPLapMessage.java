package shared.networking.dto;

import java.util.List;
import java.util.Set;

import shared.model.Card;
import shared.model.Player;

public class StartPLapMessage extends BaseMessage {

    // 10 cards for the "Pyramidenrunde".
    private Card[] plabCards;
    private List<String> playerNames;

    /**
     * Kryonet needs Constructor without parameters.
     */
    public StartPLapMessage() {}
    /**
     * @param plabCards
     */
    public StartPLapMessage(Card[] plabCards, List<String> playerNames) {
        this.plabCards = plabCards;
        this.playerNames = playerNames;
    }

    public StartPLapMessage(Card[] plabCards) {
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
