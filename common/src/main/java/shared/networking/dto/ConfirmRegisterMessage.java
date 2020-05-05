package shared.networking.dto;

import shared.model.Card;
import shared.model.Player;
import shared.model.impl.CardImpl;

public class ConfirmRegisterMessage extends BaseMessage {

    boolean master =false;
    private int ID;
    private Card[] cards;

    ConfirmRegisterMessage() {

    }

    public ConfirmRegisterMessage(int ID, Card[] cards) {
        this.ID=ID;
        this.cards = cards;
    }

    public ConfirmRegisterMessage(Player player){
        ID=0; //is ID still needed?
        cards=player.getCards();
    }

    public ConfirmRegisterMessage(Player player,boolean master){
        this.master=master;
        ID=0; //is ID still needed?
        cards=player.getCards();
    }

    public Card[] getCards() {
        return cards;
    }
    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public boolean isMaster() {
        return master;
    }
    public void setMaster(boolean master) {
        this.master = master;
    }

}
