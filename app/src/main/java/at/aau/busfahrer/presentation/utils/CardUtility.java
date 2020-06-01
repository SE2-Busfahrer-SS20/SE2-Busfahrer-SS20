package at.aau.busfahrer.presentation.utils;

import android.graphics.Color;
import android.widget.TextView;

import shared.model.Card;

/**
 * Contains shared utility functions for the GUI.
 */

public class CardUtility {

    private static final String BLACK = "#000000";
    private static final String RED = "#FF0000";

    public static void turnCard(TextView tV, Card c) {
        //Id suit is Pick or Kreuz -> change color to red
        if (c.getSuit() == 1 || c.getSuit() == 2) {
            tV.setTextColor(Color.parseColor(RED));//Red
        }

        if (tV.getText().equals("\uD83C\uDCA0")) {//if it shows the cards back-side
            tV.setText(c.toString());
        } else {
            tV.setText("\uD83C\uDCA0");//set to card back side
            tV.setTextColor(Color.parseColor(BLACK));//black
        }
    }

    // turns a card back
    public static void turnCardBack(TextView tV){
        if(!tV.getText().equals("\uD83C\uDCA0")){
            tV.setText("\uD83C\uDCA0");//set to card back side
            tV.setTextColor(Color.parseColor(BLACK));//black
        }
    }

    // this methods generate a new text view based on the card text view
    public static TextView turnCardGetView(TextView tV, Card c) {
        TextView newCard = new TextView(tV.getContext());
        newCard.setText(tV.getText());
        newCard.setTextSize(tV.getTextSize());
        newCard.setTextColor(tV.getCurrentTextColor());
        if (c.getSuit() == 1 || c.getSuit() == 2) {
            newCard.setTextColor(Color.parseColor(RED));//Red
        }
        if (newCard.getText().equals("\uD83C\uDCA0")) {//if it shows the cards back-side
            newCard.setText(c.toString());
        } else {
            newCard.setText("\uD83C\uDCA0");//set to card back side
            newCard.setTextColor(Color.parseColor(BLACK));//black
        }
        return newCard;
    }

    /**
     * Returns a Card object from a Card Array with the equal string value.
     * @param cardString
     * @param cards
     * @return card.
     */


    public static Card getCardFromString(String cardString, Card[] cards) {
        for (Card card : cards)
            if (card.toString().equals(cardString))
                return card;
        return null;
    }


}
