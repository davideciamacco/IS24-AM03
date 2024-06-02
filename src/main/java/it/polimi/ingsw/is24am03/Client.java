package it.polimi.ingsw.is24am03;

public interface Client {
    public void CreateGame(int nPlayers, String nickname);
    public void JoinGame(String nickname);
    public void PickColor(String color);
    public void DrawResource();
    public void DrawGold();
    public void DrawTable(int choice);
    public void PlaceCard(int choice, int i, int j, String face);
    public void ChooseStartingCardSide(String face);
    public void setGUI(ViewInterface view);
    public void setCLI(ViewInterface view);
    public void ChooseObjectiveCard(int choice);
    public void sendGroupText(String text);

    public void sendPrivateText(String Receiver, String text);

    public void RejoinGame(String nickname);
}
