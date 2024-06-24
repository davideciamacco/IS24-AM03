package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.Subscribers.*;
import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.game.Game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class GeneralSubscriber implements GameSub, PlayerSub, PlayerBoardSub, ChatSub {

    String nickname;
    public GeneralSubscriber(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void notifyJoinedPlayer(String player) throws RemoteException {

    }

    @Override
    public void notifyWinners(ArrayList<String> winners) throws RemoteException {

    }

    @Override
    public void notifyTurnOrder(ArrayList<String> order) throws RemoteException {

    }

    @Override
    public void notifyCurrentPlayer(String current) throws RemoteException {

    }

    @Override
    public void notifyCrashedPlayer(String username) throws RemoteException {

    }

    @Override
    public void notifyChangeState(State gameState) throws RemoteException {

    }

    @Override
    public void notifyRejoinedPlayer(String rejoinedPlayer) throws RemoteException {

    }

    @Override
    public void notifyChangePlayerBoard(String player, PlayableCard p, int i, int j) throws RemoteException {

    }

    @Override
    public void ReceiveUpdateOnPoints(String player, int points) throws RemoteException {

    }

    @Override
    public void NotifyChangePersonalCards(String Player, ArrayList<ResourceCard> hand) throws RemoteException {

    }

    @Override
    public void notifyChoiceObjective(String player, ObjectiveCard o) throws RemoteException {

    }

    @Override
    public String getSub() throws RemoteException {
        return this.nickname;
    }

    @Override
    public void notifyFirstHand(ResourceCard p1, ResourceCard p2, ResourceCard p3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2) throws RemoteException {

    }

    @Override
    public void notifyAvailableColors(ArrayList<Color> colors) throws RemoteException {

    }

    @Override
    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2) throws RemoteException {

    }

    @Override
    public void updateCommonTable(ResourceCard resourceCard, int index) throws RemoteException {

    }

    @Override
    public void NotifyNumbersOfPlayersReached() throws RemoteException {

    }

    @Override
    public void NotifyLastRound() throws RemoteException {

    }

    @Override
    public void notifyFinalColors(Map<String, Color> colors) throws RemoteException {

    }

    @Override
    public void UpdateCrashedPlayer(String nickname, ArrayList<Text> chat, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table, ArrayList<Color> colors) throws RemoteException {

    }

    @Override
    public void UpdateFirst(ArrayList<ResourceCard> commons) throws RemoteException {

    }

    @Override
    public void ReceiveGroupText(String sender, String text) throws RemoteException {

    }

    @Override
    public void ReceivePrivateText(String sender, String receiver, String text) throws RemoteException {

    }
}
