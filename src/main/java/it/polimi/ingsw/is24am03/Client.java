package it.polimi.ingsw.is24am03;

public interface Client {
    public void CreateGame(int nPlayers, String nickname);
    public void JoinGame(String nickname);

    void PickColor(String color);
}
