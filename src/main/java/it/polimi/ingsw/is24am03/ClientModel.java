package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerBoardSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.server.model.cards.*;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;

import java.io.Serial;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.rmi.RemoteException;
import java.util.ArrayList;

//client model esegue i metodi dei sub solo quando ho una connessione di tipo rmi
public class ClientModel extends UnicastRemoteObject implements ChatSub, GameSub, PlayerSub, PlayerBoardSub {


    @Serial
    private static final long serialVersionUID= -7805876932328376622L;
    private String player; //associata a un giocatore
    private ArrayList<Text> chat;


    //array list per le carte da gioco comune
    //  //0--> resource deck
    //    //1--> gold deck
    //    //2--> carta in posizione 0
    //    //3--> carta in posizione 1
    //    //4--> carta in posizione 2
    //    //5--> carta in posizione 3

   private ResourceCard resourceDeck;
   private ResourceCard goldDeck;

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

    //giocatori e punti
    private Map<String, Integer> playerPoints;

    //mappa le board dei giocatori
    private Map<String, PlayableCard[][]> boards;

    private ArrayList<String> players;

    private State gameState;

    //interfaccia utente grafica
    private CliView viewInterface;
    //posso usare client model per inoltrare alla view richieste da parte del client di vedere determinati
    //oggetti presenti

    //per mostrare alla view i messaggi della chat
    //il client socket comunica direttamente con la view nel caso siano semplici notifiche

    public ClientModel(String player, CliView viewInterface) throws RemoteException {
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

    }


    @Override
    public synchronized void  notifyJoinedPlayer(String joinedPlayer) throws RemoteException {
        viewInterface.notify(joinedPlayer+"  has joined the game");
    }

    @Override
    public synchronized void notifyWinners(ArrayList<String> winners)throws RemoteException {
       StringBuilder message = new StringBuilder();
       for(String s: winners){
           message.append(s).append("-");
       }
       message= new StringBuilder("winners are " + message);
       viewInterface.notify(message.toString());
    }

    @Override
    public synchronized void  notifyTurnOrder(ArrayList<String> order) throws RemoteException{
        for(String s : order){players.add(s);}
        StringBuilder message = new StringBuilder();
        for(String s: order){
            playerPoints.put(s,0);
            message.append(s).append("-");
            boards.put(s,new PlayableCard[81][81]);
        }
        message= new StringBuilder("Turn order is  " + message);

        viewInterface.notify(message.toString());

    }

    @Override
    public synchronized void notifyCurrentPlayer(String current)throws RemoteException {
        this.current = current;
        viewInterface.notify("current player is " + current);
        if (current.equals(player)) {
            if (gameState.equals(State.OBJECTIVE)) {
                viewInterface.notify("\nOption 1:\n");
                viewInterface.drawObjective(objectiveCard1);
                viewInterface.notify("\nOption 2:\n");
                viewInterface.drawObjective(objectiveCard2);
            } else if (gameState.equals(State.STARTING)) {
                viewInterface.drawStarting(startingCard);
            } else if (gameState.equals(State.PLAYING)) {
                viewInterface.drawBoard(boards.get(current));
            }
        }

    }


    @Override
    public synchronized void notifyCrashedPlayer(String username)throws RemoteException {
        viewInterface.notify("player "+username+" has crashed");
    }

    @Override
    public synchronized void notifyChangeState(State gameState)throws RemoteException {
        this.gameState=gameState;
        viewInterface.notify("Game state has changed, now is "+gameState.toString());


    }

    @Override
    public synchronized void notifyRejoinedPlayer(String rejoinedPlayer)throws RemoteException{
        viewInterface.notify(rejoinedPlayer+" has joined the game");
    }

    @Override
    public synchronized void notifyChangePlayerBoard(String player, PlayableCard p, int i, int j) throws RemoteException{
        //all'interno devo mettergli quello che c'era prima
        PlayableCard[][] tempBoard;
        tempBoard= boards.get(player);
        tempBoard[i][j]=p;
        boards.put(player, tempBoard);
        viewInterface.notify(player + " placed a card");
        if(player.equals(this.player)){

        }
    }

    @Override
    public synchronized void ReceiveUpdateOnPoints(String player, int points)throws RemoteException {
        playerPoints.put(player, points);
        viewInterface.notify("Update on "+player+" points! He reached"+points);
        //viewinterface.drawTable
    }

    @Override
    public synchronized  void NotifyChangePersonalCards(String Player, ArrayList<ResourceCard> p) throws RemoteException {
       hand=p;
       viewInterface.drawHand(p);
    }

    @Override
    public synchronized void notifyChoiceObjective(String player, ObjectiveCard o)throws RemoteException {
        objectiveCard1=o;
        viewInterface.notify("Your personal objective is: ");
        viewInterface.drawObjective(o);

    }

    @Override
    public synchronized String getSub()throws RemoteException {
        return this.player;
    }


    @Override
    public synchronized void notifyFirstHand(ResourceCard p1, ResourceCard p2, ResourceCard p3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2)throws RemoteException{
        hand.add(p1);
        hand.add(p2);
        hand.add(p3);
        viewInterface.drawHand(hand);
        this.startingCard=startingCard;
        this.objectiveCard1=o1;
        this.objectiveCard2=o2;
        viewInterface.drawStarting(startingCard);
        viewInterface.drawObjective(o1);
        viewInterface.drawObjective(o2);
    }

    @Override
    public synchronized void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2)throws RemoteException{
        commonObjective.add(objectiveCard1);
        commonObjective.add(objectiveCard2);
        viewInterface.drawObjective(objectiveCard1);
        viewInterface.drawObjective(objectiveCard2);
    }

    @Override
    public synchronized void updateCommonTable(ResourceCard resourceCard,int index) throws RemoteException {
        //0--> resource deck
        //1--> gold deck
        //2--> carta 0
        //3--> carta 1
        //4-->carta 2
        //5--> carta 3
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
        if(gameState!=State.STARTING){
            viewInterface.drawTable(playerPoints,resourceDeck,goldDeck,card0,card1,card2,card3);
        }

    }

    @Override
    public synchronized void NotifyNumbersOfPlayersReached() throws RemoteException {
        viewInterface.notify("Number of players has been reached, the game will start in a few moments");
    }

    @Override
    public void NotifyLastRound() throws RemoteException {
        viewInterface.notify("Last round will start, during this round drawing won't be permitted");
    }

    @Override
    public void notifyAvailableColors(ArrayList<Color> colors) throws RemoteException {
        viewInterface.drawAvailableColors(colors);
    }

    @Override
    public void notifyFinalColors(Map<String, Color> colors) throws RemoteException {

    }

    @Override
    public void UpdateCrashedPlayer(String nickname, ArrayList<Text> chat, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Map<String, Color> colors, ArrayList<ResourceCard> table) throws RemoteException {

    }


    @Override
    public void ReceiveGroupText(String sender, String text)throws RemoteException {
        this.addGroupText(new Text(sender, text));
    }

    @Override
    public void ReceivePrivateText(String sender, String receiver, String text) throws RemoteException{
        this.addGroupText(new Text(sender, receiver,text));
    }
    //metodo per aggiungere messaggio alla chat generale
    public synchronized void addGroupText(Text msg){
        chat.add(0, msg);
        //sono ordinati in ordine lifo
        List<String> messages= this.chat
                .stream()
                .map(
                        m->m.getRecipient().isEmpty() ?
                                m.getSender() + ": " + m.getMex() :
                                msg.getSender() + "(secretly sent to " + msg.getRecipient() +"):"+ msg.getMex()).toList();
        //view.drawChat(messages);

    }


}
