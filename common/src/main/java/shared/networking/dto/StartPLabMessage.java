package shared.networking.dto;

import java.util.List;
import java.util.Set;

import shared.model.Card;
import shared.model.Player;

public class StartPLabMessage extends BaseMessage {

    // 10 cards for the "Pyramidenrunde".
    private Set<Card> plabCards;
    private List<Player> playerList;

    /**
     * Kryonet needs Constructor without parameters.
     */
    public StartPLabMessage() {}
    /**
     * @param plabCards
     */
    public StartPLabMessage(Set<Card> plabCards, List<Player> playerList) {
        this.plabCards = plabCards;
        this.playerList = playerList;
    }

    public Set<Card> getPlabCards() {
        return plabCards;
    }

    public void setPlabCards(Set<Card> plabCards) {
        this.plabCards = plabCards;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
