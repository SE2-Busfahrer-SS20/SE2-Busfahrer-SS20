package at.aau.busfahrer.service;

public interface GameService {

    void createGame(int playercount);

    void playGame(String name, String MACAddress);

    void startGame();
}
