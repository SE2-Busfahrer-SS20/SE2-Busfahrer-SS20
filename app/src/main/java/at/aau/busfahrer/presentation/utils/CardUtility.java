package at.aau.busfahrer.presentation.utils;

import android.graphics.Color;
import android.widget.TextView;

import shared.model.Card;

/**
 * Contains shared utility functions for the GUI.
 */
public class CardUtility {
    public static void turnCard(TextView tV, Card c) {
            //Id suit is Pick or Kreuz -> change color to red
            if(c.getSuit()==1||c.getSuit()==2){
                tV.setTextColor(Color.parseColor("#FF0000"));//Red
            }

            if(tV.getText().equals("\uD83C\uDCA0")) {//if it shows the cards back-side
                tV.setText(c.toString());
            }else{
                tV.setText("\uD83C\uDCA0");//set to card back side
                tV.setTextColor(Color.parseColor("#000000"));//black
            }
    }
}
