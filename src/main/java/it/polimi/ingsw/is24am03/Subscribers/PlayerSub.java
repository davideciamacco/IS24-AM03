package it.polimi.ingsw.is24am03.Subscribers;


import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.enums.Color;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * A PlayerSub is a class which observes a Player class, in order to receive update from it.
 *
 */
public interface PlayerSub extends Subscriber {

    /**
     * Provides update about the points of a certain player
     * @param player refers to the player who scored those points
     * @param points amount of points scored by the player
     * @throws RemoteException RMI Exception
     */
    public void ReceiveUpdateOnPoints(String player, int points) throws RemoteException;

    /**
     * Provides update about the personal cards of a certain player after a drawing.
     * @param Player refers to the player who drew a card
     * @param hand updated set of personal cards assigned to the player
     * @throws RemoteException RMI Exception
     */
    public void NotifyChangePersonalCards(String Player,ArrayList<ResourceCard>hand) throws RemoteException;

    /**
     *Provides update about the secret objective card chose by a certain player
     * @param player refers to the player who chose his secret objective card
     * @param objectiveCard refers to the objective card chose by the player
     * @throws RemoteException RMI Exception
     */
    public void notifyChoiceObjective(String player, ObjectiveCard objectiveCard) throws RemoteException;

    /**
     * Provides the nickname associated to the subscriber
     * @return the nickname of the player susbscribed to a certain Player class
     * @throws RemoteException RMI Exception
     */
    public String getSub() throws RemoteException;

    /**
     * Provides the first update sent to a player about his personal cards, his starting card and the two objective cards from which he will have to choose
     * @param resourceCard1 refers to the first card in the player's hand
     * @param resourceCard2 refers to the second card in the player's hand
     * @param resourceCard3 refers to the third card in the player's hand
     * @param startingCard refers to the starting card assigned to the player, he will have to choose the side
     * @param objectiveCard1 refers to the first objective card assigned to the player
     * @param objectiveCard2 refers to the second objective card assigned to the player
     * @throws RemoteException RMI Exception
     */
    public void notifyFirstHand(ResourceCard resourceCard1, ResourceCard resourceCard2, ResourceCard resourceCard3, StartingCard startingCard, ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2) throws RemoteException;

    /**
     * Provides the available colors from which a certain player can choose.
     * @param colors the available colors
     * @throws RemoteException RMI Exception
     */
    public void notifyAvailableColors(ArrayList<Color> colors) throws RemoteException;

}
