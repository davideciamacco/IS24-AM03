package it.polimi.ingsw.is24am03.server.model.game;

import it.polimi.ingsw.is24am03.Subscribers.*;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteGameController extends Remote{

    public void createGame(int numPlayers, String nickname, String ConnectionType) throws RemoteException, GameAlreadyCreatedException;
    public void drawResources(String player) throws /*throws EmptyDeckException, PlayerNotInTurnException, InvalidStateException*/ RemoteException; /*GameNotExistingException*/
    public void drawGold(String player) throws /*PlayerNotInTurnException, InvalidStateException, EmptyDeckException,  GameNotExistingException*/RemoteException;
    public void drawTable(String player, int choice) throws /*PlayerNotInTurnException, InvalidStateException, NullCardSelectedException,*/ RemoteException /*GameNotExistingException*/;
    public void addPlayer(String player, String ConnectionType) throws FullLobbyException, NicknameAlreadyUsedException, RemoteException, GameNotExistingException;
    public void placeCard(String player, int choice, int i, int j, String face) throws UnknownPlayerException,PlayerNotInTurnException, InvalidStateException, RemoteException, GameNotExistingException, CoordinatesOutOfBoundsException, NoCardsAvailableException, RequirementsNotMetException, ArgumentException;
    public void setObjectiveCard(String player, int choice) throws RemoteException;


    public void rejoinedChief(String player) throws RemoteException;

    public void selectStartingFace(String player, String face)throws RemoteException;
    public void canSelectStartingFace(String player, String face)throws PlayerNotInTurnException,InvalidStateException,GameNotExistingException, RemoteException,ArgumentException, UnknownPlayerException;

    void pickColor(String nickname, String color) throws /*PlayerNotInTurnException, InvalidStateException, ColorAlreadyPickedException,*/ RemoteException/*, GameNotExistingException*/;
public void canPickColor(String nickname, String color) throws PlayerNotInTurnException, InvalidStateException, ColorAlreadyPickedException, RemoteException, GameNotExistingException, UnknownPlayerException, IllegalArgumentException;
    public void sendPrivateText(String nickname, String receiver, String text) throws RemoteException;

    public void sendGroupText(String nickname, String text) throws RemoteException;


    public void addToObserver(ChatSub subscriber) throws RemoteException;
    public void addToObserver(PlayerSub subscriber) throws RemoteException;
    public void addToObserver(PlayerBoardSub subscriber) throws RemoteException;
    public void addToObserver(GameSub subscriber) throws RemoteException;
    public void removeSub(ChatSub subscriber) throws RemoteException;
    public void removeSub(PlayerSub subscriber) throws RemoteException;
    public void removeSub(PlayerBoardSub subscriber) throws RemoteException;
    public void removeSub(GameSub subscriber) throws RemoteException;

    public void canStart() throws RemoteException;

    public void canSetObjectiveCard(String player, int choice) throws PlayerNotInTurnException, GameNotExistingException, RemoteException, InvalidStateException, UnknownPlayerException;
    public void canDrawResources(String player) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException,RemoteException, EmptyDeckException, UnknownPlayerException;
    public void canDrawGold(String player)throws PlayerNotInTurnException,  UnknownPlayerException,InvalidStateException, GameNotExistingException, EmptyDeckException,RemoteException;
    public void canDrawTable(String player, int choice) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, RemoteException, NullCardSelectedException, UnknownPlayerException;
   // public void canPlaceCard(String player, int choice, int i, int j,String face) throws PlayerNotInTurnException, InvalidStateException, RemoteException, GameNotExistingException, CoordinatesOutOfBoundsException, NoCardsAvailableException, RequirementsNotMetException, RemoteException;

public void canSendGroupChat(String sender, String text) throws  BadTextException,InvalidStateException, RemoteException, UnknownPlayerException, GameNotExistingException;
public void canSendPrivateChat(String sender, String receiver, String text) throws  UnknownPlayerException,GameNotExistingException,PlayerAbsentException, BadTextException, InvalidStateException, ParametersException, RemoteException;

    void rejoinGame(String nickname, String ConnectionType) throws RemoteException, InvalidStateException, GameNotExistingException, UnknownPlayerException;

    void setLastHeartBeat(String nickname) throws RemoteException;
}

