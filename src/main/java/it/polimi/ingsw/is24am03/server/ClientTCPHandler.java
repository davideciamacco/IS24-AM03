
package it.polimi.ingsw.is24am03.server;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerBoardSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.messages.*;
import it.polimi.ingsw.is24am03.server.controller.GameController;
import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.game.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//import java.rmi.RemoteException;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  ClientTCPHandler is a class used to manage the connection to a client when connected over TCP. It parses the messages over the network
 *  and forwards the responses to the client.
 */
public class ClientTCPHandler implements Runnable, ChatSub, PlayerSub, GameSub, PlayerBoardSub{
    private Socket socket;
    private GameController gameController;
    private  ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private boolean active;
    private String nickname;
    private  Queue<Message> queueMessages;


    /**
     * Constructor of a ClientTCPHandler
     * @param socket connection socket to the client
     * @param gameController actual GameController reference
     */
    public ClientTCPHandler(Socket socket, GameController gameController) {
        this.socket = socket;
        this.gameController = gameController;
        this.queueMessages = new ArrayDeque<>();
        active=true;
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Connection with client failed over TCP");
        }
    }

    /**
     * Handles the execution of the client handler thread. This method continuously listens for incoming messages,
     * processes them, and sends appropriate responses. It also manages a heartbeat mechanism to keep the connection
     * alive.
     */
    public void run() {
        Message response;

        Timer heartbeatTimer = new Timer(true);
        final long HEARTBEAT_INTERVAL = 1000;

        // TimerTask per inviare heartbeat periodicamente
        TimerTask heartbeatTask = new TimerTask() {
            @Override
            public void run() {
                sendMessage(new HeartbeatMessage());
            }
        };

        heartbeatTimer.scheduleAtFixedRate(heartbeatTask, 0, HEARTBEAT_INTERVAL);

        while (active) {
            try {
                Message incomingMessage = (Message) inputStream.readObject();
                response = this.messageParser(incomingMessage);
                sendMessage(response);
            } catch (SocketException e) {
                //e.printStackTrace();
                active = false;
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
            } catch (IOException ignored) {
                //ignored.printStackTrace();
                active = false;
            }
        }

        heartbeatTimer.cancel();

        if (gameController.getGameModel() != null) {
            removeFromObservers();
            gameController.handleCrashedPlayer(nickname);
        }
    }

    /**
     * Parses the incoming message and returns the corresponding response message based on its type.
     *
     * @param inputMessage The incoming message to parse.
     * @return The parsed message response.
     */
    private Message messageParser(Message inputMessage){
        Message outputMessage=null;

        switch (inputMessage.getMessageType()) {
            case CREATE_GAME -> outputMessage = this.parse((CreateGameMessage) inputMessage);
            case JOIN_GAME -> outputMessage = this.parse((JoinGameMessage) inputMessage);
            case PICK_COLOR -> outputMessage = this.parse((PickColorMessage) inputMessage);
            case CHOOSE_STARTING_CARD_SIDE -> outputMessage = this.parse((ChooseStartingMessage) inputMessage);
            case CHOOSE_OBJECTIVE -> outputMessage = this.parse((ChooseObjectiveMessage) inputMessage);
            case PLACE_CARD -> outputMessage = this.parse((PlaceCardMessage) inputMessage);
            case DRAW_GOLD -> outputMessage =this.parse((DrawGoldMessage) inputMessage);
            case DRAW_RESOURCE -> outputMessage = this.parse((DrawResourceMessage) inputMessage);
            case DRAW_TABLE -> outputMessage = this.parse((DrawTableMessage) inputMessage);
            case REJOIN_GAME -> outputMessage=this.parse((RejoinGameMessage) inputMessage);
            case GROUP_CHAT -> outputMessage=this.parse((GroupChatMessage)inputMessage);
            case PRIVATE_CHAT -> outputMessage=this.parse((PrivateChatMessage) inputMessage);
        }
        return outputMessage;
    }

    /**
     * Parses a drawTableMessage, validates the action, performs the drawing operation,
     * and returns a ConfirmDrawMessage indicating the success or failure of the operation.
     *
     * @param drawTableMessage The drawTableMessage to parse and process.
     * @return A ConfirmDrawMessage indicating the result of the draw operation.
     */
    private Message parse(DrawTableMessage drawTableMessage){
        boolean result;
        String description = "";
        try {
            gameController.canDrawTable(drawTableMessage.getNickname(),drawTableMessage.getChoice());
            gameController.drawTable(drawTableMessage.getNickname(),drawTableMessage.getChoice());
            result = true;
        }
        catch (IllegalArgumentException e){
            result=false;
            description="Choice must be 1/2/3/4";
        }
        catch (NullCardSelectedException e){
            result=false;
            description="Empty place selected";
        }
        catch (PlayerNotInTurnException e)
        {
            result=false;
            description="Not your turn";
        }
        catch(InvalidStateException e )
        {
            result = false;
            description = "Action not allowed in this state";
        }
        catch(GameNotExistingException e )
        {
            result = false;
            description = "Game not existing";
        }catch (UnknownPlayerException e){
            result=false;
            description=e.getMessage();
        }
        return new ConfirmDrawMessage(result, description);
    }

    /**
     * Parses a RejoinGameMessage, attempts to rejoin the game for the specified nickname using TCP,
     * and returns a ConfirmRejoinGameMessage indicating the success or failure of the rejoin operation.
     *
     * @param rejoinGameMessage The RejoinGameMessage to parse and process.
     * @return A ConfirmRejoinGameMessage indicating the result of the rejoin operation.
     */
    private Message parse(RejoinGameMessage rejoinGameMessage){
        boolean result;
        String description = "";
        try {
            gameController.rejoinGame(rejoinGameMessage.getNickname(), "TCP");
            result = true;
            this.nickname = rejoinGameMessage.getNickname();
            this.subscribeToObservers();
            gameController.rejoinedChief(rejoinGameMessage.getNickname());

        }
        catch(UnknownPlayerException e )
        {
            result = false;
            description = "Player not existing";

        } catch (InvalidStateException e) {
            result=false;
            description="Action not allowed in this state";
        } catch (GameNotExistingException e){
            result=false;
            description="Game doesn't exist";
        }

        return new ConfirmRejoinGameMessage(result, description);
    }

    /**
     * Parses a PlaceCardMessage, attempts to place a card on the board for the specified player,
     * and returns a ConfirmPlaceMessage indicating the success or failure of the operation.
     *
     * @param placeCardMessage The PlaceCardMessage to parse and process.
     * @return A ConfirmPlaceMessage indicating the result of the place card operation.
     */
    private Message parse(PlaceCardMessage placeCardMessage){
        boolean result;

        String description = "";
        try {
            gameController.placeCard(placeCardMessage.getPlayer(),placeCardMessage.getChoice(),placeCardMessage.getI(),placeCardMessage.getJ(),placeCardMessage.getFace());
            result = true;
        }
        catch (ArgumentException | UnknownPlayerException e){
            result=false;
            description= e.getMessage();
        }
        catch(CoordinatesOutOfBoundsException e){
            result = false;
            description = "Coordinates out of bounds";
        }
        catch (PlayerNotInTurnException e)
        {
            result=false;
            description="Not your turn";
        }
        catch(InvalidStateException e )
        {
            result = false;
            description = "Action not allowed in this state";
        }
        catch(GameNotExistingException e)
        {
            result=false;
            description = "Game not existing";
        }
        catch(PositionOccupiedException e){
            result=false;
            description= "Position is not empty";
        }
        catch(NoCardsAvailableException e){
            result = false;
            description = "Card can't be placed in these coordinates";
        }
        catch (RequirementsNotMetException e){
            result = false;
            description = "Gold card requirements not satisfied";
        }
        catch(IllegalArgumentException e){
            result = false;
            description = "Invalid arguments";
        }  catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return new ConfirmPlaceMessage(result, description);
    }

    /**
     * Parses a chooseObjectiveMessage, validates the player's choice of objective card,
     * and returns a ConfirmChooseObjectiveMessage indicating the success or failure of the operation.
     *
     * @param chooseObjectiveMessage The chooseObjectiveMessage to parse and process.
     * @return A ConfirmChooseObjectiveMessage indicating the result of choosing the objective card.
     */
    private Message parse(ChooseObjectiveMessage chooseObjectiveMessage){
        boolean result;
        String description = "";
        try {
            gameController.canSetObjectiveCard(chooseObjectiveMessage.getPlayer(), chooseObjectiveMessage.getChoose());
            gameController.setObjectiveCard(chooseObjectiveMessage.getPlayer(), chooseObjectiveMessage.getChoose());
            result = true;
        }
        catch(IllegalArgumentException e)
        {
            result=false;
            description = "Invalid arguments";
        }
        catch(InvalidStateException e){
            result=false;
            description = "Action not allowed in this state";
        }
        catch (PlayerNotInTurnException e)
        {
            result=false;
            description="Not your turn";
        }

        catch(GameNotExistingException e)
        {
            result=false;
            description = "Game not existing";
        }
        catch (UnknownPlayerException e){
            result=false;
            description= e.getMessage();
        }

        return new ConfirmChooseObjectiveMessage(result, description);
    }

    /**
     * Parses a CreateGameMessage, validates the request to create a new game,
     * and returns a ConfirmGameMessage indicating the success or failure of the operation.
     *
     * @param createGameMessage The CreateGameMessage to parse and process.
     * @return A ConfirmGameMessage indicating the result of creating the game.
     */
    private Message parse(CreateGameMessage createGameMessage){
        boolean result;
        String description = "";
        try {

            gameController.createGame(createGameMessage.getPlayerNumber(), createGameMessage.getNickname(), "TCP");
            this.nickname=createGameMessage.getNickname();
            this.subscribeToObservers();
            result = true;
        }
        catch(IllegalArgumentException e)
        {
            result=false;
            description = "Invalid arguments";
        }
        catch(GameAlreadyCreatedException e)
        {
            result=false;
            description = "Game already created";
        }

        return new ConfirmGameMessage(result, description, this.nickname);

    }

    /**
     * Parses a JoinGameMessage, validates the request to join a game,
     * and returns a ConfirmJoinGameMessage indicating the success or failure of the operation.
     *
     * @param joinGameMessage The JoinGameMessage to parse and process.
     * @return A ConfirmJoinGameMessage indicating the result of joining the game.
     */
    private Message parse(JoinGameMessage joinGameMessage){
        boolean result;
        String description = "";
        try {
            if(!joinGameMessage.getHasJoined()) {
                    gameController.addPlayer(joinGameMessage.getNickname(), "TCP");
                    this.nickname= joinGameMessage.getNickname();
                    this.subscribeToObservers();
                    if(gameController.getGameModel().getNumPlayers()!=gameController.getGameModel().getPlayers().size()){
                        description="Joined successfully";
                    }
                    gameController.canStart();
                    result = true;
            }
            else
            {
                result = false;
                description = "Already joined";
            }
        }
        catch (NicknameAlreadyUsedException e)
        {
            result = false;
            description = "Nickname already used";
        }
        catch (FullLobbyException e)
        {
            result = false;
            description = "Lobby is full";

        }
        catch (IllegalArgumentException e)
        {
            result = false;
            description = "Nickname not allowed";
        }

        catch(GameNotExistingException e)
        {
            result=false;
            description = "Game not existing";
        }


        return new ConfirmJoinGameMessage(result, description, this.nickname);

    }

    /**
     * Parses a PickColorMessage, validates the request to pick a color,
     * and returns a ConfirmPickColorMessage indicating the success or failure of the operation.
     *
     * @param pickColorMessage The PickColorMessage to parse and process.
     * @return A ConfirmPickColorMessage indicating the result of picking the color.
     */
    private Message parse(PickColorMessage pickColorMessage) {
        boolean result;
        String description = "";
        try {
            gameController.canPickColor(pickColorMessage.getNickname(), pickColorMessage.getColor());
            gameController.pickColor(pickColorMessage.getNickname(),pickColorMessage.getColor());
            result = true;

        } catch (IllegalArgumentException e)
        {
            result=false;
            description="Invalid argument";
        }
        catch (ColorAlreadyPickedException e)
        {
            result=false;
            description="Color not available";
        }
        catch (PlayerNotInTurnException e)
        {
            result=false;
            description="Not your turn";
        }
        catch (InvalidStateException e)
        {
            result=false;
            description="Action not allowed in this state";
        }
        catch(GameNotExistingException e)
        {
            result=false;
            description = "Game not existing";
        }
        catch (UnknownPlayerException e){
            result=false;
            description=e.getMessage();
        }

        return new ConfirmPickColorMessage(result, description);
    }

    /**
     * Parses a ChooseStartingMessage, validates the request to choose a starting card face,
     * and returns a ConfirmStartingCardMessage indicating the success or failure of the operation.
     *
     * @param chooseStartingMessage The ChooseStartingMessage to parse and process.
     * @return A ConfirmStartingCardMessage indicating the result of choosing the starting card face.
     */
    private Message parse(ChooseStartingMessage chooseStartingMessage) {
        boolean result;
        String description = "";
        try {
            gameController.canSelectStartingFace(chooseStartingMessage.getPlayer(),chooseStartingMessage.getFace());
            gameController.selectStartingFace(chooseStartingMessage.getPlayer(),chooseStartingMessage.getFace());
            result = true;
        }
        catch (InvalidStateException e)
        {
            result=false;
            description="Action not allowed in this state";
        } catch (PlayerNotInTurnException e)
        {
            result=false;
            description="Not your turn";
        }
        catch (IllegalArgumentException e)
        {
            result=false;
            description="input not valid";
        }
        catch (ArgumentException | UnknownPlayerException e){
            result=false;
            description=e.getMessage();
        }
        catch(GameNotExistingException e)
        {
            result=false;
            description = "Game not existing";
        }
        return new ConfirmStartingCardMessage(result, description);
    }

    /**
     * Parses a drawResourceMessage, validates the request to draw resources,
     * and returns a ConfirmDrawMessage indicating the success or failure of the operation.
     *
     * @param drawResourceMessage The drawResourceMessage to parse and process.
     * @return A ConfirmDrawMessage indicating the result of drawing resources.
     */
    private Message parse(DrawResourceMessage drawResourceMessage){
        boolean result;
        String description = "";
        try {
            gameController.canDrawResources(drawResourceMessage.getNickname());
            gameController.drawResources(drawResourceMessage.getNickname());
            result = true;
        }
        catch(PlayerNotInTurnException e)
        {
            result=false;
            description = "Not your turn";
        }
        catch(InvalidStateException e)
        {
            result=false;
            description = "Action not allowed in this state";
        }
        catch(GameNotExistingException e)
        {
            result=false;
            description = "Game not existing";
        }
        catch (EmptyDeckException e){
            result=false;
            description= "Empty deck";
        }
        catch (UnknownPlayerException e){
            result=false;
            description=e.getMessage();
        }


        return new ConfirmDrawMessage(result, description);
    }

    /**
     * Parses a DrawGoldMessage, validates the request to draw gold,
     * and returns a ConfirmDrawMessage indicating the success or failure of the operation.
     *
     * @param drawGoldMessage The DrawGoldMessage to parse and process.
     * @return A ConfirmDrawMessage indicating the result of drawing gold.
     */
    private Message parse(DrawGoldMessage drawGoldMessage){
        boolean result;
        String description = "";
        try {
            gameController.canDrawGold(drawGoldMessage.getNickname());
            gameController.drawGold(drawGoldMessage.getNickname());
            result = true;
        }
        catch(PlayerNotInTurnException e)
        {
            result=false;
            description = "Not your turn";
        }
        catch(InvalidStateException e)
        {
            result=false;
            description = "Action not allowed in this state";
        }
        catch(GameNotExistingException e)
        {
            result=false;
            description = "Game not existing";
        }
        catch(EmptyDeckException | UnknownPlayerException e){
            result=false;
            description=e.getMessage();
        }

        return new ConfirmDrawMessage(result, description);
    }

    /**
     * Sends a Message object over the socket's ObjectOutputStream.
     *
     * @param message The Message object to send.
     */
    private void sendMessage(Message message){
        synchronized (outputStream) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
                outputStream.reset();
            } catch (IOException e) {
                try {
                    //e.printStackTrace();
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }


    /**
     * Notifies all connected clients that a new player has joined the game.
     *
     * @param joinedPlayer The nickname of the player who joined.
     * @throws RemoteException If there is a communication-related exception.
     */
    @Override
    public void notifyJoinedPlayer(String joinedPlayer) throws RemoteException {
        JoinedPlayerMessage joinedPlayerMessage = new JoinedPlayerMessage(joinedPlayer);
        this.sendMessage(joinedPlayerMessage);
    }

    /**
     * Notifies all connected clients of the winners of the game.
     *
     * @param winners The list of nicknames of the players who won the game.
     * @throws RemoteException If there is a communication-related exception.
     */
    @Override
    public void notifyWinners(ArrayList<String> winners) throws RemoteException {
        WinnersMessage winnersMessage = new WinnersMessage(winners);
        this.sendMessage(winnersMessage);
    }

    /**
     * Notifies all connected clients of the current turn order in the game.
     *
     * @param order The list of nicknames representing the current turn order.
     * @throws RemoteException If there is a communication-related exception.
     */
    @Override
    public void notifyTurnOrder(ArrayList<String> order) throws RemoteException {
        TurnOrderMessage turnOrderMessage = new TurnOrderMessage(order);
        this.sendMessage(turnOrderMessage);
    }

    /**
     * Notifies all connected clients of the player who is currently taking their turn.
     *
     * @param current The nickname of the current player.
     * @throws RemoteException If there is a communication-related exception.
     */
    @Override
    public void notifyCurrentPlayer(String current) throws RemoteException {
        CurrentPlayerMessage currentPlayerMessage = new CurrentPlayerMessage(current);
        this.sendMessage(currentPlayerMessage);
    }

    /**
     * Notifies all connected clients that a player has crashed or disconnected from the game unexpectedly.
     *
     * @param username The username of the player who has crashed.
     * @throws RemoteException If there is a communication-related exception.
     */
    @Override
    public void notifyCrashedPlayer(String username) throws RemoteException {
        CrashedPlayerMessage crashedPlayerMessage=new CrashedPlayerMessage(username);
        this.sendMessage(crashedPlayerMessage);
    }

    /**
     * Notifies all connected clients about a change in the game state.
     *
     * @param gameState The new state of the game to be communicated to clients.
     * @throws RemoteException If there is a communication-related exception.
     */
    @Override
    public void notifyChangeState(State gameState) throws RemoteException {
        ChangeStateMessage changeStateMessage=new ChangeStateMessage(gameState);
        this.sendMessage(changeStateMessage);
    }

    /**
     * Notifies the client that a player has rejoined the game.
     *
     * @param rejoinedPlayer The nickname of the rejoined player.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void notifyRejoinedPlayer(String rejoinedPlayer) throws RemoteException {
        RejoinedPlayerMessage rejoinedPlayerMessage=new RejoinedPlayerMessage(rejoinedPlayer);
        this.sendMessage(rejoinedPlayerMessage);
    }

    /**
     * Notifies the client about a change in a player's board state.
     *
     * @param player The nickname of the player.
     * @param p The card played.
     * @param i The row index.
     * @param j The column index.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void notifyChangePlayerBoard(String player, PlayableCard p, int i, int j) throws RemoteException {
        PlayerBoardMessage playerBoardMessage=new PlayerBoardMessage(player,p,i,j);
        this.sendMessage(playerBoardMessage);
    }

    /**
     * Receives an update on a player's points and notifies the client.
     *
     * @param player The nickname of the player.
     * @param points The updated points.
     */
    @Override
    public void ReceiveUpdateOnPoints(String player, int points) {
        UpdatePointsMessage updatePointsMessage=new UpdatePointsMessage(player,points);
        this.sendMessage(updatePointsMessage);
    }
    /**
     * Notifies the client about a change in a player's personal cards.
     *
     * @param Player The nickname of the player.
     * @param hand The updated list of resource cards.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void NotifyChangePersonalCards(String Player, ArrayList<ResourceCard> hand) throws RemoteException {
        PersonalCardsMessage personalCardsMessage=new PersonalCardsMessage(Player,hand);
        this.sendMessage(personalCardsMessage);
    }
    /**
     * Notifies the client about a player's choice of objective card.
     *
     * @param player The nickname of the player.
     * @param o The chosen objective card.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void notifyChoiceObjective(String player, ObjectiveCard o) throws RemoteException {
        ChoiceObjectiveMessage choiceObjectiveMessage=new ChoiceObjectiveMessage(player,o);
        this.sendMessage(choiceObjectiveMessage);
    }
    /**
     * Retrieves the nickname of the client.
     *
     * @return The nickname of the client.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public String getSub() throws RemoteException {
        return this.nickname;
    }
    /**
     * Notifies the client about the initial hand dealt to a player.
     *
     * @param p1 First resource card.
     * @param p2 Second resource card.
     * @param p3 Third resource card.
     * @param startingCard The starting card.
     * @param o1 First objective card.
     * @param o2 Second objective card.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void notifyFirstHand(ResourceCard p1, ResourceCard p2, ResourceCard p3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2) throws RemoteException {
        FirstHandMessage firstHandMessage=new FirstHandMessage(p1,p2,p3,startingCard, o1,o2);
        this.sendMessage(firstHandMessage);
    }
    /**
     * Notifies the client about the common objective cards.
     *
     * @param objectiveCard1 The first common objective card.
     * @param objectiveCard2 The second common objective card.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2) throws RemoteException {
        CommonObjectiveMessage commonObjectiveMessage=new CommonObjectiveMessage(objectiveCard1,objectiveCard2);
        this.sendMessage(commonObjectiveMessage);
    }
    /**
     * Updates the client about a change in the common table's state.
     *
     * @param resourceCard The resource card added/removed.
     * @param index The index of the common table.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void updateCommonTable(ResourceCard resourceCard, int index) throws RemoteException {
        NotifyCommonTableMessage notifyCommonTableMessage=new NotifyCommonTableMessage(resourceCard,index);
        this.sendMessage(notifyCommonTableMessage);
    }
    /**
     * Notifies the client that the number of players reached the required amount.
     *
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void NotifyNumbersOfPlayersReached() throws RemoteException {
        NotifyNumPlayersReachedMessage notifyNumPlayersReachedMessage=new NotifyNumPlayersReachedMessage();
        this.sendMessage(notifyNumPlayersReachedMessage);
    }
    /**
     * Notifies the client that it is the last round of the game.
     *
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void NotifyLastRound() throws RemoteException {
        LastRoundMessage lastRoundMessage=new LastRoundMessage();
        this.sendMessage(lastRoundMessage);
    }
    /**
     * Notifies the client about the available colors for selection.
     *
     * @param colors The list of available colors.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void notifyAvailableColors(ArrayList<Color> colors) throws RemoteException {
        AvailableColorMessage availableColorMessage= new AvailableColorMessage(colors);
        this.sendMessage(availableColorMessage);
    }
    /**
     * Notifies the client about the final chosen colors of players.
     *
     * @param colors The map of players and their chosen colors.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void notifyFinalColors(Map<String, Color> colors) throws RemoteException {
        FinalColorsMessage finalColorsMessage=new FinalColorsMessage(colors);
        this.sendMessage(finalColorsMessage);
    }
    /**
     * Updates the client about a crashed player's state.
     *
     * @param nickname The nickname of the crashed player.
     * @param chat The chat messages.
     * @param gameState The current game state.
     * @param hand The crashed player's hand.
     * @param objectiveCard The objective card of the crashed player.
     * @param boards The boards of all players.
     * @param points The points of all players.
     * @param players The list of players.
     * @param objectiveCards The objective cards of all players.
     * @param color The color of the crashed player.
     * @param table The common table of resources.
     * @param colors The available colors.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void UpdateCrashedPlayer(String nickname, ArrayList<Text> chat, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table, ArrayList<Color> colors) throws RemoteException {
        UpdateCrashedPlayerMessage updateCrashedPlayerMessage=new UpdateCrashedPlayerMessage(nickname,chat,gameState,hand,objectiveCard,boards,points,players,objectiveCards,color,table, colors);
        this.sendMessage(updateCrashedPlayerMessage);
    }
    /**
     * Updates the client about the initial common resources available.
     *
     * @param commons The list of initial common resource cards.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void UpdateFirst(ArrayList<ResourceCard> commons) throws RemoteException {
        StartingCommonMessage startingCommonMessage=new StartingCommonMessage(commons);
        this.sendMessage(startingCommonMessage);
    }
    /**
     * Receives a group chat message and processes it.
     *
     * @param sender The sender of the group chat.
     * @param text The message text.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void ReceiveGroupText(String sender, String text) throws RemoteException {
        GroupChatMessage groupChatMessage=new GroupChatMessage(sender, text);
        this.sendMessage(groupChatMessage);

    }
    /**
     * Receives a private chat message and processes it.
     *
     * @param sender The sender of the private chat.
     * @param receiver The receiver of the private chat.
     * @param text The message text.
     * @throws RemoteException If there is a communication error.
     */
    @Override
    public void ReceivePrivateText(String sender, String receiver, String text) throws RemoteException {
        PrivateChatMessage privateChatMessage=new PrivateChatMessage(sender, receiver,text);
        this.sendMessage(privateChatMessage);
    }
    /**
     * Subscribes the client to relevant observers.
     */
    private void subscribeToObservers(){
        gameController.addToObserver((GameSub) this);
        gameController.addToObserver((ChatSub) this);
        gameController.addToObserver((PlayerSub) this);
        gameController.addToObserver((PlayerBoardSub) this);
    }
    /**
     * Removes the client from relevant observers.
     */
    private void removeFromObservers(){
        gameController.removeSub((ChatSub)this);
        gameController.removeSub((PlayerSub) this);
        gameController.removeSub((PlayerBoardSub) this);
        gameController.removeSub((GameSub) this);

    }
    /**
     * Parses a group chat message and sends a confirmation message.
     *
     * @param groupChatMessage The group chat message to parse.
     * @return A confirmation message regarding the success of sending the message.
     */
    private Message parse(GroupChatMessage groupChatMessage){
        boolean result;
        String description = "";
        try {
            gameController.canSendGroupChat(groupChatMessage.getSender(), groupChatMessage.getText());
            gameController.sendGroupText(groupChatMessage.getSender(), groupChatMessage.getText());
            result=true;

        } catch ( BadTextException | GameNotExistingException | UnknownPlayerException | InvalidStateException e) {
            result = false;
            description = description + e.getMessage();
        }

        return new ConfirmChatMessage(result, description);
    }

    /**
     * Parses a private chat message and sends a confirmation message.
     *
     * @param privateChatMessage The private chat message to parse.
     * @return A confirmation message regarding the success of sending the message.
     */
    private Message parse(PrivateChatMessage privateChatMessage){
        boolean result;
        String description="";
        try{
            gameController.canSendPrivateChat(privateChatMessage.getSender(),privateChatMessage.getRecipient(),privateChatMessage.getText());
            gameController.sendPrivateText(privateChatMessage.getSender(), privateChatMessage.getRecipient(),privateChatMessage.getText());
            result=true;

        }catch (GameNotExistingException| UnknownPlayerException |PlayerAbsentException | BadTextException | InvalidStateException | ParametersException e){

            result=false;
            description=description+e.getMessage();
        }

        return new ConfirmChatMessage(result,description);
    }


}
