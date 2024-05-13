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

    public void ChooseObjectiveCard(int choice);

    public void sendGroupText(String sender, String text);

    public void sendPrivateText(String sender, String Receiver, String text);

}
