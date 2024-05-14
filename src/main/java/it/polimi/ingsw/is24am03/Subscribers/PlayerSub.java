package it.polimi.ingsw.is24am03.Subscribers;


import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.enums.Color;

import java.rmi.RemoteException;
import java.util.ArrayList;

//contiene i metodi implementati dall'observer della classe player
public interface PlayerSub extends Subscriber {

    public void ReceiveUpdateOnPoints(String player, int points) throws RemoteException;

    public void NotifyChangePersonalCards(String Player,ArrayList<ResourceCard>hand) throws RemoteException;

    public void notifyChoiceObjective(String player, ObjectiveCard o) throws RemoteException;

    public String getSub() throws RemoteException;


    public void notifyFirstHand(ResourceCard p1, ResourceCard p2, ResourceCard p3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2) throws RemoteException;


    public void notifyAvailableColors(ArrayList<Color> colors) throws RemoteException;

}
