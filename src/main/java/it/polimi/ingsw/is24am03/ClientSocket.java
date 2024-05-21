package it.polimi.ingsw.is24am03;
import it.polimi.ingsw.is24am03.messages.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientSocket implements Client{
    private boolean hasJoined;
    private ClientModel clientModel;
    private final String ip;
    private final int port;
    private final Socket connection;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private final Queue<Message> queueMessages;
    private final ExecutorService threadManager;
    private final ViewInterface view;

    private String nickname;

    public ClientSocket(String ip, int port, ViewInterface view) {
        this.ip = ip;
        this.port = port;
        this.queueMessages = new ArrayDeque<>();
        this.threadManager = Executors.newCachedThreadPool();
        this.view = view;
        this.hasJoined = false;

        try {
            this.connection = new Socket(ip, port);
            this.outputStream = new ObjectOutputStream(connection.getOutputStream());
            this.inputStream = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Connection Failed! Please restart the game");
        }
        this.messagesReceiver();
        this.ParserAgent();
    }

    public void CreateGame(int nPlayers, String nickname) {
        CreateGameMessage requestMessage = new CreateGameMessage(nPlayers, nickname);
        this.nickname=nickname;
        try{
            clientModel = new ClientModel(nickname, view);
        }
        catch(RemoteException e){

        }
        this.sendMessage(requestMessage);
    }

    public void JoinGame(String nickname){
        JoinGameMessage joinMessage = new JoinGameMessage(nickname, hasJoined);
        this.nickname = nickname;
        try{
        clientModel = new ClientModel(nickname, view);
        }
        catch(RemoteException e){}
        this.sendMessage(joinMessage);
    }

    @Override
    public void PickColor(String color) {
        PickColorMessage colorMessage = new PickColorMessage(nickname, color);
        this.sendMessage(colorMessage);
    }

    public void ChooseStartingCardSide(String face){
        ChooseStartingMessage startingMessage= new ChooseStartingMessage(nickname,face);
        this.sendMessage(startingMessage);
    }

    public void PlaceCard(int choice, int i, int j, String face){
        PlaceCardMessage placeCardMessage = new PlaceCardMessage(nickname, choice, i, j, face);
        this.sendMessage(placeCardMessage);
    }

    public void DrawGold(){
        DrawGoldMessage drawGoldMessage = new DrawGoldMessage(nickname);
        this.sendMessage(drawGoldMessage);
    }

    public void DrawResource(){
        DrawResourceMessage drawResourceMessage = new DrawResourceMessage(nickname);
        this.sendMessage(drawResourceMessage);
    }

    public void DrawTable(int choice){
        DrawTableMessage drawTableMessage = new DrawTableMessage(nickname, choice);
        this.sendMessage(drawTableMessage);
    }

    public void ChooseObjectiveCard(int choice){
        ChooseObjectiveMessage chooseObjectiveMessage = new ChooseObjectiveMessage(nickname, choice);
        this.sendMessage(chooseObjectiveMessage);
    }

    @Override
    public void sendGroupText(String text) {
        GroupChatMessage groupChatMessage= new GroupChatMessage(nickname, text);
        this.sendMessage(groupChatMessage);
    }

    @Override
    public void sendPrivateText(String receiver, String text) {
        PrivateChatMessage privateChatMessage= new PrivateChatMessage(nickname, receiver, text);
        this.sendMessage(privateChatMessage);
    }

    public void RejoinGame(String player_name){
        RejoinGameMessage rejoinGameMessage = new RejoinGameMessage(player_name);
        this.nickname = player_name;
        try{
            clientModel = new ClientModel(player_name, view);
        }
        catch(RemoteException e){}
        this.sendMessage(rejoinGameMessage);
    }


    private void messagesReceiver()  {
        threadManager.execute( () -> {
            boolean active = true;
            while(active) {
                synchronized (queueMessages) {
                    try {
                        Message incomingMessage = (Message) inputStream.readObject();
                        queueMessages.add(incomingMessage);
                        queueMessages.notifyAll();
                        queueMessages.wait(1);
                    } catch (IOException | ClassNotFoundException | InterruptedException e ) {
                        e.printStackTrace();
                        active = false;
                    }
                }
            }
        });
    }


    private void ParserAgent(){
        threadManager.execute( () -> {
            while(this.connection.isConnected()){
                synchronized (queueMessages){
                    while(queueMessages.isEmpty()){
                        try {
                            queueMessages.notifyAll();
                            queueMessages.wait();
                        } catch (InterruptedException ignored) {}
                    }
                    this.parse(queueMessages.poll());
                    //System.out.println("Client riceve da server");
                }
            }
        });
    }

    private void parse(Message responseMessage){

    //    if(responseMessage == null) return;

        switch (responseMessage.getMessageType()){

            //MESSAGGI DI CONFERMA
            case CONFIRM_GAME -> this.parse((ConfirmGameMessage) responseMessage);
            case CONFIRM_JOIN -> this.parse((ConfirmJoinGameMessage) responseMessage);
            case CONFIRM_PICK -> this.parse((ConfirmPickColorMessage) responseMessage);
            case CONFIRM_CHOOSE_SIDE -> this.parse((ConfirmStartingCardMessage) responseMessage);
            case CONFIRM_CHOOSE_OBJECTIVE -> this.parse((ConfirmChooseObjectiveMessage) responseMessage);
            case CONFIRM_PLACE -> this.parse((ConfirmPlaceMessage) responseMessage);
            case CONFIRM_DRAW -> this.parse((ConfirmDrawMessage) responseMessage);
            case CONFIRM_REJOIN -> this.parse((ConfirmRejoinGameMessage) responseMessage);

            ///////

            //MESSAGGI UPDATE DEL GIOCO (INTESO COME COMMON TABLE, STATI ETC)
            //TUTTI I MESSAGGI DI UPDATE DI GAME SONO BROADCAST

            case UPDATE_COMMON_TABLE -> this.parse((NotifyCommonTableMessage)responseMessage);
            case COMMON_OBJECTIVE-> this.parse((CommonObjectiveMessage) responseMessage);
            case NOTIFY_WINNERS-> this.parse((WinnersMessage) responseMessage);
            case JOINED_PLAYER-> this.parse((JoinedPlayerMessage) responseMessage);
            case NOTIFY_REJOINED_PLAYER-> this.parse((RejoinedPlayerMessage) responseMessage);
            case NOTIFY_CURRENT_PLAYER -> this.parse((CurrentPlayerMessage) responseMessage);
            case TURN_ORDER-> this.parse((TurnOrderMessage) responseMessage);
            case GAME_STATE-> this.parse((ChangeStateMessage) responseMessage);
            //questo notifica a quelli in gioco che un certo player è crashato
            case NOTIFY_CRASHED_PLAYER-> this.parse((CrashedPlayerMessage) responseMessage);

            //MESSAGGI DI UPDATE RIGUARDANTI UN GIOCATORE

            //BROADCAST MESSAGE//

            case UPDATE_POINTS-> this.parse((UpdatePointsMessage) responseMessage);
            //PRIVATE MESSAGES//
            case UPDATE_PERSONAL_CARDS-> this.parse((PersonalCardsMessage) responseMessage);
            //message choice objective is sent after the player chose his secret objective
            case UPDATE_PERSONAL_OBJECTIVE-> this.parse((ChoiceObjectiveMessage) responseMessage);
            //first-hand message contains first hand of the player + objective cards+ starting card
            case FIRST_HAND-> this.parse((FirstHandMessage) responseMessage);
            //MESSAGGIO DI UPDATE PER IL PLAYER CRASHATO RITORNATO IN GIOCO//
            case UPDATE_CRASHED_PLAYER -> this.parse((UpdateCrashedPlayerMessage)responseMessage);
            //PLAYER BOARD MESSAGE, E' UN MESSAGGIO BROADCAST//
            case UPDATE_PLAYER_BOARD-> this.parse((PlayerBoardMessage) responseMessage);
            //CHAT//
            case GROUP_CHAT-> this.parse((GroupChatMessage) responseMessage);
            case PRIVATE_CHAT-> this.parse((PrivateChatMessage) responseMessage);
            case CONFIRM_CHAT -> this.parse((ConfirmChatMessage)responseMessage);

            case FIRST_COMMON -> this.parse((StartingCommonMessage)responseMessage);
            case AVAILABLE_COLORS -> this.parse((AvailableColorMessage) responseMessage);
            case FINAL_COLORS -> this.parse((FinalColorsMessage)responseMessage);
            case NOTIFY_ADDITIONAL_ROUND -> this.parse((LastRoundMessage)responseMessage);
            case NOTIFY_NUM_PLAYERS_REACHED -> this.parse((NotifyNumPlayersReachedMessage)responseMessage);
            default -> {
            }
        }
    }

    //TUTTE LE NOTIFICHE DI UPDATE VERRANNO POI GESTITE CON METODI CHIAMATI SUL LOCAL MODEL /

    private void parse(LastRoundMessage lastRoundMessage){
        try{
            this.clientModel.NotifyLastRound();
        }catch (RemoteException e){}
    }
    private void parse(FinalColorsMessage response){
        try{
            this.clientModel.notifyFinalColors(response.getColors());
        }catch (RemoteException e){}
    }
    private void parse(StartingCommonMessage response){
        try{
            this.clientModel.UpdateFirst(response.getCommons());
        }catch (RemoteException e){}
    }

    private void parse(AvailableColorMessage response){
        try {
            this.clientModel.notifyAvailableColors(response.getColors());
        }catch(RemoteException e){}
    }

    private void parse(NotifyNumPlayersReachedMessage response){
        try
        {
            this.clientModel.NotifyNumbersOfPlayersReached();
        }catch(RemoteException e){}
    }

    private void parse(ConfirmDrawMessage message) {
        if (message.getconfirmdraw()){
            System.out.println("Card drawn successfully");
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }
    private void parse(ConfirmPlaceMessage message) {
        if (!message.getConfirmPlace()) {
            System.out.println(message.getDetails());
        }
        System.out.flush();
    }

    private void parse(ConfirmGameMessage message) {
        if (message.getConfirmGameCreation()){
            this.nickname = nickname;
            System.out.println("Game created successfully");
            //qui creo il local model
            /*try {
                this.clientModel = new ClientModel(this.nickname, view);
            }catch (RemoteException e){}*/
            hasJoined = true;
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }
    private void parse(ConfirmChooseObjectiveMessage message) {
        if (message.getConfirmChoose()){
            System.out.println("Objective card selected successfully");
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }
    private void parse(NotifyCommonTableMessage message) {
        try {
            this.clientModel.updateCommonTable(message.getResourceCard(), message.getIndex());
        }catch (RemoteException e){}
    }
    private void parse(CommonObjectiveMessage responseMessage){
        try {
            this.clientModel.notifyCommonObjective(responseMessage.getObjectiveCard1(), responseMessage.getObjectiveCard2());
        } catch (RemoteException e) {

        }
    }
    private void parse(WinnersMessage response){
        try {
            this.clientModel.notifyWinners(response.getWinners());
        } catch (RemoteException e) {

        }
    }

    private void parse(ConfirmRejoinGameMessage response){
        if(response.getConfirmRejoin())
            System.out.println("Rejoined successfully");
        else
            System.out.println(response.getDetails());
    }

    private void parse(JoinedPlayerMessage response){
        try {
            this.clientModel.notifyJoinedPlayer(response.getPlayer());
        } catch (RemoteException e) {

        }

    }
    private void parse (RejoinedPlayerMessage response){
        try {
            this.clientModel.notifyRejoinedPlayer(response.getPlayer());
        } catch (RemoteException e) {

        }
       // System.out.println(response.getPlayer()+"has rejoined the game");
    }

    private void parse(CurrentPlayerMessage response){
        try {
            this.clientModel.notifyCurrentPlayer(response.getCurrentPlayer());
        } catch (RemoteException e) {

        }
    }

    private void parse(TurnOrderMessage response){
        try {
            this.clientModel.notifyTurnOrder(response.getTurnOrder());
        } catch (RemoteException e) {

        }
    }

    private void parse(ChangeStateMessage response){
        try {
            this.clientModel.notifyChangeState(response.getState());
        } catch (RemoteException e) {

        }
    }

    private void parse(CrashedPlayerMessage response){
        try {
            this.clientModel.notifyCrashedPlayer(response.getPlayer());
        } catch (RemoteException ignored) {
            //System.out.println("Crashato");
        }
    }


    private void parse(ConfirmJoinGameMessage message){
        if(message.getConfirmJoin()) {
            System.out.println("Joined successfully");

            hasJoined = true;
        }
        else{
            clientModel=null;
            System.out.println(message.getDetails());
        }
        System.out.flush();
    }

    private void parse(ConfirmPickColorMessage message) {
        if (message.getConfirmPickColor()){
            System.out.println("Color picked successfully");
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }

    private void parse(ConfirmStartingCardMessage message) {
        if (message.getConfirmStarting()){
            System.out.println("Starting card side chosen successfully");
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }

    private void parse(UpdatePointsMessage response){
        try {
            this.clientModel.ReceiveUpdateOnPoints(response.getPlayer(),response.getPoints());
        } catch (RemoteException e) {

        }
    }

    private void parse(PersonalCardsMessage response){
        try {
            this.clientModel.NotifyChangePersonalCards(response.getPlayer(), response.getHand());
        } catch (RemoteException e) {

        }
    }

    private void parse(ChoiceObjectiveMessage choiceObjectiveMessage){
        try {
            System.out.println("you chose objective\n");
            this.clientModel.notifyChoiceObjective(choiceObjectiveMessage.getPlayer(), choiceObjectiveMessage.getObjectiveCard());
        } catch (RemoteException e) {

        }
    }

    private void parse(FirstHandMessage firstHandMessage){
        try {
            this.clientModel.notifyFirstHand(firstHandMessage.getPlayableCard1(), firstHandMessage.getPlayableCard2(), firstHandMessage.getPlayableCard3(), firstHandMessage.getStartingCard(), firstHandMessage.getObjectiveCard1(), firstHandMessage.getObjectiveCard2());
        } catch (RemoteException e) {

        }
    }

    private void parse(UpdateCrashedPlayerMessage updateCrashedPlayerMessage){
        try {
            this.clientModel.UpdateCrashedPlayer(updateCrashedPlayerMessage.getNickname(), updateCrashedPlayerMessage.getChat(), updateCrashedPlayerMessage.getGameState(), updateCrashedPlayerMessage.getHand(), updateCrashedPlayerMessage.getObjectiveCard(), updateCrashedPlayerMessage.getBoards(), updateCrashedPlayerMessage.getPoints(), updateCrashedPlayerMessage.getPlayers(), updateCrashedPlayerMessage.getObjectiveCards(), updateCrashedPlayerMessage.getColor(), updateCrashedPlayerMessage.getTable());
        }catch (RemoteException e){}
    }

    private void parse(PlayerBoardMessage playerBoardMessage){
        try {
            this.clientModel.notifyChangePlayerBoard(playerBoardMessage.getPlayer(),playerBoardMessage.getPlayableCard(), playerBoardMessage.getI(), playerBoardMessage.getJ());
        } catch (RemoteException e) {

        }

    }

    private void parse(GroupChatMessage groupChatMessage){
        try {
            this.clientModel.ReceiveGroupText(groupChatMessage.getSender(),groupChatMessage.getText());
        } catch (RemoteException e) {

        }
    }
    private void parse(PrivateChatMessage privateChatMessage){
        try {
            this.clientModel.ReceivePrivateText(privateChatMessage.getSender(), privateChatMessage.getRecipient(),privateChatMessage.getText());
        } catch (RemoteException e) {

        }
    }

    private void parse(ConfirmChatMessage confirmChatMessage){
        if(confirmChatMessage.isConfirmChat()){
            System.out.println("Message sent correctly");
        }
        else{
            System.out.println(confirmChatMessage.getDetails());
        }
        System.out.flush();
    }

    private void sendMessage(Message message) {
        synchronized (outputStream) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
                outputStream.reset();
                //System.out.println("Client invia a server");
            } catch (IOException ignored) {
            }
        }
    }
}