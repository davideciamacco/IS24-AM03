package it.polimi.ingsw.is24am03.client.connection;

import it.polimi.ingsw.is24am03.client.view.ViewInterface;

/**
 *
 * This is the interface that handles all the communication methods, it will be implemented by the two classes that handles the connection with RMI and TCP
 *
 */
public interface Client {

    /**
     * Method used to perform the game creation in the server
     * @param nPlayers number of players for the game
     * @param nickname username of the player who creates a game
     */
    public void CreateGame(int nPlayers, String nickname);

    /**
     * Method that makes it possible for the player to join the game
     * @param nickname username of the player who joins the game
     */
    public void JoinGame(String nickname);

    /**
     * Method that makes it possible for the player to choose a color
     * @param color the color chosen
     */
    public void PickColor(String color);

    /**
     * Method that makes it possible for the player to draw a card from the resource deck
     */
    public void DrawResource();

    /**
     * Method that makes it possible for the player to draw a card from the gold deck
     */
    public void DrawGold();

    /**
     * Method that makes it possible for the player to draw a card from one of the four cards on the common table
     * @param choice number of the chosen card
     */
    public void DrawTable(int choice);

    /**
     * Method that makes it possible for the player to place a card
     * @param choice number of the chosen card
     * @param i the row selected
     * @param j the column selected
     * @param face card's side selected
     */
    public void PlaceCard(int choice, int i, int j, String face);

    /**
     * Method that makes it possible for the player to choose a side for the starting card
     * @param face side chosen
     */
    public void ChooseStartingCardSide(String face);

    /**
     * Method used to set the GUI view for a client
     * @param view reference to the view interface
     */
    public void setGUI(ViewInterface view);

    /**
     * Method used to set the CLI view for a client
     * @param view reference to the view interface
     */
    public void setCLI(ViewInterface view);

    /**
     * Method that makes it possible for a player to choose his personal objective card
     * @param choice the number of the chosen objective card, either 1 or 2
     */
    public void ChooseObjectiveCard(int choice);

    /**
     * Method that makes it possible for a player to send a text in the group chat
     * @param text text message
     */
    public void sendGroupText(String text);

    /**
     * Method that makes it possible for a player to send a private text to a certain player
     * @param Receiver nickname of the recipient
     * @param text text message
     */
    public void sendPrivateText(String Receiver, String text);

    /**
     * Method that makes it possible for a player to rejoin the game
     * @param nickname player who wants to rejoin
     */
    public void RejoinGame(String nickname);
}
