package it.polimi.ingsw.is24am03.client.connection;

import it.polimi.ingsw.is24am03.client.view.ViewInterface;

/**
 *
 */
public interface Client {

    /**
     *
     * @param nPlayers
     * @param nickname
     */
    public void CreateGame(int nPlayers, String nickname);

    /**
     *
     * @param nickname
     */
    public void JoinGame(String nickname);

    /**
     *
     * @param color
     */
    public void PickColor(String color);

    /**
     *
     */
    public void DrawResource();

    /**
     *
     */
    public void DrawGold();

    /**
     *
     * @param choice
     */
    public void DrawTable(int choice);

    /**
     *
     * @param choice
     * @param i
     * @param j
     * @param face
     */
    public void PlaceCard(int choice, int i, int j, String face);

    /**
     *
     * @param face
     */
    public void ChooseStartingCardSide(String face);

    /**
     *
     * @param view
     */
    public void setGUI(ViewInterface view);

    /**
     *
     * @param view
     */
    public void setCLI(ViewInterface view);

    /**
     *
     * @param choice
     */
    public void ChooseObjectiveCard(int choice);

    /**
     *
     * @param text
     */
    public void sendGroupText(String text);

    /**
     *
     * @param Receiver
     * @param text
     */
    public void sendPrivateText(String Receiver, String text);

    /**
     *
     * @param nickname
     */
    public void RejoinGame(String nickname);
}
