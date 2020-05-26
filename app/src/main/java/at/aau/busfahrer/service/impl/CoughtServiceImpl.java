package at.aau.busfahrer.service.impl;

import java.util.List;
import at.aau.busfahrer.service.CoughtService;
import shared.model.Player;
import shared.model.PlayerDTO;
import shared.model.impl.PlayersStorageImpl;

public class CoughtServiceImpl implements CoughtService {

    private static CoughtService instance;


    private List<PlayerDTO> playerList;
    private int currentPlayer;
    private PlayerDTO playerCheated;
    private PlayerDTO myself;
    private int scoreCheater;
    private  int myScore;
    private PlayersStorageImpl pl;
    private int indexOfMe;

    private CoughtServiceImpl(){};

    public static CoughtService getInstance(){
        if(instance == null){
            instance = new CoughtServiceImpl();
        }
        return instance;
    }

    public  boolean isCheating(){
        pl = PlayersStorageImpl.getInstance();
        playerList = pl.getPlayerList();
        //Check wich player's turn it is
        //get the index of the curren player on the playerList
        currentPlayer = pl.getCurrentTurn();
        //get the Index of myself from the player list
        indexOfMe = pl.getTempID();

        myself = playerList.get(indexOfMe);
        playerCheated = playerList.get(currentPlayer);
        //if the currentplayer has cheated, he get one point and I lose one point
        if (playerCheated.isCheating()){

            //CughtMessage an server schicken !!!

            //the player who cheated increases his score
            scoreCheater = playerCheated.getScore();
            scoreCheater++;

//            playerCheated.setScore(scoreCheater);

            //myScore will be decremented one time
            myScore = myself.getScore();
            if(myScore!=0){
                myScore--;
            }


//            myself.setScore(myScore);
            return true;

        }else{
            //the player who has NOT cheated decreases his score
//            scoreCheater = playerCheated.getScore();
//            if(scoreCheater!=0){
//                scoreCheater--;
//            }
//            playerCheated.setScore(scoreCheater);
//
//            //myScore will be increased one time
//            myScore = myself.getScore();
//            myScore++;
//            myself.setScore(myScore);
            return false;
        }

    }
}
