package it.polimi.ingsw.is24am03.client.connection;
import it.polimi.ingsw.is24am03.client.view.ViewInterface;
import it.polimi.ingsw.is24am03.client.clientModel.ClientModel;
import it.polimi.ingsw.is24am03.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
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
    private ViewInterface view;
    private String nickname;


    /**
     *
     * @param ip
     * @param port
     * @param view
     */
    public ClientSocket(String ip, int port, ViewInterface view) {
        this.ip = ip;
        this.port = port;
        this.queueMessages = new ArrayDeque<>();
        this.threadManager = Executors.newCachedThreadPool();
        this.view = view;
        this.hasJoined = false;
        try {
            this.connection = new Socket(ip, port);
            this.connection.setSoTimeout(2000);
            this.connection.setKeepAlive(false);
            this.outputStream = new ObjectOutputStream(connection.getOutputStream());
            this.inputStream = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Connection Failed! Please restart the game");
        }
        this.messagesReceiver();
        this.ParserAgent();
    }

    /**
     *
     * @param gui
     */
    public void setGUI(ViewInterface gui){
        this.view=gui;
    }

    /**
     *
     * @param cli
     */
    public void setCLI(ViewInterface cli){
        this.view=cli;
    }

    /**
     *
     * @param nPlayers
     * @param nickname
     */
    public void CreateGame(int nPlayers, String nickname) {
        CreateGameMessage requestMessage = new CreateGameMessage(nPlayers, nickname);
        this.nickname=nickname;
        this.sendMessage(requestMessage);
    }

    /**
     *
     * @param nickname
     */
    public void JoinGame(String nickname){
        JoinGameMessage joinMessage = new JoinGameMessage(nickname, hasJoined);
        this.nickname = nickname;
        this.sendMessage(joinMessage);
    }

    /**
     *
     * @param color
     */
    @Override
    public void PickColor(String color) {
        PickColorMessage colorMessage = new PickColorMessage(nickname, color);
        this.sendMessage(colorMessage);
    }

    /**
     *
     * @param face
     */
    public void ChooseStartingCardSide(String face){
        ChooseStartingMessage startingMessage= new ChooseStartingMessage(nickname,face);
        this.sendMessage(startingMessage);
    }

    /**
     *
     * @param choice
     * @param i
     * @param j
     * @param face
     */
    public void PlaceCard(int choice, int i, int j, String face){
        PlaceCardMessage placeCardMessage = new PlaceCardMessage(nickname, choice, i, j, face);
        this.sendMessage(placeCardMessage);
    }

    /**
     *
     */
    public void DrawGold(){
        DrawGoldMessage drawGoldMessage = new DrawGoldMessage(nickname);
        this.sendMessage(drawGoldMessage);
    }

    /**
     *
     */
    public void DrawResource(){
        DrawResourceMessage drawResourceMessage = new DrawResourceMessage(nickname);
        this.sendMessage(drawResourceMessage);
    }

    /**
     *
     * @param choice
     */
    public void DrawTable(int choice){
        DrawTableMessage drawTableMessage = new DrawTableMessage(nickname, choice);
        this.sendMessage(drawTableMessage);
    }

    /**
     *
     * @param choice
     */
    public void ChooseObjectiveCard(int choice){
        ChooseObjectiveMessage chooseObjectiveMessage = new ChooseObjectiveMessage(nickname, choice);
        this.sendMessage(chooseObjectiveMessage);
    }

    /**
     *
     * @param text
     */
    @Override
    public void sendGroupText(String text) {
        GroupChatMessage groupChatMessage= new GroupChatMessage(nickname, text);
        this.sendMessage(groupChatMessage);
    }

    /**
     *
     * @param receiver
     * @param text
     */
    @Override
    public void sendPrivateText(String receiver, String text) {
        PrivateChatMessage privateChatMessage= new PrivateChatMessage(nickname, receiver, text);
        this.sendMessage(privateChatMessage);
    }

    /**
     *
     * @param player_name
     */
    public void RejoinGame(String player_name){
        RejoinGameMessage rejoinGameMessage = new RejoinGameMessage(player_name);
        this.nickname = player_name;
        this.sendMessage(rejoinGameMessage);
    }


    /**
     *
     */
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
                    }
                    catch (SocketTimeoutException e){
                        System.out.println("Server disconnected. Closing client...");
                        //System.out.println(this.view);
                        active=false;
                        System.exit(0);
                    }
                    catch (IOException | ClassNotFoundException | InterruptedException e ) {
                        System.out.println("Server disconnected. Closing client...");
                        active = false;
                        System.exit(0);
                    }
                }
            }
        });
    }


    /**
     *
     */
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

    /**
     *
     * @param responseMessage
     */
    private void parse(Message responseMessage){

    //    if(responseMessage == null) return;

        switch (responseMessage.getMessageType()){

            case CONFIRM_GAME -> this.parse((ConfirmGameMessage) responseMessage);
            case CONFIRM_JOIN -> this.parse((ConfirmJoinGameMessage) responseMessage);
            case CONFIRM_PICK -> this.parse((ConfirmPickColorMessage) responseMessage);
            case CONFIRM_CHOOSE_SIDE -> this.parse((ConfirmStartingCardMessage) responseMessage);
            case CONFIRM_CHOOSE_OBJECTIVE -> this.parse((ConfirmChooseObjectiveMessage) responseMessage);
            case CONFIRM_PLACE -> this.parse((ConfirmPlaceMessage) responseMessage);
            case CONFIRM_DRAW -> this.parse((ConfirmDrawMessage) responseMessage);
            case CONFIRM_REJOIN -> this.parse((ConfirmRejoinGameMessage) responseMessage);
            case CONFIRM_CHAT -> this.parse((ConfirmChatMessage)responseMessage);
            case UPDATE_COMMON_TABLE -> this.parse((NotifyCommonTableMessage)responseMessage);
            case COMMON_OBJECTIVE-> this.parse((CommonObjectiveMessage) responseMessage);
            case NOTIFY_WINNERS-> this.parse((WinnersMessage) responseMessage);
            case JOINED_PLAYER-> this.parse((JoinedPlayerMessage) responseMessage);
            case NOTIFY_REJOINED_PLAYER-> this.parse((RejoinedPlayerMessage) responseMessage);
            case NOTIFY_CURRENT_PLAYER -> this.parse((CurrentPlayerMessage) responseMessage);
            case TURN_ORDER-> this.parse((TurnOrderMessage) responseMessage);
            case GAME_STATE-> this.parse((ChangeStateMessage) responseMessage);
            case NOTIFY_CRASHED_PLAYER-> this.parse((CrashedPlayerMessage) responseMessage);
            case UPDATE_POINTS-> this.parse((UpdatePointsMessage) responseMessage);
            case UPDATE_PERSONAL_CARDS-> this.parse((PersonalCardsMessage) responseMessage);
            case UPDATE_PERSONAL_OBJECTIVE-> this.parse((ChoiceObjectiveMessage) responseMessage);
            case FIRST_HAND-> this.parse((FirstHandMessage) responseMessage);
            case UPDATE_CRASHED_PLAYER -> this.parse((UpdateCrashedPlayerMessage)responseMessage);
            case UPDATE_PLAYER_BOARD-> this.parse((PlayerBoardMessage) responseMessage);
            case GROUP_CHAT-> this.parse((GroupChatMessage) responseMessage);
            case PRIVATE_CHAT-> this.parse((PrivateChatMessage) responseMessage);
            case FIRST_COMMON -> this.parse((StartingCommonMessage)responseMessage);
            case AVAILABLE_COLORS -> this.parse((AvailableColorMessage) responseMessage);
            case FINAL_COLORS -> this.parse((FinalColorsMessage)responseMessage);
            case NOTIFY_ADDITIONAL_ROUND -> this.parse((LastRoundMessage)responseMessage);
            case NOTIFY_NUM_PLAYERS_REACHED -> this.parse((NotifyNumPlayersReachedMessage)responseMessage);
            default -> {
            }
        }
    }

    /**
     *
     * @param lastRoundMessage
     */
    private void parse(LastRoundMessage lastRoundMessage){
        try{
            this.clientModel.NotifyLastRound();
        }catch (RemoteException e){}
    }

    /**
     *
     * @param response
     */
    private void parse(FinalColorsMessage response){
        try{
            this.clientModel.notifyFinalColors(response.getColors());
        }catch (RemoteException e){}
    }

    /**
     *
     * @param response
     */
    private void parse(StartingCommonMessage response){
        try{
            this.clientModel.UpdateFirst(response.getCommons());
        }catch (RemoteException e){}
    }

    /**
     *
     * @param response
     */
    private void parse(AvailableColorMessage response){
        try {
            this.clientModel.notifyAvailableColors(response.getColors());
        }catch(RemoteException e){}
    }

    /**
     *
     * @param response
     */
    private void parse(NotifyNumPlayersReachedMessage response){
        if(this.clientModel==null) {
            try {
                clientModel = new ClientModel(this.nickname, view);
                this.clientModel.NotifyNumbersOfPlayersReached();
            } catch (RemoteException e) {
            }
        }
        else {
            try {
                this.clientModel.NotifyNumbersOfPlayersReached();
            } catch (RemoteException e) {
            }
        }
    }

    /**
     *
     * @param message
     */
    private void parse(ConfirmDrawMessage message) {
        if (!message.getconfirmdraw()) {
            if(clientModel!=null)
                this.clientModel.printNotifications(message.getDetails());
            else {
                view.drawError(message.getDetails());
            }
        }
        System.out.flush();
    }

    /**
     *
     * @param message
     */
    private void parse(ConfirmPlaceMessage message) {
        if (!message.getConfirmPlace()) {
            if(clientModel!=null)
                this.clientModel.printNotifications(message.getDetails());
            else {
                view.drawError(message.getDetails());
            }
        }
        System.out.flush();
    }

    /**
     *
     * @param message
     */
    private void parse(ConfirmGameMessage message) {
        if (message.getConfirmGameCreation()){
            this.nickname = message.getNickname();
            view.confirmCreate();
            try {
                this.clientModel = new ClientModel(this.nickname, view);
            }catch (RemoteException e){}
            hasJoined = true;
        }
        else
            view.drawError(message.getDetails());
        System.out.flush();
    }

    /**
     *
     * @param message
     */
    private void parse(ConfirmChooseObjectiveMessage message) {
        if (!message.getConfirmChoose()){
            if(clientModel!=null)
                this.clientModel.printNotifications(message.getDetails());
            else
                view.drawError(message.getDetails());
        }
        System.out.flush();
    }

    /**
     *
     * @param message
     */
    private void parse(NotifyCommonTableMessage message) {
        try {
            this.clientModel.updateCommonTable(message.getResourceCard(), message.getIndex());
        }catch (RemoteException e){}
    }

    /**
     *
     * @param responseMessage
     */
    private void parse(CommonObjectiveMessage responseMessage){
        try {
            this.clientModel.notifyCommonObjective(responseMessage.getObjectiveCard1(), responseMessage.getObjectiveCard2());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param response
     */
    private void parse(WinnersMessage response){
        try {
            this.clientModel.notifyWinners(response.getWinners());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param response
     */
    private void parse(ConfirmRejoinGameMessage response){
        if(!response.getConfirmRejoin()){
            view.drawError(response.getDetails());
        }

    }

    /**
     *
     * @param response
     */
    private void parse(JoinedPlayerMessage response){
        try {
            this.clientModel.notifyJoinedPlayer(response.getPlayer());
        } catch (RemoteException e) {
        }
    }

    /**
     *
     * @param response
     */
    private void parse (RejoinedPlayerMessage response){
        try {
            this.clientModel.notifyRejoinedPlayer(response.getPlayer());
        } catch (RemoteException e) {

        }
       // System.out.println(response.getPlayer()+"has rejoined the game");
    }

    /**
     *
     * @param response
     */
    private void parse(CurrentPlayerMessage response){
        try {
            this.clientModel.notifyCurrentPlayer(response.getCurrentPlayer());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param response
     */
    private void parse(TurnOrderMessage response){
        try {
            this.clientModel.notifyTurnOrder(response.getTurnOrder());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param response
     */
    private void parse(ChangeStateMessage response){
        try {
            this.clientModel.notifyChangeState(response.getState());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param response
     */
    private void parse(CrashedPlayerMessage response){
        try {
            this.clientModel.notifyCrashedPlayer(response.getPlayer());
        } catch (RemoteException ignored) {
        }
    }

    /**
     *
     * @param message
     */
    private void parse(ConfirmJoinGameMessage message){
        if(message.getConfirmJoin()) {
            hasJoined = true;
            if (this.clientModel == null) {
                try {
                    this.clientModel = new ClientModel(message.getNickname(), view);
                    if (!message.getDetails().isEmpty()) {
                        view.confirmJoin();
                    }
                }catch (RemoteException e){}
            }
        }
        else{
            view.drawError(message.getDetails());
        }
        System.out.flush();
    }

    /**
     *
     * @param message
     */
    private void parse(ConfirmPickColorMessage message) {
        if (!message.getConfirmPickColor()){
            if(clientModel!=null)
                this.clientModel.printNotifications(message.getDetails());
            else
                view.drawError(message.getDetails());
        }

        System.out.flush();
    }

    /**
     *
     * @param message
     */
    private void parse(ConfirmStartingCardMessage message) {
        if (!message.getConfirmStarting()){
            if(clientModel!=null)
                this.clientModel.printNotifications(message.getDetails());
            else
                view.drawError(message.getDetails());
        }
        System.out.flush();
    }

    /**
     *
     * @param response
     */
    private void parse(UpdatePointsMessage response){
        try {
            this.clientModel.ReceiveUpdateOnPoints(response.getPlayer(),response.getPoints());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param response
     */
    private void parse(PersonalCardsMessage response){
        try {
            this.clientModel.NotifyChangePersonalCards(response.getPlayer(), response.getHand());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param choiceObjectiveMessage
     */
    private void parse(ChoiceObjectiveMessage choiceObjectiveMessage){
        try {
            this.clientModel.notifyChoiceObjective(choiceObjectiveMessage.getPlayer(), choiceObjectiveMessage.getObjectiveCard());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param firstHandMessage
     */
    private void parse(FirstHandMessage firstHandMessage){
        try {
            this.clientModel.notifyFirstHand(firstHandMessage.getPlayableCard1(), firstHandMessage.getPlayableCard2(), firstHandMessage.getPlayableCard3(), firstHandMessage.getStartingCard(), firstHandMessage.getObjectiveCard1(), firstHandMessage.getObjectiveCard2());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param updateCrashedPlayerMessage
     */
    private void parse(UpdateCrashedPlayerMessage updateCrashedPlayerMessage){
        try{
            clientModel = new ClientModel(this.nickname, view);
            this.clientModel.UpdateCrashedPlayer(updateCrashedPlayerMessage.getNickname(), updateCrashedPlayerMessage.getChat(), updateCrashedPlayerMessage.getGameState(), updateCrashedPlayerMessage.getHand(), updateCrashedPlayerMessage.getObjectiveCard(), updateCrashedPlayerMessage.getBoards(), updateCrashedPlayerMessage.getPoints(), updateCrashedPlayerMessage.getPlayers(), updateCrashedPlayerMessage.getObjectiveCards(), updateCrashedPlayerMessage.getColor(), updateCrashedPlayerMessage.getTable(), updateCrashedPlayerMessage.getColors());
        }
        catch(RemoteException e){}

    }

    /**
     *
     * @param playerBoardMessage
     */
    private void parse(PlayerBoardMessage playerBoardMessage){
        try {
            this.clientModel.notifyChangePlayerBoard(playerBoardMessage.getPlayer(),playerBoardMessage.getPlayableCard(), playerBoardMessage.getI(), playerBoardMessage.getJ());
        } catch (RemoteException e) {

        }

    }

    /**
     *
     * @param groupChatMessage
     */
    private void parse(GroupChatMessage groupChatMessage){
        try {
            this.clientModel.ReceiveGroupText(groupChatMessage.getSender(),groupChatMessage.getText());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param privateChatMessage
     */
    private void parse(PrivateChatMessage privateChatMessage){
        try {
            this.clientModel.ReceivePrivateText(privateChatMessage.getSender(), privateChatMessage.getRecipient(),privateChatMessage.getText());
        } catch (RemoteException e) {

        }
    }

    /**
     *
     * @param confirmChatMessage
     */
    private void parse(ConfirmChatMessage confirmChatMessage){
        if(!confirmChatMessage.isConfirmChat()){
            if(clientModel!=null)
                this.clientModel.printNotifications(confirmChatMessage.getDetails());
            else
                view.drawError(confirmChatMessage.getDetails());
        }
       // System.out.flush();
    }

    /**
     *
     * @param message
     */
    private void sendMessage(Message message) {
        synchronized (outputStream) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
                outputStream.reset();
                //System.out.println("Client invia a server");
            } catch (IOException ignored) {
                System.out.println("Server disconnected. Closing client...");
                System.exit(0);
            }
        }
    }
}