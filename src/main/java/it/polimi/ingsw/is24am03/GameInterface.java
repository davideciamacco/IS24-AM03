package it.polimi.ingsw.is24am03;

public interface GameInterface {
    //public void startGame() throws NotAllPlayersHaveJoinedException;
    public void drawResources(String player) throws EmptyDeckException, PlayerNotInTurnException, InvalidStateException;
    public void drawGold(String player) throws PlayerNotInTurnException, InvalidStateException, EmptyDeckException;
    public void drawTable(String player, int choice) throws PlayerNotInTurnException, InvalidStateException, NullCardSelectedException;
    public void addPlayer(String player) throws FullLobbyException, NicknameAlreadyUsedException;
    public void placeCard(String player, int choice, int i, int j, boolean face) throws PlayerNotInTurnException, InvalidStateException;
    public void setObjectiveCard(String player, int choice) throws PlayerNotInTurnException;

    public void selectStartingFace(String player, boolean face) throws PlayerNotInTurnException, InvalidStateException;

    //public ArrayList<Player> checkWinner();
    //public Player getCurrentPlayer();
    //public ArrayList <Colors> getAvailableColors();
    //public void sendMessageAll(Player sender, String message);
    //public void sendMessage(Player sender, Player receiver, String message);
}

