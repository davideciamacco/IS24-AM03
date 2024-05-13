package it.polimi.ingsw.is24am03.Subscribers;

import it.polimi.ingsw.is24am03.Subscribers.Subscriber;

import java.rmi.RemoteException;

public interface ChatSub extends Subscriber {


    //notify new private message
    public String getSub() throws RemoteException;
    //metodo chiamato dal modello presente nel client che notificherà chi di dovere del messaggio ricevuto
    //questo metodo è usato per la chat di gruppo
    public void ReceiveGroupText(String sender, String text) throws RemoteException;

    //notify new group chat message
    public void ReceivePrivateText(String sender, String receiver, String text) throws RemoteException;
}
