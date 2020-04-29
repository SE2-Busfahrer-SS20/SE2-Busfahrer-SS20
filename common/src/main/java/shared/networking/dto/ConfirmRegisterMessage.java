package shared.networking.dto;

import shared.model.Card;
import shared.model.Player;
import shared.model.impl.CardImpl;

public class ConfirmRegisterMessage extends BaseMessage {

    boolean master =false;
    private int ID;
    private Card[] cards;

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

    //just for test purpose
    public ConfirmRegisterMessage() {
        this.ID=0;
        this.cards = new CardImpl[4];
        //4x Herz Ass
        this.cards[0] = new CardImpl(1,1);
        this.cards[1] = new CardImpl(1,1);
        this.cards[2] = new CardImpl(1,1);
        this.cards[3] = new CardImpl(1,1);
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }


}
