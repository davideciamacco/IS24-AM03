package it.polimi.ingsw.is24am03;

public interface GameInterface {
    public void startGame();
    public void drawResources(Player P, ResourcesDeck RD);
    public void drawGold(Player P, GoldDeck GD);
    public void drawTable(Player P, int choice);
    public void addPlayer(Player P);
    public void placeCard(Player P, PlayableCard c, int i, int j, boolean face);
    public ArrayList<Player> checkWinner();
    public Player getCurrentPlayer();
    public ArrayList <Colors> getAvailableColors();
    public void sendMessageAll(Player sender, String message);
    public void sendMessage(Player sender, Player receiver, String message);
}

