package it.polimi.ingsw.is24am03.server.model.game;

import it.polimi.ingsw.is24am03.Subscribers.*;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteGameController extends Remote{
    public void createGame(int numPlayers, String nickname) throws RemoteException, GameAlreadyCreatedException;
    public void drawResources(String player) throws EmptyDeckException, PlayerNotInTurnException, InvalidStateException, RemoteException, GameNotExistingException;
    public void drawGold(String player) throws PlayerNotInTurnException, InvalidStateException, EmptyDeckException, RemoteException, GameNotExistingException;
    public void drawTable(String player, int choice) throws PlayerNotInTurnException, InvalidStateException, NullCardSelectedException, RemoteException, GameNotExistingException;
    public void addPlayer(String player) throws FullLobbyException, NicknameAlreadyUsedException, RemoteException, GameNotExistingException;
    public void placeCard(String player, int choice, int i, int j, String face) throws PlayerNotInTurnException, InvalidStateException, RemoteException, GameNotExistingException, CoordinatesOutOfBoundsException, NoCardsAvailableException, RequirementsNotMetException;
    public void setObjectiveCard(String player, int choice) throws PlayerNotInTurnException, RemoteException, GameNotExistingException;

    public void selectStartingFace(String player, String face) throws PlayerNotInTurnException, InvalidStateException, RemoteException, GameNotExistingException;

    void pickColor(String nickname, String color) throws PlayerNotInTurnException, InvalidStateException, ColorAlreadyPickedException, RemoteException, GameNotExistingException;

    public void sendPrivateText(String nickname, String receiver, String text) throws PlayerAbsentException,BadTextException,InvalidStateException,ParametersException, RemoteException;

    public void sendGroupText(String nickname, String text) throws BadTextException, InvalidStateException, RemoteException;


    public void addToObserver(ChatSub subscriber) throws RemoteException;
    public void addToObserver(PlayerSub subscriber) throws RemoteException;
    public void addToObserver(PlayerBoardSub subscriber) throws RemoteException;
    public void addToObserver(GameSub subscriber) throws RemoteException;
    public void removeSub(ChatSub subscriber) throws RemoteException;
    public void removeSub(PlayerSub subscriber) throws RemoteException;
    public void removeSub(PlayerBoardSub subscriber) throws RemoteException;
    public void removeSub(GameSub subscriber) throws RemoteException;

    public void canStart() throws RemoteException;


    void rejoinGame(String nickname) throws RemoteException;
}

