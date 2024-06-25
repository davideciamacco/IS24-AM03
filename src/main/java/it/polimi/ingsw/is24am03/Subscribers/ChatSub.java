package it.polimi.ingsw.is24am03.Subscribers;

import it.polimi.ingsw.is24am03.Subscribers.Subscriber;

import java.rmi.RemoteException;

public interface ChatSub extends Subscriber {


    public String getSub() throws RemoteException;
    public void ReceiveGroupText(String sender, String text) throws RemoteException;

    public void ReceivePrivateText(String sender, String receiver, String text) throws RemoteException;
}
