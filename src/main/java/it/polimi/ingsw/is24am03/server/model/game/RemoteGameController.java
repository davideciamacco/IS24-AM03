package it.polimi.ingsw.is24am03.server.model.game;

import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteGameController extends Remote{
    public void createGame(int numPlayers, String nickname) throws RemoteException, GameAlreadyCreatedException;
    public void drawResources(String player) throws EmptyDeckException, PlayerNotInTurnException, InvalidStateException, RemoteException;
    public void drawGold(String player) throws PlayerNotInTurnException, InvalidStateException, EmptyDeckException, RemoteException;
    public void drawTable(String player, int choice) throws PlayerNotInTurnException, InvalidStateException, NullCardSelectedException, RemoteException;
    public void addPlayer(String player) throws FullLobbyException, NicknameAlreadyUsedException, RemoteException, InvalidStateException;
    public void placeCard(String player, int choice, int i, int j, boolean face) throws PlayerNotInTurnException, InvalidStateException, RemoteException;
    public void setObjectiveCard(String player, int choice) throws PlayerNotInTurnException, RemoteException;

    public void selectStartingFace(String player, boolean face) throws PlayerNotInTurnException, InvalidStateException, RemoteException;

    void pickColor(String nickname, String color) throws PlayerNotInTurnException, InvalidStateException, ColorAlreadyPickedException, RemoteException;
}

