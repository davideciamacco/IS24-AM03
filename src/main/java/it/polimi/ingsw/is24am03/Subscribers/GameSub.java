package it.polimi.ingsw.is24am03.Subscribers;
//interfaccia che espone i metodi che observer di game può chiamare


import it.polimi.ingsw.is24am03.Subscribers.Subscriber;
import it.polimi.ingsw.is24am03.messages.MessageType;
import it.polimi.ingsw.is24am03.server.model.cards.*;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface GameSub extends Subscriber {

    public void notifyJoinedPlayer(String player) throws RemoteException;

    //notify winners
    public void notifyWinners(ArrayList<String> winners) throws RemoteException;

    //notify turn order
    public void notifyTurnOrder(ArrayList<String> order) throws RemoteException;

    //notify current player
    public void notifyCurrentPlayer(String current) throws RemoteException;

    public void notifyCrashedPlayer(String username) throws RemoteException;

    //notify changed game status
    public void notifyChangeState(State gameState) throws RemoteException;

    public void notifyRejoinedPlayer(String rejoinedPlayer) throws RemoteException;

    public String getSub() throws RemoteException;

    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2) throws RemoteException;

    public void updateCommonTable(ResourceCard resourceCard, int index) throws RemoteException;

    public void NotifyNumbersOfPlayersReached() throws RemoteException;

   public void NotifyLastRound()throws RemoteException;

    public void notifyFinalColors(Map<String, Color> colors) throws RemoteException;

    public void UpdateCrashedPlayer(String nickname, ArrayList<Text> chat, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table, ArrayList<Color> colors) throws RemoteException;

    public void UpdateFirst(ArrayList<ResourceCard> commons) throws RemoteException;

}
