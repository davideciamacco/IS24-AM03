package it.polimi.ingsw.is24am03.Subscribers;

import it.polimi.ingsw.is24am03.server.model.player.Player;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
//il sub si iscrive a un listener per essere notificato di cambiamenti di stato
public interface Subscriber extends Remote,Serializable{
        String getSub()throws RemoteException;

}
