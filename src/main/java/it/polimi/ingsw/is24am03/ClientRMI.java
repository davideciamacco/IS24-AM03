package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.game.RemoteGameController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI implements Client{
    Registry registry;
    private String username;
    private RemoteGameController gameController;
    private final CliView view;
    //private Game gameModel;
    private final String ip;
    private final int port;
    private boolean hasJoined;
    private boolean connectionClosed;
    private String nickname;

    public ClientRMI(String hostName, int portNumber, CliView view) {
        boolean connected = false;
        this.connectionClosed = false;
        this.hasJoined=false;
        RemoteGameController temp = null;
        this.ip = hostName;
        this.port = portNumber;
        this.view = view;
        while(!connected){
            try{
                this.registry= LocateRegistry.getRegistry(hostName, portNumber);
                String remoteObjectName = "game_controller";
                temp =  (RemoteGameController) registry.lookup(remoteObjectName);

                connected = true;
            }catch(RemoteException e){
            }catch(NotBoundException e){
            }

        }

        this.gameController = temp;
    }


    public void CreateGame(int nPlayers, String nickname){

        try {
            this.gameController.createGame(nPlayers, nickname);
            System.out.println("Game created successfully");
            this.nickname = nickname;
            hasJoined=true;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments");
        } catch (GameAlreadyCreatedException e) {
            System.out.println("Game already created");
        } catch (RemoteException e){

        }
        System.out.flush();
    }

    public void JoinGame(String nickname){
        try{
            this.gameController.addPlayer(nickname);
            System.out.println("Joined successfully");
            hasJoined=true;
            this.nickname=nickname;
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Nickname not allowed");
        }
        catch(FullLobbyException e)
        {
            System.out.println("Lobby is full");
        }
        catch(NicknameAlreadyUsedException e)
        {
            System.out.println("Nickname already used");
        }
        catch(GameNotExistingException e )
        {
            System.out.println("Game not existing");
        }
        catch (RemoteException e)
        {

        }
        System.out.flush();
    }

    public void PickColor(String color)
    {
        try {
            this.gameController.pickColor(nickname, color);
            System.out.println("Color picked successfully");
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
            this.gameController.selectStartingFace(nickname,face);
            System.out.println("Starting card side chosen successfully");

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            System.out.println("Not your turn");
        } catch (InvalidStateException e){
            System.out.println("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            System.out.println("GameNotExists exception");
        } catch (RemoteException e)
        {

        }
        System.out.flush();
    }
    public void PlaceCard(int choice,int i,int j,String face){
        try {
            this.gameController.placeCard(nickname,choice,i,j,face);
            System.out.println("Card placed successfully");

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            System.out.println("Not your turn");
        } catch (InvalidStateException e){
            System.out.println("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            System.out.println("GameNotExists exception");
        } catch (RemoteException e)
        {

        }
        System.out.flush();
    }
    public void DrawGold(){
        try {
            this.gameController.drawGold(nickname);
            System.out.println("Gold card drawn successfully");
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
            this.gameController.drawResources(nickname);
            System.out.println("Resource card drawn successfully");
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
            this.gameController.drawTable(nickname,choice);
            System.out.println("Card drawn successfully");
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
            this.gameController.setObjectiveCard(nickname, choice);
            System.out.println("Objective card selected successfully");
        } catch (GameNotExistingException e) {
            System.out.println("Game not existing");
        } catch (PlayerNotInTurnException e) {
            System.out.println("Not your turn");
        }
        catch(IllegalArgumentException e){
            System.out.println("Invalid arguments");
        } catch(RemoteException e) {

        }
    }
}