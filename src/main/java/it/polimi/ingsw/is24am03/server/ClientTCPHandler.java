
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

public class ClientTCPHandler implements Runnable, ChatSub, PlayerSub, GameSub, PlayerBoardSub{
    private Socket socket;
    private GameController gameController;
    private  ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private boolean active;
    private String nickname;
    private  Queue<Message> queueMessages;



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

    public void run() {
        Message response;
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
        if (gameController.getGameModel() != null) {
            removeFromObservers();
            gameController.handleCrashedPlayer(nickname);
        }
    }


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


    private Message parse(DrawTableMessage DrawTableMessage){
        boolean result;
        String description = "";
        try {
            gameController.canDrawTable(DrawTableMessage.getNickname(),DrawTableMessage.getChoice());
            gameController.drawTable(DrawTableMessage.getNickname(),DrawTableMessage.getChoice());
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
    private Message parse(ChooseObjectiveMessage ChooseObjectiveMessage){
        boolean result;
        String description = "";
        try {
            gameController.canSetObjectiveCard(ChooseObjectiveMessage.getPlayer(), ChooseObjectiveMessage.getChoose());
            gameController.setObjectiveCard(ChooseObjectiveMessage.getPlayer(), ChooseObjectiveMessage.getChoose());
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
  
    private Message parse(DrawResourceMessage DrawResourceMessage){
        boolean result;
        String description = "";
        try {
            gameController.canDrawResources(DrawResourceMessage.getNickname());
            gameController.drawResources(DrawResourceMessage.getNickname());
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

    @Override
    public void notifyJoinedPlayer(String joinedPlayer) throws RemoteException {
        JoinedPlayerMessage joinedPlayerMessage=new JoinedPlayerMessage(joinedPlayer);
        this.sendMessage(joinedPlayerMessage);
    }

    @Override
    public void notifyWinners(ArrayList<String> winners) throws RemoteException {
        WinnersMessage winnersMessage=new WinnersMessage(winners);
        this.sendMessage(winnersMessage);
    }

    @Override
    public void notifyTurnOrder(ArrayList<String> order) throws RemoteException {
        TurnOrderMessage turnOrderMessage=new TurnOrderMessage(order);
        this.sendMessage(turnOrderMessage);
    }

    @Override
    public void notifyCurrentPlayer(String current) throws RemoteException {
        CurrentPlayerMessage currentPlayerMessage=new CurrentPlayerMessage(current);
        this.sendMessage(currentPlayerMessage);
    }

    @Override
    public void notifyCrashedPlayer(String username) throws RemoteException {
        CrashedPlayerMessage crashedPlayerMessage=new CrashedPlayerMessage(username);
        this.sendMessage(crashedPlayerMessage);
    }

    @Override
    public void notifyChangeState(State gameState) throws RemoteException {
        ChangeStateMessage changeStateMessage=new ChangeStateMessage(gameState);
        this.sendMessage(changeStateMessage);
    }

    @Override
    public void notifyRejoinedPlayer(String rejoinedPlayer) throws RemoteException {
        RejoinedPlayerMessage rejoinedPlayerMessage=new RejoinedPlayerMessage(rejoinedPlayer);
        this.sendMessage(rejoinedPlayerMessage);
    }

    @Override
    public void notifyChangePlayerBoard(String player, PlayableCard p, int i, int j) throws RemoteException {
        PlayerBoardMessage playerBoardMessage=new PlayerBoardMessage(player,p,i,j);
        this.sendMessage(playerBoardMessage);
    }

    @Override
    public void ReceiveUpdateOnPoints(String player, int points) {
        UpdatePointsMessage updatePointsMessage=new UpdatePointsMessage(player,points);
        this.sendMessage(updatePointsMessage);
    }

    @Override
    public void NotifyChangePersonalCards(String Player, ArrayList<ResourceCard> hand) throws RemoteException {
        PersonalCardsMessage personalCardsMessage=new PersonalCardsMessage(Player,hand);
        this.sendMessage(personalCardsMessage);
    }

    @Override
    public void notifyChoiceObjective(String player, ObjectiveCard o) throws RemoteException {
        ChoiceObjectiveMessage choiceObjectiveMessage=new ChoiceObjectiveMessage(player,o);
        this.sendMessage(choiceObjectiveMessage);
    }

    @Override
    public String getSub() throws RemoteException {
        return this.nickname;
    }

    @Override
    public void notifyFirstHand(ResourceCard p1, ResourceCard p2, ResourceCard p3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2) throws RemoteException {
        FirstHandMessage firstHandMessage=new FirstHandMessage(p1,p2,p3,startingCard, o1,o2);
        this.sendMessage(firstHandMessage);
    }

    @Override
    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2) throws RemoteException {
        CommonObjectiveMessage commonObjectiveMessage=new CommonObjectiveMessage(objectiveCard1,objectiveCard2);
        this.sendMessage(commonObjectiveMessage);
    }

    @Override
    public void updateCommonTable(ResourceCard resourceCard, int index) throws RemoteException {
        NotifyCommonTableMessage notifyCommonTableMessage=new NotifyCommonTableMessage(resourceCard,index);
        this.sendMessage(notifyCommonTableMessage);
    }

    @Override
    public void NotifyNumbersOfPlayersReached() throws RemoteException {
        NotifyNumPlayersReachedMessage notifyNumPlayersReachedMessage=new NotifyNumPlayersReachedMessage();
        this.sendMessage(notifyNumPlayersReachedMessage);
    }

    @Override
    public void NotifyLastRound() throws RemoteException {
        LastRoundMessage lastRoundMessage=new LastRoundMessage();
        this.sendMessage(lastRoundMessage);
    }

    @Override
    public void notifyAvailableColors(ArrayList<Color> colors) throws RemoteException {
        AvailableColorMessage availableColorMessage= new AvailableColorMessage(colors);
        this.sendMessage(availableColorMessage);
    }

    @Override
    public void notifyFinalColors(Map<String, Color> colors) throws RemoteException {
        FinalColorsMessage finalColorsMessage=new FinalColorsMessage(colors);
        this.sendMessage(finalColorsMessage);
    }

    @Override
    public void UpdateCrashedPlayer(String nickname, ArrayList<Text> chat, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table, ArrayList<Color> colors) throws RemoteException {
        UpdateCrashedPlayerMessage updateCrashedPlayerMessage=new UpdateCrashedPlayerMessage(nickname,chat,gameState,hand,objectiveCard,boards,points,players,objectiveCards,color,table, colors);
        this.sendMessage(updateCrashedPlayerMessage);
    }

    @Override
    public void UpdateFirst(ArrayList<ResourceCard> commons) throws RemoteException {
        StartingCommonMessage startingCommonMessage=new StartingCommonMessage(commons);
        this.sendMessage(startingCommonMessage);
    }

    @Override
    public void ReceiveGroupText(String sender, String text) throws RemoteException {
        GroupChatMessage groupChatMessage=new GroupChatMessage(sender, text);
        this.sendMessage(groupChatMessage);

    }

    @Override
    public void ReceivePrivateText(String sender, String receiver, String text) throws RemoteException {
        PrivateChatMessage privateChatMessage=new PrivateChatMessage(sender, receiver,text);
        this.sendMessage(privateChatMessage);
    }

    private void subscribeToObservers(){
        gameController.addToObserver((GameSub) this);
        gameController.addToObserver((ChatSub) this);
        gameController.addToObserver((PlayerSub) this);
        gameController.addToObserver((PlayerBoardSub) this);
    }

    private void removeFromObservers(){
        gameController.removeSub((ChatSub)this);
        gameController.removeSub((PlayerSub) this);
        gameController.removeSub((PlayerBoardSub) this);
        gameController.removeSub((GameSub) this);

    }
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
