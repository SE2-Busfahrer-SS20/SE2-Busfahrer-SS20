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
    private String mac;

    public PlayerDTOImpl() {
    }

    public PlayerDTOImpl(String name, Integer score, boolean isCheating) {
        this.name = name;
        this.score = score;
        this.isCheating = isCheating;
        this.isBusdriver = false;
    }
    public PlayerDTOImpl(String name, Integer score, boolean isCheating, String mac) {
        this.name = name;
        this.score = score;
        this.isCheating = isCheating;
        this.isBusdriver = false;
        this.mac =mac;
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

    @Override
    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
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

    @Override
    public String getMAC() {
        return this.mac;
    }

    @Override
    public void setMAC(String mac) {
        this.mac=mac;
    }

    public static List<PlayerDTO> getDTOFromPlayerList(List<Player> playerList){

        List<PlayerDTO> playerListDTO= new ArrayList<>();

        for (Player player : playerList) {
            playerListDTO.add(new PlayerDTOImpl(player.getName(), player.getScore(), player.isCheated()));
        }
        return playerListDTO;
    }
    public static PlayerDTO getDTOFromPlayer(Player player){
        return new PlayerDTOImpl(player.getName(), player.getScore(), player.isCheated());
    }
}
