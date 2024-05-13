package it.polimi.ingsw.is24am03;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.rmi.RemoteException;
import java.util.ArrayList;

//client model esegue i metodi dei sub solo quando ho una connessione di tipo rmi
public class ClientModel implements ChatSub, GameSub, PlayerSub, PlayerBoardSub {


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

    private ArrayList<ResourceCard> commonTable;
    private ArrayList<ObjectiveCard> commonObjective;
    private String color;
    private Map<String, Boolean> playersState;

    private ArrayList<ResourceCard> hand;
    private ObjectiveCard objectiveCard;

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
        this.commonTable=new ArrayList<>();
        this.commonObjective=new ArrayList<>();
        this.playersState=new HashMap<>();
        this.hand=new ArrayList<>();
        this.objectiveCard=null;
        this.playerPoints=new HashMap<>();
        this.boards=new HashMap<>();
        this.players.add(player);

    }
//TODO togliere dai metodi che fanno override i throws remote exception


    @Override
    public void notifyJoinedPlayer(ArrayList<String> joinedPlayer)  {

    }

    @Override
    public void notifyWinners(ArrayList<String> winners) {

    }

    @Override
    public void notifyTurnOrder(ArrayList<String> order) {

    }

    @Override
    public void notifyCurrentPlayer(String current){

    }

    @Override
    public void notifyCrashedPlayer(String username) {

    }

    @Override
    public void notifyChangeState(State gameState) {
        this.gameState=gameState;


    }

    @Override
    public void notifyRejoinedPlayer(String rejoinedPlayer){

    }

    @Override
    public void notifyChangePlayerBoard(String player, PlayableCard p, int i, int j) {

    }

    @Override
    public void ReceiveUpdateOnPoints(String player, int points) {

    }

    @Override
    public void NotifyChangePersonalCards(String Player, ArrayList<ResourceCard> p)  {

    }

    @Override
    public void notifyChoiceObjective(String player, ObjectiveCard o) {

    }

    @Override
    public String getSub() {
        return this.player;
    }


    @Override
    public void notifyFirstHand(String player, ResourceCard p1, ResourceCard p2, ResourceCard p3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2){

    }

    @Override
    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2){

    }

    @Override
    public void updateCommonTable(ResourceCard resourceCard,int index) throws RemoteException {

    }

    @Override
    public void NotifyNumbersOfPlayersReached() throws RemoteException {

    }

    @Override
    public void NotifyLastRound() throws RemoteException {

    }

    @Override
    public void notifyAvailableColors(ArrayList<Color> colors) throws RemoteException {

    }

    @Override
    public void notifyFinalColors(Map<String, Color> colors) throws RemoteException {

    }


    @Override
    public void ReceiveGroupText(String sender, String text) {
        this.addGroupText(new Text(sender, text));
    }

    @Override
    public void ReceivePrivateText(String sender, String receiver, String text) {
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
