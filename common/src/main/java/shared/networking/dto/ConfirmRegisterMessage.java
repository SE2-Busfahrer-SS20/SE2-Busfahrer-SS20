package shared.networking.dto;

import shared.model.Card;
import shared.model.Player;

public class ConfirmRegisterMessage extends BaseMessage {

    boolean master =false;
    private int ID;
    private Card[] cards;

    ConfirmRegisterMessage() {

    }

    public ConfirmRegisterMessage(Card[] cards) {
        this.cards = cards;
    }

    public ConfirmRegisterMessage(Player player){
        ID=player.getTempID();
        cards=player.getCards();

    }

    public ConfirmRegisterMessage(Player player,boolean master){
        ID=player.getTempID();
        this.master=master;
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

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }
}
