package it.polimi.ingsw.is24am03.client.clientModel;

import it.polimi.ingsw.is24am03.SceneType;
import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerBoardSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.client.view.ViewInterface;
import it.polimi.ingsw.is24am03.server.model.cards.*;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;

import java.io.Serial;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 */
public class ClientModel extends UnicastRemoteObject implements ChatSub, GameSub, PlayerSub, PlayerBoardSub {
    @Serial
    private static final long serialVersionUID= -7805876932328376622L;
    private String player;
    private ArrayList<Text> chat;
    private ArrayList<String> times=new ArrayList<>();
    private ResourceCard resourceDeck;
    private ResourceCard goldDeck;
    private ArrayList<Color> colors = new ArrayList<>();
    private ResourceCard card0;
    private ResourceCard card1;
    private ResourceCard card2;
    private ResourceCard card3;
    private ArrayList<ObjectiveCard> commonObjective;
    private String color;
    private Map<String, Boolean> playersState;
    private String current;
    private ArrayList<ResourceCard> hand;
    private ObjectiveCard objectiveCard1;
    private ObjectiveCard objectiveCard2;
    private StartingCard startingCard;
    private Map<String, Integer> playerPoints;
    private Map<String, PlayableCard[][]> boards;
    private ArrayList<String> players;
    private State gameState;
    private ViewInterface viewInterface;

    /**
     *
     * @param player
     * @param viewInterface
     * @throws RemoteException
     */
    public ClientModel(String player, ViewInterface viewInterface) throws RemoteException {
        super();
        this.player=player;
        this.viewInterface=viewInterface;
        this.chat=new ArrayList<>();
        this.resourceDeck=null;
        this.goldDeck=null;
        this.card0=null;
        this.card1=null;
        this.card2=null;
        this.card3=null;
        this.commonObjective=new ArrayList<>();
        this.playersState=new HashMap<>();
        this.hand=new ArrayList<>();
        this.objectiveCard1=null;
        this.objectiveCard2=null;
        this.startingCard=null;
        this.playerPoints=new HashMap<>();
        this.boards=new HashMap<>();
        this.boards.put(player,new PlayableCard[81][81]);
        this.players=new ArrayList<>();
        this.players.add(player);
        viewInterface.drawScene(SceneType.WAITING);
    }

    /**
     *
     * @param joinedPlayer
     * @throws RemoteException
     */
    @Override
    public synchronized void  notifyJoinedPlayer(String joinedPlayer) throws RemoteException {
        viewInterface.notifyJoinedPlayer(joinedPlayer);
    }

    /**
     *
     * @param winners
     * @throws RemoteException
     */
    @Override
    public synchronized void notifyWinners(ArrayList<String> winners)throws RemoteException {
        viewInterface.notifyWinners(winners);
    }

    /**
     *
     * @param order
     * @throws RemoteException
     */
    @Override
    public synchronized void  notifyTurnOrder(ArrayList<String> order) throws RemoteException{
        players=order;
        for(String s: order){
            playerPoints.put(s,0);
            boards.put(s,new PlayableCard[81][81]);
        }
        viewInterface.notifyTurnOrder(order, this.player);
    }

    /**
     *
     * @param current
     * @throws RemoteException
     */
    @Override
    public synchronized void notifyCurrentPlayer(String current)throws RemoteException {
        this.current = current;
        viewInterface.notifyCurrentPlayer(current,boards,player, hand, gameState);

    }

    /**
     *
     * @param username
     * @throws RemoteException
     */
    @Override
    public synchronized void notifyCrashedPlayer(String username)throws RemoteException {
        viewInterface.notifyCrashedPlayer(username);
    }

    /**
     *
     * @param gameState
     * @throws RemoteException
     */
    @Override
    public synchronized void notifyChangeState(State gameState)throws RemoteException {
        this.gameState=gameState;
        viewInterface.notifyChangeState(gameState);
    }

    /**
     *
     * @param rejoinedPlayer
     * @throws RemoteException
     */
    @Override
    public synchronized void notifyRejoinedPlayer(String rejoinedPlayer)throws RemoteException{
        if(!rejoinedPlayer.equals(this.player))
            viewInterface.notifyRejoinedPlayer(rejoinedPlayer);
    }

    /**
     *
     * @param player
     * @param p
     * @param i
     * @param j
     * @throws RemoteException
     */
    @Override
    public synchronized void notifyChangePlayerBoard(String player, PlayableCard p, int i, int j) throws RemoteException{
        PlayableCard[][] tempBoard;
        tempBoard= boards.get(player);
        tempBoard[i][j]=p;
        if (i + 1 < 81 && j + 1 < 81 && tempBoard[i + 1][j + 1] != null) {
            tempBoard[i + 1][j + 1].setCornerCoverage(0, true);
        }
        if (i + 1 < 81 && j - 1 >= 0 && tempBoard[i + 1][j - 1] != null) {
            tempBoard[i + 1][j - 1].setCornerCoverage(1, true);
        }
        if (i - 1 >= 0 && j + 1 < 81 && tempBoard[i - 1][j + 1] != null) {
            tempBoard[i - 1][j + 1].setCornerCoverage(3, true);
        }
        if (i - 1 >= 0 && j - 1 >= 0 && tempBoard[i - 1][j - 1] != null) {
            tempBoard[i - 1][j - 1].setCornerCoverage(2, true);
        }
        boards.put(player, tempBoard);
        viewInterface.notifyChangePlayerBoard(player, this.player, boards);
    }

    /**
     *
     * @param player
     * @param points
     * @throws RemoteException
     */
    @Override
    public synchronized void ReceiveUpdateOnPoints(String player, int points)throws RemoteException {
        playerPoints.put(player, points);
        viewInterface.ReceiveUpdateOnPoints(player, points);
        viewInterface.printUpdatedPoints(playerPoints);
    }

    /**
     *
     * @param Player
     * @param p
     * @throws RemoteException
     */
    @Override
    public synchronized  void NotifyChangePersonalCards(String Player, ArrayList<ResourceCard> p) throws RemoteException {
        hand=p;
        viewInterface.NotifyChangePersonalCards(p);
    }

    /**
     *
     * @param player
     * @param o
     * @throws RemoteException
     */
    @Override
    public synchronized void notifyChoiceObjective(String player, ObjectiveCard o)throws RemoteException {
        objectiveCard1=o;
        viewInterface.notifyChoiceObjective(o);

    }

    /**
     *
     * @return
     * @throws RemoteException
     */
    @Override
    public synchronized String getSub()throws RemoteException {
        return this.player;
    }

    /**
     *
     * @param p1
     * @param p2
     * @param p3
     * @param startingCard
     * @param o1
     * @param o2
     * @throws RemoteException
     */
    @Override
    public synchronized void notifyFirstHand(ResourceCard p1, ResourceCard p2, ResourceCard p3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2)throws RemoteException{
        hand.add(p1);
        hand.add(p2);
        hand.add(p3);
        this.startingCard=startingCard;
        this.objectiveCard1=o1;
        this.objectiveCard2=o2;
        viewInterface.notifyFirstHand(hand,startingCard,o1,o2);
    }

    /**
     *
     * @param objectiveCard1
     * @param objectiveCard2
     * @throws RemoteException
     */
    @Override
    public synchronized void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2)throws RemoteException{
        commonObjective.add(objectiveCard1);
        commonObjective.add(objectiveCard2);
        viewInterface.notifyCommonObjective(objectiveCard1,objectiveCard2);
    }

    /**
     *
     * @param resourceCard
     * @param index
     * @throws RemoteException
     */
    @Override
    public synchronized void updateCommonTable(ResourceCard resourceCard,int index) throws RemoteException {
        switch (index){
            case 0:
                this.resourceDeck=resourceCard;
                break;
            case 1:
                this.goldDeck=resourceCard;
                break;
            case 2:
                this.card0=resourceCard;
                break;
            case 3:
                this.card1=resourceCard;
                break;
            case 4:
                this.card2=resourceCard;
                break;
            case 5:
                this.card3=resourceCard;
                break;
        }
        if(index==1 || index==0)
            viewInterface.updateCommonTable(playerPoints,resourceDeck,goldDeck,card0,card1,card2,card3);
    }

    /**
     *
     * @throws RemoteException
     */
    @Override
    public synchronized void NotifyNumbersOfPlayersReached() throws RemoteException {
        viewInterface.NotifyNumbersOfPlayersReached();
        viewInterface.drawScene(SceneType.GAME);
    }

    /**
     *
     * @throws RemoteException
     */
    @Override
    public void NotifyLastRound() throws RemoteException {
        viewInterface.NotifyLastRound();
    }

    /**
     *
     * @param colors
     * @throws RemoteException
     */
    @Override
    public void notifyAvailableColors(ArrayList<Color> colors) throws RemoteException {
        viewInterface.notifyAvailableColors(colors);
    }

    /**
     *
     * @param colors
     * @throws RemoteException
     */
    @Override
    public void notifyFinalColors(Map<String, Color> colors) throws RemoteException {
        for(String p: colors.keySet())
            this.colors.add(colors.get(p));
        viewInterface.notifyFinalColors(colors, players);
    }

    /**
     *
     * @param nickname
     * @param chat
     * @param gameState
     * @param hand
     * @param objectiveCard
     * @param boards
     * @param points
     * @param players
     * @param objectiveCards
     * @param color
     * @param table
     * @param colors
     * @throws RemoteException
     */
    @Override
    public void UpdateCrashedPlayer(String nickname, ArrayList<Text> chat, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table, ArrayList<Color> colors) throws RemoteException {
        Map<String, Color> playerscolors=new HashMap<>();
        for(int i=0; i<players.size(); i++){
            playerscolors.put(players.get(i),colors.get(i));
        }
        this.current=nickname;
        this.chat=chat;
        this.gameState=gameState;
        this.hand=hand;
        this.objectiveCard1=objectiveCard;
        this.boards=boards;
        this.playerPoints=points;
        this.players=players;
        this.commonObjective=objectiveCards;
        this.resourceDeck=table.get(0);
        this.goldDeck=table.get(1);
        this.card0=table.get(2);
        this.card1=table.get(3);
        this.card2=table.get(4);
        this.card3=table.get(5);
        this.colors = colors;
        viewInterface.drawScene(SceneType.GAME);
        viewInterface.updateCommonTable(playerPoints,resourceDeck,goldDeck,card0,card1,card2,card3);
        viewInterface.notifyCommonObjective(commonObjective.get(0), commonObjective.get(1));
        this.NotifyChangePersonalCards(this.player,hand);
        this.notifyFinalColors(playerscolors);
        this.notifyChangeState(gameState);
        viewInterface.notifyCurrentPlayer(current,boards,player, hand, gameState);
        viewInterface.notifyTurnOrder(this.players, this.player);
        viewInterface.notifyChoiceObjective(objectiveCard);
        viewInterface.restoreChat(chat, this.player);
        viewInterface.UpdateCrashedPlayer(nickname, this.player, gameState, hand, objectiveCard, boards, points, players, objectiveCards, color, table);
        for(String player: this.players) {
            viewInterface.notifyChangePlayerBoard(player, this.player, this.boards);
            viewInterface.ReceiveUpdateOnPoints(player, playerPoints.get(player));
        }
        viewInterface.printNotifications("Rejoined successfully");
    }

    /**
     *
     * @param commons
     * @throws RemoteException
     */
    @Override
    public void UpdateFirst(ArrayList<ResourceCard> commons) throws RemoteException {
        this.resourceDeck=commons.get(0);
        this.goldDeck=commons.get(1);
        this.card0=commons.get(2);
        this.card1=commons.get(3);
        this.card2=commons.get(4);
        this.card3=commons.get(5);
        viewInterface.UpdateFirst(playerPoints,commons);
    }

    /**
     *
     * @param sender
     * @param text
     * @throws RemoteException
     */
    @Override
    public void ReceiveGroupText(String sender, String text)throws RemoteException {
        this.addGroupText(new Text(sender, text));
    }

    /**
     *
     * @param sender
     * @param receiver
     * @param text
     * @throws RemoteException
     */
    @Override
    public void ReceivePrivateText(String sender, String receiver, String text) throws RemoteException{
        this.addGroupText(new Text(sender, receiver,text));
    }

    /**
     *
     * @param msg
     */
    public synchronized void addGroupText(Text msg){
        chat.add(0, msg);
        viewInterface.addGroupText(chat, this.player);
    }

    /**
     *
     * @param message
     */
    public void printNotifications(String message) {
        viewInterface.printNotifications(message);
    }

}
