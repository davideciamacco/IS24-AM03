package it.polimi.ingsw.is24am03.Subscribers;

import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;

import java.rmi.RemoteException;

public interface PlayerBoardSub extends Subscriber{

    public void notifyChangePlayerBoard(String player, PlayableCard p, int i, int j) throws RemoteException;

    public String getSub() throws RemoteException;


}
