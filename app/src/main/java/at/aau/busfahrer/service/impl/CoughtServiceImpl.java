package at.aau.busfahrer.service.impl;

import java.util.List;
import at.aau.busfahrer.service.CoughtService;
import shared.model.Player;
import shared.model.impl.PlayersStorageImpl;
import shared.model.impl.GameImpl;
import shared.networking.dto.PlayedMessage;

public class CoughtServiceImpl implements CoughtService {

    private List<Player> playerList;
    private GameImpl gameImpl;
    private int currentPlayer;
    private Player playerCheated;
    private Player myself;
    private int scoreCheater;
    private  int myScore;
    private PlayersStorageImpl pl;
    private int indexOfMe;

    public  boolean isCheating(){
        //Check wich player's turn it is
        playerList = gameImpl.getPlayerList();
        //get the index of the curren player on the playerList
        currentPlayer = gameImpl.getCurrentPlayer();
        //get the Index of myself from the player list
        indexOfMe = pl.getTempID();

        myself = playerList.get(indexOfMe);
        playerCheated = playerList.get(currentPlayer);
        //if the currentplayer has cheated, he get one point and I lose one point
        if (playerCheated.isCheatedThisRound() == true){

            //the player who cheated increases his score
            scoreCheater = playerCheated.getScore();
            scoreCheater++;
            playerCheated.setScore(scoreCheater);

            //myScore will be decremented one time
            myScore = myself.getScore();
            myScore--;
            myself.setScore(myScore);
            return true;

        }else{
            //the player who has NOT cheated decreases his score
            scoreCheater = playerCheated.getScore();
            scoreCheater--;
            playerCheated.setScore(scoreCheater);

            //myScore will be increased one time
            myScore = myself.getScore();
            myScore++;
            myself.setScore(myScore);
            return false;
        }

    }
}
