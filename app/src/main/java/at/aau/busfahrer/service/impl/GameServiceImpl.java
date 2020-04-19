package at.aau.busfahrer.service.impl;


import at.aau.busfahrer.presentation.GuessActivity;
import at.aau.busfahrer.service.GameService;
import shared.model.Game;
import shared.networking.NetworkClient;
import shared.networking.dto.CreateGameMessage;
import shared.networking.kryonet.NetworkClientKryo;

public class GameServiceImpl implements GameService {

    private NetworkClient client;

    public GameServiceImpl(String host) {

        this.client = new NetworkClientKryo(host);
        client.start();
    }


    public void createGame(int playercount, String gameName) {

        client.sendMessage(new CreateGameMessage(playercount, gameName));

    }
}
