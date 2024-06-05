package it.polimi.ingsw.is24am03;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private ArrayList<String> times=new ArrayList<>();

    //array list per le carte da gioco comune
    //    //0--> resource deck
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
    private ViewInterface viewInterface;
    //posso usare client model per inoltrare alla view richieste da parte del client di vedere determinati
    //oggetti presenti

    //per mostrare alla view i messaggi della chat
    //il client socket comunica direttamente con la view nel caso siano semplici notifiche

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


    @Override
    public synchronized void  notifyJoinedPlayer(String joinedPlayer) throws RemoteException {
        viewInterface.notifyJoinedPlayer(joinedPlayer);
    }

    @Override
    public synchronized void notifyWinners(ArrayList<String> winners)throws RemoteException {
        viewInterface.notifyWinners(winners);
    }


    @Override
    public synchronized void  notifyTurnOrder(ArrayList<String> order) throws RemoteException{
        players=order;
        for(String s: order){
            playerPoints.put(s,0);
            boards.put(s,new PlayableCard[81][81]);
        }

        viewInterface.notifyTurnOrder(order);

    }


    @Override
    public synchronized void notifyCurrentPlayer(String current)throws RemoteException {
        this.current = current;
        viewInterface.notifyCurrentPlayer(current,boards,player, hand, gameState);

    }


    @Override
    public synchronized void notifyCrashedPlayer(String username)throws RemoteException {
        viewInterface.notifyCrashedPlayer(username);
    }


    @Override
    public synchronized void notifyChangeState(State gameState)throws RemoteException {
        this.gameState=gameState;
        //lo stato del gioco può essere starting, playing, drawing o ending

        //il problema è che se sono allo starting devo aspettare il current player

        //se lo stato cambia e sono ancora io il giocatore devo gestirla diversamente

        viewInterface.notifyChangeState(gameState);

    }


    @Override
    public synchronized void notifyRejoinedPlayer(String rejoinedPlayer)throws RemoteException{
        if(!rejoinedPlayer.equals(this.player))
            viewInterface.notifyRejoinedPlayer(rejoinedPlayer);
    }


    @Override
    public synchronized void notifyChangePlayerBoard(String player, PlayableCard p, int i, int j) throws RemoteException{
        //all'interno devo mettergli quello che c'era prima
        PlayableCard[][] tempBoard;
        tempBoard= boards.get(player);
        tempBoard[i][j]=p;
        if (i + 1 < 81 && j + 1 < 81 && tempBoard[i + 1][j + 1] != null) {
            tempBoard[i + 1][j + 1].setCornerCoverage(0, true); // Imposta come coperto l'angolo 0 della carta adiacente in basso a destra
        }
        if (i + 1 < 81 && j - 1 >= 0 && tempBoard[i + 1][j - 1] != null) {
            tempBoard[i + 1][j - 1].setCornerCoverage(1, true); // Imposta come coperto l'angolo 1 della carta adiacente in basso a sinistra
        }
        if (i - 1 >= 0 && j + 1 < 81 && tempBoard[i - 1][j + 1] != null) {
            tempBoard[i - 1][j + 1].setCornerCoverage(3, true); // Imposta come coperto l'angolo 3 della carta adiacente in alto a destra
        }
        if (i - 1 >= 0 && j - 1 >= 0 && tempBoard[i - 1][j - 1] != null) {
            tempBoard[i - 1][j - 1].setCornerCoverage(2, true); // Imposta come coperto l'angolo 2 della carta adiacente in alto a sinistra
        }
        boards.put(player, tempBoard);
        viewInterface.notifyChangePlayerBoard(player, this.player, boards);

    }


    @Override
    public synchronized void ReceiveUpdateOnPoints(String player, int points)throws RemoteException {
        playerPoints.put(player, points);
        viewInterface.ReceiveUpdateOnPoints(player, points);
    }


    @Override
    public synchronized  void NotifyChangePersonalCards(String Player, ArrayList<ResourceCard> p) throws RemoteException {
        hand=p;
        viewInterface.NotifyChangePersonalCards(p);
    }


    @Override
    public synchronized void notifyChoiceObjective(String player, ObjectiveCard o)throws RemoteException {
        objectiveCard1=o;
        viewInterface.notifyChoiceObjective(o);

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
        this.startingCard=startingCard;
        this.objectiveCard1=o1;
        this.objectiveCard2=o2;
       viewInterface.notifyFirstHand(hand,startingCard,o1,o2);
    }

    @Override
    public synchronized void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2)throws RemoteException{
        commonObjective.add(objectiveCard1);
        commonObjective.add(objectiveCard2);
      viewInterface.notifyCommonObjective(objectiveCard1,objectiveCard2);
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
        if(index==1 || index==0)
            viewInterface.updateCommonTable(playerPoints,resourceDeck,goldDeck,card0,card1,card2,card3);


    }

    @Override
    public synchronized void NotifyNumbersOfPlayersReached() throws RemoteException {
       // viewInterface.notify("Number of players has been reached, the game will start in a few moments");
        viewInterface.NotifyNumbersOfPlayersReached();
        viewInterface.drawScene(SceneType.COLOR);
    }

    @Override
    public void NotifyLastRound() throws RemoteException {
        viewInterface.NotifyLastRound();
    }

    @Override
    public void notifyAvailableColors(ArrayList<Color> colors) throws RemoteException {
        viewInterface.notifyAvailableColors(colors);
    }

    @Override
    public void notifyFinalColors(Map<String, Color> colors) throws RemoteException {
        viewInterface.notifyFinalColors(colors, players);
    }

    @Override
    public void UpdateCrashedPlayer(String nickname, ArrayList<Text> chat, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table) throws RemoteException {

        this.current=nickname; //
        this.chat=chat; //
        this.gameState=gameState; //
        this.hand=hand;
        this.objectiveCard1=objectiveCard;
        this.boards=boards; //
        this.playerPoints=points; //
        this.players=players; //
        this.commonObjective=objectiveCards;
        this.resourceDeck=table.get(0);
        this.goldDeck=table.get(1);
        this.card0=table.get(2);
        this.card1=table.get(3);
        this.card2=table.get(4);
        this.card3=table.get(5);
        viewInterface.UpdateCrashedPlayer(nickname, this.player, gameState, hand, objectiveCard, boards, points, players, objectiveCards, color, table);
    }

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
        viewInterface.addGroupText(chat, this.player);
    }
    public void printNotifications(String message) {
        viewInterface.printNotifications(message);
    }

}
