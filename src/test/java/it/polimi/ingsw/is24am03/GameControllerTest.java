package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.server.controller.GameController;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.player.Player;
import it.polimi.ingsw.is24am03.server.model.player.PlayerBoard;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;



class GameControllerTest {

    @Test
    void createGame() throws RemoteException {
        GameController gc1 = new GameController();
        assertDoesNotThrow(() -> gc1.createGame(3, "HostPlayer", "TCP"));
        assertNotNull(gc1.getGameModel());
        assertEquals(3, gc1.getGameModel().getNumPlayers());
        assertEquals("HostPlayer", gc1.getGameModel().getPlayers().get(0).getNickname());

        GameController gc2 = new GameController();

        assertThrows(IllegalArgumentException.class, () -> gc2.createGame(1, "HostPlayer", "TCP"));
        assertNull(gc2.getGameModel());

        assertThrows(IllegalArgumentException.class, () -> gc2.createGame(5, "HostPlayer", "TCP"));
        assertNull(gc2.getGameModel());

        assertThrows(IllegalArgumentException.class, () -> gc2.createGame(4, "", "TCP"));
        assertNull(gc2.getGameModel());
    }

    @Test
    void selectStartingFace() throws RemoteException {
        GameController gc5 = new GameController();
        assertDoesNotThrow(() -> gc5.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc5.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc5.getGameModel().addSub(gs1);
        gc5.getGameModel().addSub(gs2);
        gc5.getGameModel().addSub(ps1);
        gc5.getGameModel().addSub(ps2);
        gc5.canStart();
        gc5.getGameModel().setGameState(State.STARTING);

        Player currentPlayer1 = gc5.getGameModel().getPlayers().get(gc5.getGameModel().getCurrentPlayer());
        currentPlayer1.setPlayerBoard(new PlayerBoard(currentPlayer1));
        assertDoesNotThrow(() -> gc5.canSelectStartingFace(currentPlayer1.getNickname(), "FRONT"));
        gc5.selectStartingFace(currentPlayer1.getNickname(), "FRONT");
        assertThrows(PlayerNotInTurnException.class, () -> gc5.canSelectStartingFace(currentPlayer1.getNickname(), "FRONT"));

    }

    @Test
    void setObjectiveCard() throws RemoteException {
        GameController gc5 = new GameController();
        assertDoesNotThrow(() -> gc5.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc5.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc5.getGameModel().addSub(gs1);
        gc5.getGameModel().addSub(gs2);
        gc5.getGameModel().addSub(ps1);
        gc5.getGameModel().addSub(ps2);
        gc5.canStart();
        gc5.getGameModel().setGameState(State.OBJECTIVE);

        Player currentPlayer1 = gc5.getGameModel().getPlayers().get(gc5.getGameModel().getCurrentPlayer());
        assertDoesNotThrow(() -> gc5.canSetObjectiveCard(currentPlayer1.getNickname(), 1));
        gc5.setObjectiveCard(currentPlayer1.getNickname(), 1);
        assertThrows(PlayerNotInTurnException.class, () -> gc5.canSetObjectiveCard(currentPlayer1.getNickname(), 2));

        Player currentPlayer2 = gc5.getGameModel().getPlayers().get(gc5.getGameModel().getCurrentPlayer());
        assertThrows(IllegalArgumentException.class, () -> gc5.canSetObjectiveCard(currentPlayer2.getNickname(), 3));

    }

    @Test
    void drawResources() throws RemoteException {
        GameController gc4 = new GameController();
        assertDoesNotThrow(() -> gc4.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc4.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc4.getGameModel().addSub(gs1);
        gc4.getGameModel().addSub(gs2);
        gc4.getGameModel().addSub(ps1);
        gc4.getGameModel().addSub(ps2);
        gc4.canStart();
        gc4.getGameModel().setGameState(State.DRAWING);

        String currentPlayer1 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();

        assertDoesNotThrow(() -> gc4.canDrawResources(currentPlayer1));
        gc4.drawResources(currentPlayer1);
        assertEquals(4, gc4.getGameModel().getPlayers().get(0).getHand().size());

        assertThrows(PlayerNotInTurnException.class, () -> gc4.canDrawResources(currentPlayer1));
        String currentPlayer2 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();

        assertThrows(InvalidStateException.class, () -> gc4.canDrawResources(currentPlayer2));

    }

    @Test
    void drawGold() throws RemoteException {
        GameController gc4 = new GameController();
        assertDoesNotThrow(() -> gc4.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc4.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc4.getGameModel().addSub(gs1);
        gc4.getGameModel().addSub(gs2);
        gc4.getGameModel().addSub(ps1);
        gc4.getGameModel().addSub(ps2);
        gc4.canStart();
        gc4.getGameModel().setGameState(State.DRAWING);

        String currentPlayer1 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();

        assertDoesNotThrow(() -> gc4.canDrawGold(currentPlayer1));
        gc4.drawGold(currentPlayer1);
        assertEquals(4, gc4.getGameModel().getPlayers().get(0).getHand().size());

        assertThrows(PlayerNotInTurnException.class, () -> gc4.canDrawGold(currentPlayer1));
        String currentPlayer2 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();

        //State s = gc4.getGameModel().getGameState();

        assertThrows(InvalidStateException.class, () -> gc4.canDrawGold(currentPlayer2));
    }

    @Test
    void drawTable() throws RemoteException {
        GameController gc7 = new GameController();
        assertDoesNotThrow(() -> gc7.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc7.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc7.getGameModel().addSub(gs1);
        gc7.getGameModel().addSub(gs2);
        gc7.getGameModel().addSub(ps1);
        gc7.getGameModel().addSub(ps2);
        gc7.canStart();
        gc7.getGameModel().setGameState(State.DRAWING);

        String currentPlayer1 = gc7.getGameModel().getPlayers().get(gc7.getGameModel().getCurrentPlayer()).getNickname();

        assertThrows(IllegalArgumentException.class, () -> gc7.canDrawTable(currentPlayer1, 0));

        assertDoesNotThrow(() -> gc7.canDrawTable(currentPlayer1, 1));
        gc7.drawTable(currentPlayer1, 1);
        assertEquals(4, gc7.getGameModel().getPlayers().get(0).getHand().size());

        assertThrows(PlayerNotInTurnException.class, () -> gc7.canDrawTable(currentPlayer1, 1));

        String currentPlayer2 = gc7.getGameModel().getPlayers().get(gc7.getGameModel().getCurrentPlayer()).getNickname();
        //State s = gc7.getGameModel().getGameState();
        assertThrows(InvalidStateException.class, () -> gc7.canDrawTable(currentPlayer2, 1));
    }

    @Test
    void addPlayer() throws RemoteException {
        GameController gc3 = new GameController();
        assertDoesNotThrow(() -> gc3.createGame(3, "Giorgio", "TCP"));
        assertEquals(1, gc3.getGameModel().getPlayers().size());

        assertDoesNotThrow(() -> gc3.addPlayer("Andrea", "TCP"));
        assertEquals(2, gc3.getGameModel().getPlayers().size());
        assertEquals("Andrea", gc3.getGameModel().getPlayers().get(1).getNickname());

        assertThrows(NicknameAlreadyUsedException.class, () -> gc3.addPlayer("Andrea", "TCP"));
        assertEquals(2, gc3.getGameModel().getPlayers().size());

        assertDoesNotThrow(() -> gc3.addPlayer("Marco", "TCP"));
        assertEquals(3, gc3.getGameModel().getPlayers().size());
        assertThrows(FullLobbyException.class, () -> gc3.addPlayer("Giulia", "TCP"));
    }


    @Test
    void placeCard() throws RemoteException {
        GameController gc6 = new GameController();
        assertDoesNotThrow(() -> gc6.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc6.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc6.getGameModel().addSub(gs1);
        gc6.getGameModel().addSub(gs2);
        gc6.getGameModel().addSub(ps1);
        gc6.getGameModel().addSub(ps2);
        gc6.canStart();
        Player currentPlayer2;
        Player currentPlayer1 = gc6.getGameModel().getPlayers().get(gc6.getGameModel().getCurrentPlayer());
        if (currentPlayer1.equals(gc6.getGameModel().getPlayers().get(0)))
            currentPlayer2 = gc6.getGameModel().getPlayers().get(1);
        else
            currentPlayer2 = gc6.getGameModel().getPlayers().get(0);
        currentPlayer1.setPlayerBoard(new PlayerBoard(currentPlayer1));
        currentPlayer1.getPlayerBoard().placeStartingCard(currentPlayer1.getStartingCard(), true);

        assertThrows(InvalidStateException.class, () -> gc6.placeCard(currentPlayer1.getNickname(), 1, 41, 41, "FRONT"));

        gc6.getGameModel().setGameState(State.PLAYING);

        assertThrows(PlayerNotInTurnException.class, () -> gc6.placeCard(currentPlayer2.getNickname(), 1, 41, 41, "FRONT"));
        assertThrows(ArgumentException.class, () -> gc6.placeCard(currentPlayer1.getNickname(), 0, 41, 41, "FRONT"));
        assertDoesNotThrow(() -> gc6.placeCard(currentPlayer1.getNickname(), 1, 41, 41, "FRONT"));
    }
}