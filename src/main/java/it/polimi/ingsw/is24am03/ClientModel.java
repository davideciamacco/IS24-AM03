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
           players=order;
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
        if(gameState==State.PLAYING){
            if(current.equals(player)){
                viewInterface.drawBoard(boards.get(player));
                viewInterface.drawHand(this.hand);
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
        if(!rejoinedPlayer.equals(this.player))
            viewInterface.notify(rejoinedPlayer+" has rejoined the game");
    }

    @Override
    public synchronized void notifyChangePlayerBoard(String player, PlayableCard p, int i, int j) throws RemoteException{

        //all'interno devo mettergli quello che c'era prima
        PlayableCard[][] tempBoard;
        tempBoard= boards.get(player);
        tempBoard[i][j]=p;

        boards.put(player, tempBoard);

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

        if(player.equals(this.player)){
            System.out.println("You placed a card successfully\n");
            viewInterface.drawBoard(boards.get(player));
        }
        else{
            viewInterface.notify("*************** " + player + " PLACED A CARD! HERE IS HIS/HER BOARD **************+ ");
            viewInterface.drawBoard(boards.get(player));
        }

    }

    @Override
    public synchronized void ReceiveUpdateOnPoints(String player, int points)throws RemoteException {
        playerPoints.put(player, points);
        viewInterface.notify("Update on "+player+" points! He/She reached "+ points +" points!");
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
        System.out.println("\n");
        System.out.println("--PERSONAL OBJECTIVES---\n");
        viewInterface.drawObjective(o1);
       viewInterface.drawObjective(o2);
        System.out.println("---END PERSONAL OBJECTIVES---\n");
    }

    @Override
    public synchronized void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2)throws RemoteException{
        commonObjective.add(objectiveCard1);
        commonObjective.add(objectiveCard2);
        System.out.println("--COMMON OBJECTIVES---");
        viewInterface.drawObjective(objectiveCard1);
        viewInterface.drawObjective(objectiveCard2);
        System.out.println("---END COMMON OBJECTIVES---");
        System.out.println("\n");
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
            viewInterface.drawTable(playerPoints,resourceDeck,goldDeck,card0,card1,card2,card3);


    }

    @Override
    public synchronized void NotifyNumbersOfPlayersReached() throws RemoteException {
        viewInterface.notify("Number of players has been reached, the game will start in a few moments");
        viewInterface.drawScene(SceneType.GAME);
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

        String ANSI_RESET = "\033[0m";

         String ANSI_RED = "\033[0;31m";
        String ANSI_GREEN = "\033[0;32m";
         String ANSI_YELLOW = "\033[0;33m";
      String ANSI_BLUE = "\033[0;34m";
        String square = "\u25A0";
        System.out.println("--------FINAL COLORS-------");
        for(String p: players){
           if(colors.get(p).equals(Color.BLUE)){
               System.out.println( p + ":" + ANSI_BLUE  + square + ANSI_RESET);
           }
            if(colors.get(p).equals(Color.RED)){
                System.out.println( p + ":" + ANSI_RED  + square + ANSI_RESET);
            }
            if(colors.get(p).equals(Color.GREEN)){
                System.out.println( p + ":" + ANSI_GREEN  + square + ANSI_RESET);
            }
            if(colors.get(p).equals(Color.YELLOW)){
                System.out.println( p + ":" + ANSI_YELLOW  + square + ANSI_RESET);
            }
        }


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

        //gli mostro a video il giocatore corrente
        System.out.println("---------------------------------------------------------------------");
        System.out.println("HERE ARE SOME UPDATES YOU MIGHT'VE MISSED WHILE GONE!");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("CURRENT PLAYER: "+ nickname);
        StringBuilder message = new StringBuilder();
        for(String s: players){
            message.append(s).append("-");
        }
        message= new StringBuilder("TURN ORDER IS " + message);
        viewInterface.notify(message.toString());
        System.out.println("GAME NOW IS IN "+gameState + " STATE");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("OTHER PLAYERS' BOARDS, TAKE A PEEK!");
        for(String s: players){
            if(!s.equals(this.player)){
                System.out.println("THIS IS "+ s + " BOARD" + " WHO SCORED SO FAR " + playerPoints.get(s));
                viewInterface.drawBoard(boards.get(s));
                System.out.println("---------------------------------------------------------------------");
            }
        }
        viewInterface.drawTable(playerPoints,resourceDeck,goldDeck,card0,card1,card2,card3);
        System.out.println("--COMMON OBJECTIVES---");
        viewInterface.drawObjective(objectiveCards.get(0));
        viewInterface.drawObjective(objectiveCards.get(1));
        System.out.println("---END COMMON OBJECTIVES---");
        viewInterface.drawHand(hand);
        System.out.println("YOUR PERSONAL OBJECTIVE IS:");
        viewInterface.drawObjective(objectiveCard);
        System.out.println("---------------------------------------------------------------------");



    }

    @Override
    public void UpdateFirst(ArrayList<ResourceCard> commons) throws RemoteException {

        this.resourceDeck=commons.get(0);
        this.goldDeck=commons.get(1);
        this.card0=commons.get(2);
        this.card1=commons.get(3);
        this.card2=commons.get(4);
        this.card3=commons.get(5);
        viewInterface.drawTable(playerPoints,resourceDeck,goldDeck,card0,card1,card2,card3);
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
        //se text è pubblico allora disegno gli ultimi 2 messaggi
        //devo disegnare dalla fine
       // LocalDateTime currentTime = LocalDateTime.now();

        // Definisci il formato desiderato per l'orario
       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format l'orario corrente
        //String formattedTime = currentTime.format(formatter);

        //times.add(0,formattedTime);
        // Stampa l'orario corrente

        if(msg.getRecipient()==null){
            //stampo tutti i messaggi precedenti se ce ne sono, che abbiano receiver nullo
            if(findNumber(chat)>=2){
                System.out.println("----PREVIOUS GROUP CHAT MESSAGES----");
            }
            for(int i=chat.size()-1; i>0; i--){
                if(chat.get(i).getSender().equals(this.player) && chat.get(i).getRecipient()==null){
                    System.out.println(chat.get(i).getSender() + " (YOU) : " + chat.get(i).getMex());

                }
                else {
                    if(chat.get(i).getRecipient()==null){
                    System.out.println( chat.get(i).getSender() + " : " + chat.get(i).getMex());}

                }
            }
            if(findNumber(chat)>=2){
                System.out.println("************************************");
            }
            System.out.println("----NEW GROUP CHAT MESSAGE----");
            if(chat.get(0).getSender().equals(this.player)){
                System.out.println("YOU: " + chat.get(0).getMex());
                System.out.println("************************************");
            }
            else{
                System.out.println(chat.get(0).getSender() + " : " + chat.get(0).getMex());
                System.out.println("************************************");
            }
        }
        else{
            if(msg.getSender().equals(this.player)){
                //trovo tutti i messaggi scambiati con il player destinatario
                System.out.println("YOU SENT A TEXT TO "+ msg.getRecipient() + "THIS IS YOUR PRIVATE CHAT WITH: " + msg.getRecipient());
                System.out.println("************************************");
                for(int i=chat.size()-1; i>=0; i--){
                    //messaggi che io ho mandato a lui
                    if(chat.get(i).getSender().equals(this.player) && chat.get(i).getRecipient().equals(msg.getRecipient())){
                    System.out.println("YOU " + " : " + chat.get(i).getMex() );}
                    //messaggi che lui ha mandato a me
                    else if(chat.get(i).getSender().equals(msg.getRecipient()) && chat.get(i).getRecipient().equals(this.player)){
                        System.out.println( chat.get(i).getSender() + " : " + chat.get(i).getMex() );}
                    }
                }

            if(msg.getRecipient().equals(this.player)) {
                //stampo tutti i loro vecchi messaggi
                System.out.println("YOU HAVE A NEW TEXT FROM " + msg.getSender() + " THIS IS YOUR PRIVATE CHAT WITH: " + msg.getSender());
                System.out.println("************************************");
                for(int i=chat.size()-1; i>=0; i--){
                    //messa che io ho mandato a lui
                    if(chat.get(i).getSender().equals(this.player) && chat.get(i).getRecipient().equals(msg.getSender())){
                        System.out.println("YOU " + " : " + chat.get(i).getMex());}
                    //mess che lui ha mandato a me
                    else if(chat.get(i).getSender().equals(msg.getSender()) && chat.get(i).getRecipient().equals(this.player)){
                        System.out.println( chat.get(i).getSender() + " : " + chat.get(i).getMex());}
                }

            }
        }



    }

    private int findNumber(ArrayList<Text> chat){

        int number=0;
        for(Text t: chat){
            if(t.getRecipient()==null){
                number++;
            }
        }
        return number;


    }




}
