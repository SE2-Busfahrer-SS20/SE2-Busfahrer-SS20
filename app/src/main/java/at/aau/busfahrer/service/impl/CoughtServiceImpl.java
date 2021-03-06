package at.aau.busfahrer.service.impl;

import java.util.List;
import at.aau.busfahrer.service.CoughtService;
import at.aau.busfahrer.service.GamePlayService;
import shared.model.PlayerDTO;
import shared.model.impl.PlayersStorageImpl;

public class CoughtServiceImpl implements CoughtService {

    private static CoughtService instance;
    private List<PlayerDTO> playerList;
    private PlayerDTO playerCheated;
    private PlayerDTO myself;
    private int scoreCheater;
    private int myScore;
    private PlayersStorageImpl pl;
    private int indexOfMe;
    private GamePlayService gamePlayService;


    private CoughtServiceImpl() {
        pl = PlayersStorageImpl.getInstance();
        gamePlayService = GamePlayServiceImpl.getInstance();
    }

    public static CoughtService getInstance() {
        if (instance == null) {
            instance = new CoughtServiceImpl();
        }
        return instance;
    }

    public void getProperties(){
        gamePlayService = GamePlayServiceImpl.getInstance();
        pl = PlayersStorageImpl.getInstance();
        playerList = pl.getPlayerList();
        //get the Index of myself from the player list
        indexOfMe = pl.getTempID();
        myself = playerList.get(indexOfMe);
        myScore = myself.getScore();
    }

    public boolean isCheating() {
        int currentPlayer;
        getProperties();
        //Check wich player's turn it is
        //get the index of the curren player on the playerList
        currentPlayer = pl.getCurrentTurn();

        playerCheated = playerList.get(currentPlayer);
        //if the currentplayer has cheated, he get one point and I lose one point
        if (playerCheated.isCheating()) {
            //the player who cheated increases his score
            scoreCheater = playerCheated.getScore();
            scoreCheater++;
            playerCheated.setScore(scoreCheater);
            //myScore will be decremented one time
            myScore = decrementScore(myScore);
            myself.setScore(myScore);
            gamePlayService.sendMsgCought(currentPlayer, indexOfMe, scoreCheater, myScore, playerCheated.isCheating());
            return true;

        } else {
            //the player who has NOT cheated decreases his score
            scoreCheater = playerCheated.getScore();
            scoreCheater = decrementScore(scoreCheater);
            playerCheated.setScore(scoreCheater);
            //myScore will be increased one time
            myScore++;
            myself.setScore(myScore);
            gamePlayService.sendMsgCought(currentPlayer, indexOfMe, scoreCheater, myScore, playerCheated.isCheating());
            return false;
        }

    }

    public boolean isCheatingPlap() {
        getProperties();
        boolean cheated = false;

        for (int i = 0; i < playerList.size(); i++) {
            if (i != indexOfMe && playerList.get(i).isCheating()) {
                scoreCheater = playerList.get(i).getScore();
                scoreCheater++;
                myScore = decrementScore(myScore);
                myself.setScore(myScore);
                gamePlayService.sendMsgCought(i, indexOfMe, scoreCheater, myScore, playerList.get(i).isCheating());
                cheated = true;
            }
        }
        //If I cought Nobody, i get one point everyone else lost one point
        if (!cheated) {
            myScore++;
            myself.setScore(myScore);

            for (int j = 0; j < playerList.size(); j++) {
                if (j != indexOfMe) {
                    scoreCheater = playerList.get(j).getScore();
                    scoreCheater = decrementScore(scoreCheater);
                    gamePlayService.sendMsgCought(j, indexOfMe, scoreCheater, myScore, playerList.get(j).isCheating());
                }
            }
        }
        return cheated;
    }
    public int decrementScore(int score){
        if(score!=0){
            score--;
        }
        return score;
    }

    public boolean isCheatingBushmen(){
        int indexCheater;
        getProperties();
        indexCheater = pl.getCurrentTurn();
        playerCheated = playerList.get(indexCheater);

        if(playerCheated.isCheating() && playerCheated.isBusdriver()){
            //the player who cheated increases his score
            scoreCheater = playerCheated.getScore();
            scoreCheater++;
            playerCheated.setScore(scoreCheater);
            //myScore will be decremented one time
            myScore = decrementScore(myScore);
            myself.setScore(myScore);
            gamePlayService.sendMsgCought(indexCheater, indexOfMe, scoreCheater, myScore, playerCheated.isCheating());
            return true;
        }else{
            //the player who has NOT cheated decreases his score
            scoreCheater = playerCheated.getScore();
            scoreCheater = decrementScore(scoreCheater);
            playerCheated.setScore(scoreCheater);
            //myScore will be increased one time
            myScore++;
            myself.setScore(myScore);
            gamePlayService.sendMsgCought(indexCheater, indexOfMe, scoreCheater, myScore, playerCheated.isCheating());
            return false;
        }
    }
}
