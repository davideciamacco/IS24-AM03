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
    public void notifyJoinedPlayer(String joinedPlayer) throws RemoteException {

    }

    @Override
    public void notifyWinners(ArrayList<String> winners)throws RemoteException {

    }

    @Override
    public void notifyTurnOrder(ArrayList<String> order) throws RemoteException{

    }

    @Override
    public void notifyCurrentPlayer(String current)throws RemoteException{

    }

    @Override
    public void notifyCrashedPlayer(String username)throws RemoteException {

    }

    @Override
    public void notifyChangeState(State gameState)throws RemoteException {
        this.gameState=gameState;


    }

    @Override
    public void notifyRejoinedPlayer(String rejoinedPlayer)throws RemoteException{

    }

    @Override
    public void notifyChangePlayerBoard(String player, PlayableCard p, int i, int j) throws RemoteException{

    }

    @Override
    public void ReceiveUpdateOnPoints(String player, int points)throws RemoteException {

    }

    @Override
    public void NotifyChangePersonalCards(String Player, ArrayList<ResourceCard> p) throws RemoteException {

    }

    @Override
    public void notifyChoiceObjective(String player, ObjectiveCard o)throws RemoteException {

    }

    @Override
    public String getSub()throws RemoteException {
        return this.player;
    }


    @Override
    public void notifyFirstHand(ResourceCard p1, ResourceCard p2, ResourceCard p3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2)throws RemoteException{

    }

    @Override
    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2)throws RemoteException{

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
