package at.aau.server.service.impl;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import at.aau.server.GameServer;
import at.aau.server.service.GameService;
import at.aau.server.service.PLapService;
import shared.model.Game;
import shared.model.Player;
import shared.networking.dto.DealPointsMessage;
import shared.networking.dto.StartPLabMessage;
import shared.networking.dto.WinnerLooserMessage;


/**
 * PLab Service contains the game logic for the "Pyramiden Runde".
 * @Version: 1.0
 * @Author: Gerold Auer
 */
public class PLapServiceImpl implements PLapService {

    private GameServer server;
    private GameService gameService;

    public PLapServiceImpl(GameServer server) {
        this.server = server;
        this.gameService = GameServiceImpl.getInstance();
    }

    /**
     * Start Lab and send 10 PLab Cards to every Player.
     */
    @Override
    public void startLab(Connection connection) {
        if(this.gameService.getGame() != null) {
            Log.info("PLAb Started", "start send message to " + connection.toString() );
            connection.sendTCP(
                    new StartPLabMessage(this.gameService.getGame().getpCards(), getPlayerNames())
            );
        } else {
            Log.error("Game Object is null!, You need to start a game first.");
        }
    }

    @Override
    public void finishLab(String playerName, int points) {
        printPlayerPoints();
        List<Player> playerList = gameService.getGame().getPlayerList();
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getName().equals(playerName)) {
                playerList.get(i).addPoints(points);
            }
        }
        gameService.getGame().setPlayerList(playerList);
        gameService.getGame().playerFinishedPLav(); // increases the counter for finished players.
        // check if the lap is finished, then update all players and notify the looser to start "Bushmen Activity"
        if (lapFinished()) {
            updatePlayers();
        }
        System.out.println("After setting new Points.");
        printPlayerPoints();
    }

    @Override
    public void start() {
        addListener();
    }

    @Override
    public void updatePlayers() {
        List<Player> playerList = gameService.getGame().getPlayerList();
        // Sort players on points attribute.
        playerList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p1.getScore() - p2.getScore();
            }
        });
        for(Player p : playerList) {
            WinnerLooserMessage msg = new WinnerLooserMessage();
            // set looser to true when the player is the last in the list (Looser).
            msg.setIsLooser(playerList.indexOf(p) == playerList.size() - 1);
            p.getConnection().sendTCP(msg);
        }
    }

    private List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player p : this.gameService.getGame().getPlayerList()) {
            playerNames.add(p.getName());
        }
        return playerNames;
    }

    private void addListener() {
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof StartPLabMessage) {
                    Log.debug("PLab started for Connection: ", connection.toString());
                    startLab(connection);
                }
                if (object instanceof DealPointsMessage) {
                    DealPointsMessage msg = (DealPointsMessage) object;
                    Log.debug("PLab got Points.", + msg.getPoints() + " points for player: " + msg.getDestPlayerName());
                    finishLab(msg.getDestPlayerName(), msg.getPoints());
                }

            }
        });
    }

    /**
     * Just for Testing. TODO: remove.
     */
    private void printPlayerPoints() {
        for(Player p : gameService.getGame().getPlayerList()) {
            System.out.println("Player: " + p.getName() + " has: " + p.getScore() + " points.");
        }
    }

    private boolean lapFinished() {
        Game game = gameService.getGame();
        return game.getPlapFinishedCount() == game.getPlayerList().size();
    }

}
