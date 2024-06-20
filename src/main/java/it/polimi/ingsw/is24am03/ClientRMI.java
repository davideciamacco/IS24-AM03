package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerBoardSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.game.RemoteGameController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;


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
        addShutdownHook();
    }


    public void CreateGame(int nPlayers, String nickname){

        try {
            clientModel=new ClientModel(nickname,view);
            this.gameController.createGame(nPlayers, nickname, "RMI");

            System.out.println("Game created successfully");
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

    public void JoinGame(String nickname){
        try{
            if(!hasJoined){
                clientModel=new ClientModel(nickname, view);
                this.gameController.addPlayer(nickname, "RMI");
                System.out.println("Joined successfully");
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
        {

        }
        System.out.flush();
    }

    public void PickColor(String color)
    {
        try {
            this.gameController.canPickColor(nickname, color);
            System.out.println("Color picked successfully");
            this.gameController.pickColor(nickname,color);
            hasJoined=true;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            System.out.println("Not your turn");
        } catch (InvalidStateException e){
            System.out.println("Action not allowed in this state");
        } catch (ColorAlreadyPickedException e) {
            System.out.println("Color not available");
        } catch (RemoteException e)
        {

        } catch (GameNotExistingException e) {
            System.out.println("Game not existing");
        }


        System.out.flush();
    }
    public void ChooseStartingCardSide(String face)
    {
        try {
            this.gameController.canSelectStartingFace(nickname,face);
            System.out.println("Starting card side chosen successfully");
            this.gameController.selectStartingFace(nickname,face);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            System.out.println("Not your turn");
        } catch (InvalidStateException e){
            System.out.println("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            System.out.println("GameNotExists exception");
        }catch (ArgumentException e){
            System.out.println(e.getMessage());
        } catch (RemoteException e)
        {

        }
        System.out.flush();
    }

    @Override
    public void setGUI(ViewInterface view) {
            this.view=view;
    }

    @Override
    public void setCLI(ViewInterface view) {
        this.view=view;
    }

    public void PlaceCard(int choice,int i,int j,String face){
        try {
            this.gameController.placeCard(nickname,choice,i,j,face);
            //System.out.println("Card placed successfully");
        }catch (ArgumentException e){
            System.out.println(e.getMessage());
        }
        catch(PositionOccupiedException e){
            System.out.println("Position is not empty");
        } catch (CoordinatesOutOfBoundsException e) {
            System.out.println("Coordinates out of bound");
        } catch (NoCardsAvailableException e){
            System.out.println("Card can't be placed in these coordinates");
        } catch (RequirementsNotMetException e){
            System.out.println("Gold card requirements not satisfied");
        }catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            System.out.println("Not your turn");
        } catch (InvalidStateException e){
            System.out.println("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            System.out.println("GameNotExists exception");
        }catch (RemoteException e){}
        System.out.flush();
    }
    public void DrawGold(){
        try {
            this.gameController.canDrawGold(nickname);
            System.out.println("Gold card drawn successfully");
            this.gameController.drawGold(nickname);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            System.out.println("Not your turn");
        } catch (InvalidStateException e){
            System.out.println("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            System.out.println("GameNotExists exception");
        }  catch (EmptyDeckException e) {
            System.out.println("Empty deck exception");
        }catch (RemoteException e){

        }
        System.out.flush();
    }
    public void DrawResource(){
        try {
            this.gameController.canDrawResources(nickname);
            System.out.println("Resource card drawn successfully");
            this.gameController.drawResources(nickname);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            System.out.println("Not your turn");
        } catch (InvalidStateException e){
            System.out.println("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            System.out.println("GameNotExists exception");
        }  catch (EmptyDeckException e) {
            System.out.println("Empty deck exception");
        }catch (RemoteException e){

        }
        System.out.flush();
    }

    public void DrawTable(int choice){
        try {
            this.gameController.canDrawTable(nickname,choice);
            System.out.println("Card drawn successfully");
            this.gameController.drawTable(nickname,choice);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            System.out.println("Not your turn");
        } catch (InvalidStateException e){
            System.out.println("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            System.out.println("Game not existing exception");
        }  catch (NullCardSelectedException e) {
            System.out.println("NullCardSelectedException exception");
        }catch (RemoteException e){

        }
        System.out.flush();
    }

    public void ChooseObjectiveCard(int choice){
        try{
            this.gameController.canSetObjectiveCard(nickname, choice);
            System.out.println("Objective card selected successfully");
            this.gameController.setObjectiveCard(nickname,choice);
        } catch (GameNotExistingException e) {
            System.out.println("Game not existing");
        } catch (PlayerNotInTurnException e) {
            System.out.println("Not your turn");
        }
        catch(InvalidStateException e){
            System.out.println("Action not allowed in this state");
        }
        catch(IllegalArgumentException e){
            System.out.println("Invalid arguments");
        } catch(RemoteException e) {

        }
    }

    public void RejoinGame(String nickname){
        try{
            if(!hasJoined){
                clientModel=new ClientModel(nickname, view);
                this.gameController.rejoinGame(nickname);
                System.out.println("Rejoined successfully");
                hasJoined=true;
                this.nickname=nickname;
                this.subscribeToObservers();
                this.gameController.rejoinedChief(nickname);
            }
            else{
                System.out.println("Already joined");
            }
        }
        catch(IllegalArgumentException e){
            System.out.println("Player not existing");
        }
        catch (RemoteException e){

        } catch (InvalidStateException e) {
            System.out.println("Action not allowed in this state");
        }
    }

    //metodi per gestire invio messaggio chat
    public void sendGroupText(String text){
        try {
            this.gameController.canSendGroupChat(this.nickname, text);
            System.out.println("Group text sent successfully");
            this.gameController.sendGroupText(this.nickname, text);
        } catch (BadTextException | InvalidStateException e1) {
            System.out.println(e1.getMessage());
        } catch(RemoteException e){}


    }
    public void sendPrivateText(String receiver, String text){
        try{
            this.gameController.canSendPrivateChat(this.nickname, receiver,text);
            System.out.println("Private text sent successfully");
            this.gameController.sendPrivateText(this.nickname, receiver,text);
        } catch (BadTextException | InvalidStateException | PlayerAbsentException | ParametersException e) {
            System.out.println(e.getMessage());
        } catch(RemoteException e){}
    }

    private void subscribeToObservers(){
        try {
            gameController.addToObserver((GameSub) clientModel);
            gameController.addToObserver((ChatSub) clientModel);
            gameController.addToObserver((PlayerSub) clientModel);
            gameController.addToObserver((PlayerBoardSub) clientModel);
        }catch (RemoteException e){}
    }

    private void removeFromObservers(){
        try {
            gameController.removeSub((ChatSub) clientModel);
            gameController.removeSub((PlayerSub) clientModel);
            gameController.removeSub((PlayerBoardSub) clientModel);
            gameController.removeSub((GameSub) clientModel);
        }catch (RemoteException e){}

    }

    private void startHeartbeatSender() {
        long heartbeatInterval = 5000; // 5 seconds
        heartbeatScheduler.scheduleAtFixedRate(this::sendHeartbeat, 0, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    private void sendHeartbeat() {
        try {
            gameController.setLastHeartBeat(nickname);
        } catch (RemoteException e) {
        }
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Arresto");
            removeFromObservers();
        }));
    }
}


