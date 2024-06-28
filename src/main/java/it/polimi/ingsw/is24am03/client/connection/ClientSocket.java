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
 * Represents a client-side socket connection to interact with a server for a game.
 * Manages communication using object serialization over TCP/IP.
 */
public class ClientSocket implements Client {

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

    private String nicknameTemp;

    /**
     * Constructs a ClientSocket object to connect to the specified server IP and port,
     * using the provided ViewInterface for interaction.
     * @param ip The IP address of the server
     * @param port The port number of the server
     * @param view The ViewInterface implementation (either GUI or CLI)
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
     * Sets the GUI view for this client.
     * @param gui The GUI view to set
     */
    public void setGUI(ViewInterface gui){
        this.view=gui;
    }

    /**
     * Sets the CLI view for this client.
     * @param cli The CLI view to set
     */
    public void setCLI(ViewInterface cli){
        this.view=cli;
    }

    /**
     * Sends a request to the server to create a new game with the specified number of players and nickname.
     * @param nPlayers The number of players in the game
     * @param nickname The nickname chosen by the player
     */
    public void CreateGame(int nPlayers, String nickname) {
        CreateGameMessage requestMessage = new CreateGameMessage(nPlayers, nickname);
        this.sendMessage(requestMessage);
    }

    /**
     * Sends a request to the server to join an existing game with the specified nickname.
     * @param nickname The nickname chosen by the player
     */
    public void JoinGame(String nickname){
        JoinGameMessage joinMessage = new JoinGameMessage(nickname, hasJoined);
        this.nicknameTemp=nickname;
        this.sendMessage(joinMessage);
    }

    /**
     * Sends a request to the server to pick a color for the game.
     * @param color The color chosen by the player
     */
    @Override
    public void PickColor(String color) {
        PickColorMessage colorMessage = new PickColorMessage(nickname, color);
        this.sendMessage(colorMessage);
    }

    /**
     * Sends a request to the server to choose a starting card side for the game.
     * @param face The face of the card chosen by the player
     */
    public void ChooseStartingCardSide(String face){
        ChooseStartingMessage startingMessage= new ChooseStartingMessage(nickname,face);
        this.sendMessage(startingMessage);
    }

    /**
     * Sends a request to the server to place a card on the board.
     * @param choice The choice made by the player
     * @param i The row index for placing the card
     * @param j The column index for placing the card
     * @param face The face of the card to be placed
     */
    public void PlaceCard(int choice, int i, int j, String face){
        PlaceCardMessage placeCardMessage = new PlaceCardMessage(nickname, choice, i, j, face);
        this.sendMessage(placeCardMessage);
    }

    /**
     * Sends a request to the server to draw a gold card.
     */
    public void DrawGold(){
        DrawGoldMessage drawGoldMessage = new DrawGoldMessage(nickname);
        this.sendMessage(drawGoldMessage);
    }

    /**
     * Sends a request to the server to draw a resource card.
     */
    public void DrawResource(){
        DrawResourceMessage drawResourceMessage = new DrawResourceMessage(nickname);
        this.sendMessage(drawResourceMessage);
    }

    /**
     * Sends a request to the server to draw cards from the table.
     * @param choice The choice made by the player
     */
    public void DrawTable(int choice){
        DrawTableMessage drawTableMessage = new DrawTableMessage(nickname, choice);
        this.sendMessage(drawTableMessage);
    }

    /**
     * Sends a request to the server to choose an objective card.
     * @param choice The choice made by the player
     */
    public void ChooseObjectiveCard(int choice){
        ChooseObjectiveMessage chooseObjectiveMessage = new ChooseObjectiveMessage(nickname, choice);
        this.sendMessage(chooseObjectiveMessage);
    }

    /**
     * Sends a group chat message to the server.
     * @param text The text message to send
     */
    @Override
    public void sendGroupText(String text) {
        GroupChatMessage groupChatMessage= new GroupChatMessage(nickname, text);
        this.sendMessage(groupChatMessage);
    }

    /**
     * Sends a private chat message to a specific recipient.
     * @param receiver The recipient of the message
     * @param text The text message to send
     */
    @Override
    public void sendPrivateText(String receiver, String text) {
        PrivateChatMessage privateChatMessage= new PrivateChatMessage(nickname, receiver, text);
        this.sendMessage(privateChatMessage);
    }

    /**
     * Sends a request to the server for a player to rejoin the game.
     * @param player_name The name of the player requesting to rejoin
     */
    public void RejoinGame(String player_name){
        RejoinGameMessage rejoinGameMessage = new RejoinGameMessage(player_name);
        this.nickname=player_name;
        //this.nicknameTemp = player_name;
        this.sendMessage(rejoinGameMessage);
    }

    /**
     * Receives incoming messages from the server and adds them to the message queue.
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
     * Monitors the message queue for incoming messages and processes them accordingly.
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
                }
            }
        });
    }

    /**
     * Parses a received message and performs corresponding actions based on message type.
     * @param responseMessage The message to parse
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
     * Parses a LastRoundMessage and notifies the client model.
     *
     * @param lastRoundMessage The LastRoundMessage to parse.
     */
    private void parse(LastRoundMessage lastRoundMessage){
        try{
            this.clientModel.NotifyLastRound();
        }catch (RemoteException e){}
    }

    /**
     * Parses a FinalColorsMessage and notifies the client model with the final colors.
     *
     * @param response The FinalColorsMessage to parse.
     */
    private void parse(FinalColorsMessage response){
        try{
            this.clientModel.notifyFinalColors(response.getColors());
        }catch (RemoteException e){}
    }

    /**
     * Parses a StartingCommonMessage and updates the client model with initial commons.
     *
     * @param response The StartingCommonMessage to parse.
     */
    private void parse(StartingCommonMessage response){
        try{
            this.clientModel.UpdateFirst(response.getCommons());
        }catch (RemoteException e){}
    }

    /**
     * Parses an AvailableColorMessage and notifies the client model with available colors.
     *
     * @param response The AvailableColorMessage to parse.
     */
    private void parse(AvailableColorMessage response){
        try {
            this.clientModel.notifyAvailableColors(response.getColors());
        }catch(RemoteException e){}
    }

    /**
     * Parses a NotifyNumPlayersReachedMessage and notifies the client model about the number of players reached.
     *
     * @param response The NotifyNumPlayersReachedMessage to parse.
     */
    private void parse(NotifyNumPlayersReachedMessage response){
        if(this.clientModel==null) {
            try {
                clientModel = new ClientModel(this.nicknameTemp, view);
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
     * Parses a ConfirmDrawMessage and handles the drawing confirmation.
     *
     * @param message The ConfirmDrawMessage to parse.
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
     * Parses a ConfirmPlaceMessage and handles the placement confirmation.
     *
     * @param message The ConfirmPlaceMessage to parse.
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
     * Parses a ConfirmGameMessage and handles the confirmation of game creation.
     *
     * @param message The ConfirmGameMessage to parse.
     */
    private void parse(ConfirmGameMessage message) {
        if (message.getConfirmGameCreation()){
            this.nickname = message.getNickname();
            view.confirmCreate();
            try {
                this.clientModel = new ClientModel(this.nickname, view);
            } catch (RemoteException e){}
            hasJoined = true;
        }
        else
            view.drawError(message.getDetails());
        System.out.flush();
    }

    /**
     * Parses a ConfirmChooseObjectiveMessage and handles the confirmation of objective choice.
     *
     * @param message The ConfirmChooseObjectiveMessage to parse.
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
     * Parses a NotifyCommonTableMessage and updates the common table in the client model.
     *
     * @param message The NotifyCommonTableMessage to parse.
     */
    private void parse(NotifyCommonTableMessage message) {
        try {
            this.clientModel.updateCommonTable(message.getResourceCard(), message.getIndex());
        }catch (RemoteException e){}
    }

    /**
     * Parses a CommonObjectiveMessage and notifies the client model about common objectives.
     *
     * @param responseMessage The CommonObjectiveMessage to parse.
     */
    private void parse(CommonObjectiveMessage responseMessage){
        try {
            this.clientModel.notifyCommonObjective(responseMessage.getObjectiveCard1(), responseMessage.getObjectiveCard2());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a WinnersMessage and notifies the client model about winners.
     *
     * @param response The WinnersMessage to parse.
     */
    private void parse(WinnersMessage response){
        try {
            this.clientModel.notifyWinners(response.getWinners());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a ConfirmRejoinGameMessage and handles the confirmation of rejoining the game.
     *
     * @param response The ConfirmRejoinGameMessage to parse.
     */
    private void parse(ConfirmRejoinGameMessage response){
        if(!response.getConfirmRejoin()){
            view.drawError(response.getDetails());
        }
    }

    /**
     * Parses a JoinedPlayerMessage and notifies the client model about a new player who joined.
     *
     * @param response The JoinedPlayerMessage to parse.
     */
    private void parse(JoinedPlayerMessage response){
        try {
            this.clientModel.notifyJoinedPlayer(response.getPlayer());
        } catch (RemoteException e) {
        }
    }

    /**
     * Parses a RejoinedPlayerMessage and notifies the client model about a player who rejoined.
     *
     * @param response The RejoinedPlayerMessage to parse.
     */
    private void parse(RejoinedPlayerMessage response){
        try {
            this.clientModel.notifyRejoinedPlayer(response.getPlayer());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a CurrentPlayerMessage and notifies the client model about the current player.
     *
     * @param response The CurrentPlayerMessage to parse.
     */
    private void parse(CurrentPlayerMessage response){
        try {
            this.clientModel.notifyCurrentPlayer(response.getCurrentPlayer());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a TurnOrderMessage and notifies the client model about the turn order.
     *
     * @param response The TurnOrderMessage to parse.
     */
    private void parse(TurnOrderMessage response){
        try {
            this.clientModel.notifyTurnOrder(response.getTurnOrder());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a ChangeStateMessage and notifies the client model about a state change.
     *
     * @param response The ChangeStateMessage to parse.
     */
    private void parse(ChangeStateMessage response){
        try {
            this.clientModel.notifyChangeState(response.getState());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a CrashedPlayerMessage and notifies the client model about a crashed player.
     *
     * @param response The CrashedPlayerMessage to parse.
     */
    private void parse(CrashedPlayerMessage response){
        try {
            this.clientModel.notifyCrashedPlayer(response.getPlayer());
        } catch (RemoteException ignored) {
        }
    }

    /**
     * Parses a ConfirmJoinGameMessage and handles the confirmation of joining a game.
     *
     * @param message The ConfirmJoinGameMessage to parse.
     */
    private void parse(ConfirmJoinGameMessage message){
        if(message.getConfirmJoin()) {
            hasJoined = true;
            this.nickname = nicknameTemp;
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
     * Parses a ConfirmPickColorMessage and handles the confirmation of picking a color.
     *
     * @param message The ConfirmPickColorMessage to parse.
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
     * Parses a ConfirmStartingCardMessage and handles the confirmation of starting card selection.
     *
     * @param message The ConfirmStartingCardMessage to parse.
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
     * Parses an UpdatePointsMessage and notifies the client model about updated points for a player.
     *
     * @param response The UpdatePointsMessage to parse.
     */
    private void parse(UpdatePointsMessage response){
        try {
            this.clientModel.ReceiveUpdateOnPoints(response.getPlayer(),response.getPoints());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a PersonalCardsMessage and notifies the client model about changes in a player's personal cards.
     *
     * @param response The PersonalCardsMessage to parse.
     */
    private void parse(PersonalCardsMessage response){
        try {
            this.clientModel.NotifyChangePersonalCards(response.getPlayer(), response.getHand());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a ChoiceObjectiveMessage and notifies the client model about a player's chosen objective card.
     *
     * @param choiceObjectiveMessage The ChoiceObjectiveMessage to parse.
     */
    private void parse(ChoiceObjectiveMessage choiceObjectiveMessage){
        try {
            this.clientModel.notifyChoiceObjective(choiceObjectiveMessage.getPlayer(), choiceObjectiveMessage.getObjectiveCard());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a FirstHandMessage and notifies the client model about the initial hand and cards for a player.
     *
     * @param firstHandMessage The FirstHandMessage to parse.
     */
    private void parse(FirstHandMessage firstHandMessage){
        try {
            this.clientModel.notifyFirstHand(firstHandMessage.getPlayableCard1(), firstHandMessage.getPlayableCard2(), firstHandMessage.getPlayableCard3(), firstHandMessage.getStartingCard(), firstHandMessage.getObjectiveCard1(), firstHandMessage.getObjectiveCard2());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses an UpdateCrashedPlayerMessage and updates the client model with information about a crashed player.
     *
     * @param updateCrashedPlayerMessage The UpdateCrashedPlayerMessage to parse.
     */
    private void parse(UpdateCrashedPlayerMessage updateCrashedPlayerMessage){
        try{
            //ho i colori ordinati come i players
            /*int i=0;
            for(int j=0; j<updateCrashedPlayerMessage.getColors().size();j++){
                if(updateCrashedPlayerMessage.getColors().get(j).equals(updateCrashedPlayerMessage.getColor())){
                    i=j;
                }
            }
            this.nickname=updateCrashedPlayerMessage.getPlayers().get(i);*/
            clientModel = new ClientModel(this.nickname, view);
            this.clientModel.UpdateCrashedPlayer(updateCrashedPlayerMessage.getNickname(), updateCrashedPlayerMessage.getChat(), updateCrashedPlayerMessage.getGameState(), updateCrashedPlayerMessage.getHand(), updateCrashedPlayerMessage.getObjectiveCard(), updateCrashedPlayerMessage.getBoards(), updateCrashedPlayerMessage.getPoints(), updateCrashedPlayerMessage.getPlayers(), updateCrashedPlayerMessage.getObjectiveCards(), updateCrashedPlayerMessage.getColor(), updateCrashedPlayerMessage.getTable(), updateCrashedPlayerMessage.getColors());
        }
        catch(RemoteException e){}

    }

    /**
     * Parses a PlayerBoardMessage and notifies the client model about changes in a player's board.
     *
     * @param playerBoardMessage The PlayerBoardMessage to parse.
     */
    private void parse(PlayerBoardMessage playerBoardMessage){
        try {
            this.clientModel.notifyChangePlayerBoard(playerBoardMessage.getPlayer(),playerBoardMessage.getPlayableCard(), playerBoardMessage.getI(), playerBoardMessage.getJ());
        } catch (RemoteException e) {

        }

    }

    /**
     * Parses a GroupChatMessage and notifies the client model about a received group chat message.
     *
     * @param groupChatMessage The GroupChatMessage to parse.
     */
    private void parse(GroupChatMessage groupChatMessage){
        try {
            this.clientModel.ReceiveGroupText(groupChatMessage.getSender(),groupChatMessage.getText());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a PrivateChatMessage and notifies the client model about a received private chat message.
     *
     * @param privateChatMessage The PrivateChatMessage to parse.
     */
    private void parse(PrivateChatMessage privateChatMessage){
        try {
            this.clientModel.ReceivePrivateText(privateChatMessage.getSender(), privateChatMessage.getRecipient(),privateChatMessage.getText());
        } catch (RemoteException e) {

        }
    }

    /**
     * Parses a ConfirmChatMessage and handles the confirmation of a chat message.
     *
     * @param confirmChatMessage The ConfirmChatMessage to parse.
     */
    private void parse(ConfirmChatMessage confirmChatMessage) {
        if (!confirmChatMessage.isConfirmChat()) {
            if (clientModel != null)
                this.clientModel.printNotifications(confirmChatMessage.getDetails());
            else
                view.drawError(confirmChatMessage.getDetails());
        }
    }

    /**
     * Sends a Message object to the server via the output stream.
     *
     * @param message The Message object to be sent.
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
