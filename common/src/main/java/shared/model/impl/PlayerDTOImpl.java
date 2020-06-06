package shared.model.impl;

import shared.model.Player;
import shared.model.PlayerDTO;

import java.util.ArrayList;
import java.util.List;

public class PlayerDTOImpl implements PlayerDTO {

    private String name;
    private Integer score;
    private boolean isCheating;
    private boolean isBusdriver;

    public PlayerDTOImpl() {
    }

    public PlayerDTOImpl(String name, Integer score, boolean isCheating) {
        this.name = name;
        this.score = score;
        this.isCheating = isCheating;
        this.isBusdriver = false;
    }

    @Override
    public boolean isBusdriver() {
        return isBusdriver;
    }
    @Override
    public void setBusdriver() {
        this.isBusdriver = true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setCheating(boolean cheating) {
        this.isCheating = cheating;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public boolean isCheating() {
        return isCheating;
    }

    public static List<PlayerDTO> getDTOFromPlayerList(List<Player> playerList){

        List<PlayerDTO> playerListDTO= new ArrayList<>();

        for (int i = 0; i < playerList.size(); i++) {
            playerListDTO.add(new PlayerDTOImpl(playerList.get(i).getName(), playerList.get(i).getScore(), playerList.get(i).isCheated()));
        }
        return playerListDTO;
    }
    public static PlayerDTO getDTOFromPlayer(Player player){
        return new PlayerDTOImpl(player.getName(), player.getScore(), player.isCheated());
    }
}
