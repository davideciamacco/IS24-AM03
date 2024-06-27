package it.polimi.ingsw.is24am03.Subscribers;

import java.rmi.RemoteException;

/**
 * A ChatSub is a class which observes a Chat class in order to receive update from it.
 */
public interface ChatSub extends Subscriber {

    /**
     * Provides the nickname associated to the subscriber
     * @return the nickname of the ChatSub subscribed to the Chat class in the server
     * @throws RemoteException RMI Exception
     */
    String getSub() throws RemoteException;

    /**
     * Notifies a certain sub on a group text message.
     * @param sender represents the player who sent the text
     * @param text contains the text message
     * @throws RemoteException RMI Exception
     */
    void ReceiveGroupText(String sender, String text) throws RemoteException;

    /**
     * Notifies a sub he received a private text message.
     * @param sender represents the player who sent the text
     * @param receiver represents the player recipient
     * @param text represents the text message
     * @throws RemoteException RMI Exception
     */
    void ReceivePrivateText(String sender, String receiver, String text) throws RemoteException;
}
