package it.polimi.ingsw.is24am03.Subscribers;

import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;

import java.rmi.RemoteException;

/**
 * A PlayerBoardSub is a class which observes a PlayerBoard class in order to obtain updates from it.
 */
public interface PlayerBoardSub extends Subscriber{

    /**
     * Provides the nickname associated to the subscriber
     * @return the nickname of the player subscribed to a certain PlayerBoard class
     * @throws RemoteException RMI Exception
     */
    String getSub() throws RemoteException;

    /**
     * Provides an update regarding the player board of a player who placed a card
     * @param player refers to the player who placed a card
     * @param playableCard refers to the card placed
     * @param i refers to the row of the matrix representing the player's board in which the card has been placed
     * @param j refers to the column of the matrix representing the player's board in which the card has been placed
     * @throws RemoteException RMI Exception
     */
    void notifyChangePlayerBoard(String player, PlayableCard playableCard, int i, int j) throws RemoteException;




}
