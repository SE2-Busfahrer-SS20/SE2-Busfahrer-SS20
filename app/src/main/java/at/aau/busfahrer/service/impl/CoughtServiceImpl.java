package at.aau.busfahrer.service.impl;

import java.util.List;
import at.aau.busfahrer.service.CoughtService;
import shared.model.Player;
import shared.model.impl.PlayersStorageImpl;
import shared.model.impl.GameImpl;
import shared.networking.dto.PlayedMessage;

public class CoughtServiceImpl implements CoughtService {

    private static CoughtServiceImpl instance;

    private List<Player> playerList;
    private GameImpl gameImpl;
    private int currentPlayer;
    private Player playerCheated;
    private Player myself;
    private int scoreCheater;
    private  int myScore;
    private PlayersStorageImpl pl;
    private int indexOfMe;

    private CoughtServiceImpl(){};

    public static CoughtServiceImpl getInstance(){
        if(CoughtServiceImpl.instance == null){
            CoughtServiceImpl.instance = new CoughtServiceImpl();
        }
        return CoughtServiceImpl.instance;
    }

    public  boolean isCheating(){
        //Check wich player's turn it is
        playerList = gameImpl.getPlayerList();
        /*
        Statt den GameImpl wird von PlayerStorage die PlayerListe geholt
        Alles abändern wo GameImpl verwendet wird
        Auch den curretnPlayer von PlayerStorage holen NICHT von GamImpl
         */

        //get the index of the curren player on the playerList
        currentPlayer = pl.getCurrentTurn();
        //get the Index of myself from the player list
        indexOfMe = pl.getTempID();

        myself = playerList.get(indexOfMe);
        playerCheated = playerList.get(currentPlayer);
        //if the currentplayer has cheated, he get one point and I lose one point
        if (playerCheated.isCheatedThisRound()){

            //the player who cheated increases his score
            scoreCheater = playerCheated.getScore();
            scoreCheater++;
            playerCheated.setScore(scoreCheater);

            //myScore will be decremented one time
            //ACHTUNG keine negativen Punkte keine minuszahlen
            myScore = myself.getScore();
            myScore--;
            myself.setScore(myScore);
            return true;

        }else{
            //the player who has NOT cheated decreases his score
            //ACHTUNG keine negativen Punkte keine minuszahlen
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
