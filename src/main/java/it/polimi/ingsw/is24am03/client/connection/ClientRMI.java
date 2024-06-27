package it.polimi.ingsw.is24am03.client.connection;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerBoardSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.client.view.ViewInterface;
import it.polimi.ingsw.is24am03.client.clientModel.ClientModel;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.game.RemoteGameController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientRMI implements Client{
    Registry registry;
    private RemoteGameController gameController;
    private ViewInterface view;
    private final String ip;
    private final int port;
    private boolean hasJoined;
    private String nickname;
    private ClientModel clientModel;
    private final ScheduledExecutorService heartbeatScheduler = Executors.newScheduledThreadPool(1);


    /**
     *
     * @param hostName
     * @param portNumber
     * @param view
     */
    public ClientRMI(String hostName, int portNumber, ViewInterface view) {
        boolean connected = false;
        this.hasJoined=false;
        RemoteGameController temp = null;
        this.ip = hostName;
        this.port = portNumber;
        this.view = view;
        while(!connected){
            try {
                this.registry = LocateRegistry.getRegistry(hostName, portNumber);
                String remoteObjectName = "game_controller";
                temp = (RemoteGameController) registry.lookup(remoteObjectName);
                connected = true;
            }
            catch(RemoteException e){}
            catch(NotBoundException e){}
        }
        this.gameController = temp;
        startHeartbeatSender();
    }

    /**
     *
     * @param nPlayers
     * @param nickname
     */
    public void CreateGame(int nPlayers, String nickname){

        try {
            clientModel=new ClientModel(nickname,view);
            this.gameController.createGame(nPlayers, nickname, "RMI");
            view.confirmCreate();
            this.nickname = nickname;
            this.subscribeToObservers();
            hasJoined=true;
        } catch (IllegalArgumentException e) {
            view.drawError("Invalid arguments");
        } catch (GameAlreadyCreatedException e) {
            view.drawError("Game already created");
        } catch (RemoteException e){
        }
        System.out.flush();
    }

    /**
     *
     * @param nickname
     */
    public void JoinGame(String nickname){
        try{
            if(!hasJoined){
                clientModel=new ClientModel(nickname, view);
                this.gameController.addPlayer(nickname, "RMI");
                view.confirmJoin();
                hasJoined=true;
                this.nickname=nickname;
                this.subscribeToObservers();
                this.gameController.canStart();
            }
            else{
                view.drawError("Already joined");
            }
        }
        catch(IllegalArgumentException e)
        {
            view.drawError("Nickname not allowed");
        }
        catch(FullLobbyException e)
        {
            view.drawError("Lobby is full");
        }
        catch(NicknameAlreadyUsedException e)
        {
            view.drawError("Nickname already used");
        }
        catch(GameNotExistingException e )
        {
            view.drawError("Game not existing");
        }
        catch (RemoteException e)
        {}
        System.out.flush();
    }

    /**
     *
     * @param color
     */
    public void PickColor(String color){
        try {
            this.gameController.canPickColor(nickname, color);
            this.gameController.pickColor(nickname,color);
            hasJoined=true;
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e){
            clientModel.printNotifications("Action not allowed in this state");
        } catch (ColorAlreadyPickedException e) {
            clientModel.printNotifications("Color not available");
        } catch (RemoteException e)
        {
        } catch (GameNotExistingException e) {
            view.drawError("Game not existing");
        }
        catch (UnknownPlayerException e){
            view.drawError(e.getMessage());
        }
        System.out.flush();
    }

    /**
     *
     * @param face
     */
    public void ChooseStartingCardSide(String face){
        try {
            this.gameController.canSelectStartingFace(nickname,face);
            clientModel.printNotifications("Starting card side chosen successfully");
            this.gameController.selectStartingFace(nickname,face);
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e){
            clientModel.printNotifications("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("Game doesn't exist");
        }catch (ArgumentException e){
            view.drawError(e.getMessage());
        } catch (RemoteException e)
        {
        }catch (UnknownPlayerException e){
            clientModel.printNotifications(e.getMessage());
        }
        System.out.flush();
    }

    /**
     *
     * @param view
     */
    @Override
    public void setGUI(ViewInterface view) {
            this.view=view;
    }

    /**
     *
     * @param view
     */
    @Override
    public void setCLI(ViewInterface view) {
        this.view=view;
    }

    /**
     *
     * @param choice
     * @param i
     * @param j
     * @param face
     */
    public void PlaceCard(int choice,int i,int j,String face){
        try {
            this.gameController.placeCard(nickname,choice,i,j,face);
        }catch (ArgumentException e){
            clientModel.printNotifications("Invalid command");
        } catch (UnknownPlayerException e){
            view.drawError(e.getMessage());
        }
        catch(PositionOccupiedException e){
            clientModel.printNotifications("Position is not empty");
        } catch (CoordinatesOutOfBoundsException e) {
            clientModel.printNotifications("Coordinates out of bound");
        } catch (NoCardsAvailableException e){
            clientModel.printNotifications("Card can't be placed in these coordinates");
        } catch (RequirementsNotMetException e){
            clientModel.printNotifications("Gold card requirements not satisfied");
        }catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e){
            clientModel.printNotifications("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("GameNotExists exception");
        }catch (RemoteException e){}
        System.out.flush();
    }

    /**
     *
     */
    public void DrawGold(){
        try {
            this.gameController.canDrawGold(nickname);
            clientModel.printNotifications("Gold card drawn successfully");
            this.gameController.drawGold(nickname);
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e){
            clientModel.printNotifications("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("GameNotExists exception");
        }  catch (EmptyDeckException e) {
            clientModel.printNotifications("Empty deck exception");
        }catch (RemoteException e){

        }
        catch (UnknownPlayerException e){
            view.drawError(e.getMessage());
        }
        System.out.flush();
    }

    /**
     *
     */
    public void DrawResource(){
        try {
            this.gameController.canDrawResources(nickname);
            clientModel.printNotifications("Resource card drawn successfully");
            this.gameController.drawResources(nickname);
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e){
            clientModel.printNotifications("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("GameNotExists exception");
        }  catch (EmptyDeckException e) {
            clientModel.printNotifications("Empty deck exception");
        }catch (UnknownPlayerException e){
            view.drawError(e.getMessage());
        }
        catch (RemoteException e){

        }
        System.out.flush();
    }

    /**
     *
     * @param choice
     */
    public void DrawTable(int choice){
        try {
            this.gameController.canDrawTable(nickname,choice);
            clientModel.printNotifications("Card drawn successfully");
            this.gameController.drawTable(nickname,choice);
        } catch (UnknownPlayerException e){
            view.drawError(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e){
            clientModel.printNotifications("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("Game not existing exception");
        }  catch (NullCardSelectedException e) {
            clientModel.printNotifications("NullCardSelectedException exception");
        }catch (RemoteException e){

        }
        System.out.flush();
    }

    /**
     *
     * @param choice
     */
    public void ChooseObjectiveCard(int choice){
        try{
            this.gameController.canSetObjectiveCard(nickname, choice);
            clientModel.printNotifications("Objective card selected successfully");
            this.gameController.setObjectiveCard(nickname,choice);
        } catch (GameNotExistingException e) {
            view.drawError("Game not existing");
        } catch (UnknownPlayerException e){
            view.drawError(e.getMessage());
        }
        catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        }
        catch(InvalidStateException e){
            clientModel.printNotifications("Action not allowed in this state");
        }
        catch(IllegalArgumentException e){
            clientModel.printNotifications("Invalid arguments");
        } catch(RemoteException e) {

        }
    }

    /**
     *
     * @param nickname
     */
    public void RejoinGame(String nickname){
        try{
            if(!hasJoined){
                this.gameController.rejoinGame(nickname, "RMI");
                clientModel=new ClientModel(nickname, view);
                hasJoined=true;
                this.nickname=nickname;
                this.subscribeToObservers();
                this.gameController.rejoinedChief(nickname);
            }
            else{
                clientModel.printNotifications("Already joined");
            }
        }
        catch(UnknownPlayerException e){
            view.drawError(e.getMessage());
        }
        catch (RemoteException e){

        } catch (InvalidStateException e) {
            view.drawError("Action not allowed in this state");
        }
        catch (GameNotExistingException e){
            view.drawError("Game doesn't exist");
        }
    }

    /**
     *
      * @param text
     */
    public void sendGroupText(String text){
        try {
            this.gameController.canSendGroupChat(this.nickname, text);
            this.gameController.sendGroupText(this.nickname, text);
        } catch (BadTextException | InvalidStateException e1) {
            clientModel.printNotifications(e1.getMessage());
        } catch(RemoteException e){}
        catch (UnknownPlayerException e){
            view.drawError(e.getMessage());
        }
        catch (GameNotExistingException e){
            view.drawError("Game doesn't exist");
        }
    }

    /**
     *
     * @param receiver
     * @param text
     */
    public void sendPrivateText(String receiver, String text){
        try{
            this.gameController.canSendPrivateChat(this.nickname, receiver,text);
            this.gameController.sendPrivateText(this.nickname, receiver,text);
        } catch (BadTextException | InvalidStateException | PlayerAbsentException | ParametersException e) {
            clientModel.printNotifications(e.getMessage());
        } catch(RemoteException e){}
        catch (UnknownPlayerException e){
            view.drawError(e.getMessage());
        }
        catch (GameNotExistingException e){
            view.drawError("Game doesn't exist");
        }
    }

    /**
     *
     */
    private void subscribeToObservers(){
        try {
            gameController.addToObserver((GameSub) clientModel);
            gameController.addToObserver((ChatSub) clientModel);
            gameController.addToObserver((PlayerSub) clientModel);
            gameController.addToObserver((PlayerBoardSub) clientModel);
        }catch (RemoteException ignored){}
    }

    /**
     *
     */
    private void removeFromObservers(){
        try {
            if(clientModel!=null) {
                gameController.removeSub((ChatSub) clientModel);
                gameController.removeSub((PlayerSub) clientModel);
                gameController.removeSub((PlayerBoardSub) clientModel);
                gameController.removeSub((GameSub) clientModel);
            }
        }catch (RemoteException ignored){}

    }

    /**
     *
     */
    private void startHeartbeatSender() {
        long heartbeatInterval = 5000;
        heartbeatScheduler.scheduleAtFixedRate(this::sendHeartbeat, 0, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    /**
     *
     */
    private void sendHeartbeat() {
        try {
            gameController.setLastHeartBeat(nickname);
        } catch (RemoteException e) {
            System.out.println("Server disconnected. Closing client...");
            System.exit(0);
        }
    }

}


