package at.aau.server.service.impl;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import at.aau.server.GameServer;
import at.aau.server.service.GameService;
import at.aau.server.service.PLapService;
import shared.model.Game;
import shared.model.Player;
import shared.model.PlayerDTO;
import shared.model.impl.PlayerDTOImpl;
import shared.networking.dto.DealPointsMessage;
import shared.networking.dto.StartPLapMessage;
import shared.networking.dto.UpdateMessage;
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
                    new StartPLapMessage(this.gameService.getGame().getpCards(), getPlayerNames())
            );
        } else {
            Log.error("Game Object is null!, You need to start a game first.");
        }
    }

    @Override
    public void finishLab(String playerName, int points) {
        printPlayerPoints();
        List<Player> playerList = gameService.getGame().getPlayerList();
        for (Player player : playerList) {
            if (player.getName().equals(playerName)) {
                player.addPoints(points);
            }
        }
        gameService.getGame().setPlayerList(playerList);
        gameService.getGame().playerFinishedPLap(); // increases the counter for finished players.
        // check if the lap is finished, then update all players and notify the looser to start "Bushmen Activity"
        if (lapFinished()) {
            updatePlayers();
            Log.debug("finished Lap");
        }
    }

    @Override
    public void start() {
        addListener();
    }

    @Override
    public void updatePlayers() {
        List<Player> playerList = gameService.getGame().getPlayerList();
        // save scoreList before sorting. It's important that this list is still in the legacy order.
        List<PlayerDTO> scoreList = getScoreList(playerList);
        // Sort players on points attribute.
        playerList.sort(Comparator.comparingInt(Player::getScore));
        // get the index of the busdriver in the unsorted list.
        String tempBusdriverName = playerList.get(playerList.size()-1).getName();
        int currentPlayerIndex = setBusdriver(tempBusdriverName, scoreList);
        for(Player p : playerList) {
            WinnerLooserMessage msg = new WinnerLooserMessage();
            // Message to update the score list in the PlayerStorage Class in frontend
            UpdateMessage updateMessage = new UpdateMessage();
            updateMessage.setCurrentPlayer(currentPlayerIndex);
            updateMessage.setPlayerList(scoreList);
            // set looser to true when the player is the last in the list (Looser).
            msg.setIsLooser(playerList.indexOf(p) == playerList.size() - 1);
            p.getConnection().sendTCP(msg);
            p.getConnection().sendTCP(updateMessage);
        }
    }

    /**
     * Set the boolean attribute of the looser (Busdriver) to true.
     * Returns the index of the busdricer for the current player attribute in UpdateMessage.
     * @param tempBusdriverName
     * @param scoreList
     * @return index of busdriver.
     */
    private int setBusdriver(String tempBusdriverName, List<PlayerDTO> scoreList) {
        for(PlayerDTO p: scoreList){
            if(p.getName().equals(tempBusdriverName)) {
                p.setBusdriver();
                return scoreList.indexOf(p);
            }
        }
        return 0;
    }

    /**
     * Function to Map a List of Player objects into List of PlayerDTO objects.
     * @param playerList
     * @return
     */
    private List<PlayerDTO> getScoreList(List<Player> playerList) {
        List<PlayerDTO> dtoList = new ArrayList<>();
        for (Player p: playerList)
            dtoList.add(mapPlayer(p));
        return dtoList;
    }

    /**
     * Maps a Player Object into a PlayerDTO object.
     * @param player
     * @return
     */
    private PlayerDTO mapPlayer(Player player) {
        return new PlayerDTOImpl(player.getName(), player.getScore(), player.isCheated());
    }

    /**
     * Returns Player Names, just public for testing purposes.
     * @return playerNames List<String>
     */
    public List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player p : this.gameService.getGame().getPlayerList()) {
            playerNames.add(p.getName());
        }
        return playerNames;
    }

    private void addListener() {
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                // check if game exists.
                if (!gameService.gameExists()) {
                    Log.error("PLabService: Game does not exist anymore.");
                } else if (object instanceof StartPLapMessage) {
                    Log.debug("PLab started for Connection: ", connection.toString());
                    startLab(connection);
                } else if (object instanceof DealPointsMessage) {
                    DealPointsMessage msg = (DealPointsMessage) object;
                    Log.debug("PLab got Points.", + msg.getPoints() + " points for player: " + msg.getDestPlayerName());
                    finishLab(msg.getDestPlayerName(), msg.getPoints());
                }

            }
        });
    }

    /**
     * Print Player Points for debugging purposes.
     */
    private void printPlayerPoints() {
        for(Player p : gameService.getGame().getPlayerList()) {
            Log.debug("Player: " + p.getName() + " has: " + p.getScore() + " points.");
        }
    }

    /**
     * Check if all players finished the pyramiden lap.
     * It's just public for testing purposes.
     * @return playerList.size() == FinishedCount.
9     */
    public boolean lapFinished() {
        Game game = gameService.getGame();
        return game.getPlapFinishedCount() == game.getPlayerList().size();
    }

}
