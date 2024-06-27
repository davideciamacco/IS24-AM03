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
 * ClientModel is a class which handles the logic of the game and its status on client side
 */
public class ClientModel extends UnicastRemoteObject implements ChatSub, GameSub, PlayerSub, PlayerBoardSub {
    @Serial
    private static final long serialVersionUID= -7805876932328376622L;
    /**
     * Nickname of the player associated with the ClientModel
     */
    private final String player;
    /**
     * Chronologically ordered list of message in the player chat. The first one is the newest text.
     */
    private ArrayList<Text> chat;
    /**
     * Card which refers to the resource deck in the common table
     */
    private ResourceCard resourceDeck;
    /**
     * Card which refers to the gold deck in the common table
     */
    private ResourceCard goldDeck;
    /**
     * List of colors chosed by the players, ordered by the turn order of the game
     */
    private ArrayList<Color> colors = new ArrayList<>();
    /**
     * Reference to the first face-up card on the game table
     */
    private ResourceCard card0;
    /**
     * Reference to the second face-up card on the game table
     */
    private ResourceCard card1;
    /**
     * Reference to the third face-up card on the game table
     */
    private ResourceCard card2;
    /**
     * Reference to the fourth face-up card on the game table
     */
    private ResourceCard card3;

    /**
     * List which contains the common objectives of the game
     */
    private ArrayList<ObjectiveCard> commonObjective;
    /**
     * Reference to the current player
     */
    private String current;
    /**
     * Reference to the personal cards of the player
     */
    private ArrayList<ResourceCard> hand;
    /**
     * Reference of the first objective card assigned to the player
     */
    private ObjectiveCard objectiveCard1;

    /**
     * Reference of the second objective card assigned to the player
     */
    private ObjectiveCard objectiveCard2;

    /**
     * Reference of the starting card assigned to the player
     */
    private StartingCard startingCard;
    /**
     * Map to contain the score of each player
     */
    private Map<String, Integer> playerPoints;
    /**
     * Map to contain the board of each player
     */
    private Map<String, PlayableCard[][]> boards;
    /**
     * Ordered list of the players in the game, arranged by the turn order of the game
     */
    private ArrayList<String> players;
    /**
     * The current state of the game
     */
    private State gameState;
    /**
     * Reference to the type of view chosen by the client, GUI or CLI
     */
    private final ViewInterface viewInterface;

    /**
     * Create a new local model to keep trace of the state of the game for a single client
     * @param player refers to the player in the game
     * @param viewInterface refers to the type of interface
     * @throws RemoteException RMI Exception
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
     * Notifies a player joined the game
     * @param joinedPlayer player who joined the game
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void  notifyJoinedPlayer(String joinedPlayer) throws RemoteException {
        viewInterface.notifyJoinedPlayer(joinedPlayer);
    }

    /**
     * Notifies who the winners of the game are
     * @param winners contains the winners of the game
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void notifyWinners(ArrayList<String> winners)throws RemoteException {
        viewInterface.notifyWinners(winners);
    }

    /**
     * Notifies on the turn order of the game
     * @param order contains the turn order of the game
     * @throws RemoteException RMI Exception
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
     * Updates the current player of the game
     * @param current nickname of the current player
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void notifyCurrentPlayer(String current)throws RemoteException {
        this.current = current;
        viewInterface.notifyCurrentPlayer(current,boards,player, hand, gameState);

    }

    /**
     * Notifies that a player crashed
     * @param username the nickname of the player that crashed
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void notifyCrashedPlayer(String username)throws RemoteException {
        viewInterface.notifyCrashedPlayer(username);
    }

    /**
     * Updates the current status of the game
     * @param gameState the new state of the game
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void notifyChangeState(State gameState)throws RemoteException {
        this.gameState=gameState;
        viewInterface.notifyChangeState(gameState);
    }

    /**
     * Notifies that a player who crashed before has rejoined the game
     * @param rejoinedPlayer the nickname of the rejoined player
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void notifyRejoinedPlayer(String rejoinedPlayer)throws RemoteException{
        if(!rejoinedPlayer.equals(this.player))
            viewInterface.notifyRejoinedPlayer(rejoinedPlayer);
    }

    /**
     * Updates the personal board a player who placed a card
     * @param player the nickname of the player who placed a card
     * @param p the card placed by the player
     * @param i refers to the row of the matrix representing the player's board in which the card has been placed
     * @param j refers to the column of the matrix representing the player's board in which the card has been placed
     * @throws RemoteException RMI Exception
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
     * Updates the points of a player
     * @param player the player which points are updated
     * @param points the overall points of the player
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void ReceiveUpdateOnPoints(String player, int points)throws RemoteException {
        playerPoints.put(player, points);
        viewInterface.ReceiveUpdateOnPoints(player, points);
        viewInterface.printUpdatedPoints(playerPoints);
    }

    /**
     * Updated the player's personal cards
     * @param Player player assigned to this ClientModel
     * @param updatedCards list of the updated personal cards
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized  void NotifyChangePersonalCards(String Player, ArrayList<ResourceCard> updatedCards) throws RemoteException {
        hand=updatedCards;
        viewInterface.NotifyChangePersonalCards(updatedCards);
    }

    /**
     * Updates the personal objective card after the choiche made by this player
     * @param player player assigned to this ClientModel
     * @param objectiveCard secret objective chose by the player
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void notifyChoiceObjective(String player, ObjectiveCard objectiveCard)throws RemoteException {
        objectiveCard1=objectiveCard;
        viewInterface.notifyChoiceObjective(objectiveCard);

    }

    /**
     * Provides the nickname associated to the subscriber
     * @return the nickname of the player subscribed
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized String getSub()throws RemoteException {
        return this.player;
    }

    /**
     * Updates the cards assigned to the player.
     * @param p1 refers to the first card in the player's hand
     * @param p2 refers to the second card in the player's hand
     * @param p3 refers to the third card in the player's hand
     * @param startingCard refers to the starting card assigned to the player, he will have to choose the side
     * @param o1 refers to the first objective card assigned to the player
     * @param o2 refers to the second objective card assigned to the player
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void notifyFirstHand(ResourceCard p1, ResourceCard p2, ResourceCard p3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2)throws RemoteException{
        this.hand.add(p1);
        this.hand.add(p2);
        this.hand.add(p3);
        this.startingCard=startingCard;
        this.objectiveCard1=o1;
        this.objectiveCard2=o2;
        viewInterface.notifyFirstHand(hand,startingCard,o1,o2);
    }

    /**
     * Updates the common objectives of the game
     * @param objectiveCard1 first common objective card
     * @param objectiveCard2 secondo common objective card
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2)throws RemoteException{
        commonObjective.add(objectiveCard1);
        commonObjective.add(objectiveCard2);
        viewInterface.notifyCommonObjective(objectiveCard1,objectiveCard2);
    }

    /**
     * Updates the common table after a player draws a card
     * @param resourceCard updated card on top of a deck
     * @param index specifies which deck is updated, 0 refers to the Resource Deck, 1 to the Gold Deck and 2 to 5 refer to the four cards in the common table
     * @throws RemoteException RMI Exception
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
     * Notifies the local player who was waiting for the game to start, that the required number of players has been reached
     * @throws RemoteException RMI Exception
     */
    @Override
    public synchronized void NotifyNumbersOfPlayersReached() throws RemoteException {
        viewInterface.NotifyNumbersOfPlayersReached();
        viewInterface.drawScene(SceneType.GAME);
    }

    /**
     * Notifies the local player that the last round is starting
     * @throws RemoteException RMI Exception
     */
    @Override
    public void NotifyLastRound() throws RemoteException {
        viewInterface.NotifyLastRound();
    }

    /**
     * Notifies the local player about the colors he can choose from
     * @param colors the available colors
     * @throws RemoteException RMI Exception
     */
    @Override
    public void notifyAvailableColors(ArrayList<Color> colors) throws RemoteException {
        viewInterface.notifyAvailableColors(colors);
    }

    /**
     * Saves the colors chosen by the players
     * @param colors map that associates a player to his chosen color
     * @throws RemoteException RMI Exception
     */
    @Override
    public void notifyFinalColors(Map<String, Color> colors) throws RemoteException {
        for(String p: colors.keySet())
            this.colors.add(colors.get(p));
        viewInterface.notifyFinalColors(colors, players);
    }

    /**
     * Updates the player who rejoined the game after crashing.
     * @param nickname the current player
     * @param chat text messages from the group chat and text messages of which the player was the sender or the receiver
     * @param gameState current game state
     * @param hand player's personal cards
     * @param objectiveCard player's secret objective card
     * @param boards updated boards of each player
     * @param points updated score of each player
     * @param players turn order of the game
     * @param objectiveCards common objective cards
     * @param color color of the local player
     * @param table contains the cards located in the common table
     * @param colors color of each player associated to his nickname
     * @throws RemoteException RMI Exception
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
     * Notifies at the beginning of the game on the common cards
     * @param commons contains
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
     * Notifies the local player that another player wrote a text in the group chat
     * @param sender the nickname of the playe who sent the text
     * @param text the contents of the text
     * @throws RemoteException RMI Exception
     */
    @Override
    public void ReceiveGroupText(String sender, String text)throws RemoteException {
        this.addGroupText(new Text(sender, text));
    }

    /**
     * Notifies the local player that he received a private chat message
     * @param sender the nickname of the player who sent the text
     * @param receiver the nickname of the recipient
     * @param text the contents of the text
     * @throws RemoteException RMI Exception
     */
    @Override
    public void ReceivePrivateText(String sender, String receiver, String text) throws RemoteException{
        this.addGroupText(new Text(sender, receiver,text));
    }

    /**
     * Adds a new text message to the player's chat
     * @param msg the text received
     */
    public synchronized void addGroupText(Text msg){
        chat.add(0, msg);
        viewInterface.addGroupText(chat, this.player);
    }

    /**
     * Notifies the view about a new notification
     * @param message details of the notification
     */
    public void printNotifications(String message) {
        viewInterface.printNotifications(message);
    }

}
