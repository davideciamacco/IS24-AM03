package it.polimi.ingsw.is24am03.Subscribers;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A subscriber is a class which observe a model class in order to receive updates from it.
 */
public interface Subscriber extends Remote,Serializable{

        /**
         *It is necessary to identify the username of the observer that the client refers to
         * @return the username of the player that is observing a certain class
         * @throws RemoteException RMI Exception
         */
        String getSub()throws RemoteException;

}
