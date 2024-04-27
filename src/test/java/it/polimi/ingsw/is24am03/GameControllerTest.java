package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.controller.GameController;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.exceptions.FullLobbyException;
import it.polimi.ingsw.is24am03.server.model.exceptions.InvalidStateException;
import it.polimi.ingsw.is24am03.server.model.exceptions.NicknameAlreadyUsedException;
import it.polimi.ingsw.is24am03.server.model.exceptions.PlayerNotInTurnException;
import it.polimi.ingsw.is24am03.server.model.player.Player;
import it.polimi.ingsw.is24am03.server.model.player.PlayerBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    void createGame() {
        GameController gc1 = new GameController();
        assertDoesNotThrow(() -> gc1.createGame(3, "HostPlayer"));
        assertNotNull(gc1.getGameModel());
        assertEquals(3, gc1.getGameModel().getNumPlayers());
        assertEquals("HostPlayer", gc1.getGameModel().getPlayers().get(0).getNickname());

        GameController gc2 = new GameController();

        assertThrows(IllegalArgumentException.class, () -> gc2.createGame(1, "HostPlayer"));
        assertNull(gc2.getGameModel());

        assertThrows(IllegalArgumentException.class, () -> gc2.createGame(5, "HostPlayer"));
        assertNull(gc2.getGameModel());

        assertThrows(IllegalArgumentException.class, () -> gc2.createGame(4, ""));
        assertNull(gc2.getGameModel());
    }

    @Test
    void selectStartingFace() {
        GameController gc5 = new GameController();
        gc5.createGame(2, "Player1");
        assertDoesNotThrow(() -> gc5.addPlayer("Player2"));
        gc5.getGameModel().setGameState(State.PREPARATION_1);

        Player currentPlayer1 = gc5.getGameModel().getPlayers().get(gc5.getGameModel().getCurrentPlayer());
        currentPlayer1.setPlayerBoard(new PlayerBoard(currentPlayer1));
        assertDoesNotThrow(() -> gc5.selectStartingFace(currentPlayer1.getNickname(), true));

        assertThrows(PlayerNotInTurnException.class, () -> gc5.selectStartingFace(currentPlayer1.getNickname(), true));

    }

    @Test
    void setObjectiveCard() {
        GameController gc5 = new GameController();
        gc5.createGame(2, "Player1");
        assertDoesNotThrow(() -> gc5.addPlayer("Player2"));
        gc5.getGameModel().setGameState(State.PREPARATION_2);

        Player currentPlayer1 = gc5.getGameModel().getPlayers().get(gc5.getGameModel().getCurrentPlayer());
        assertDoesNotThrow(() -> gc5.setObjectiveCard(currentPlayer1.getNickname(), 1));

        assertThrows(PlayerNotInTurnException.class, () -> gc5.setObjectiveCard(currentPlayer1.getNickname(), 2));

        Player currentPlayer2 = gc5.getGameModel().getPlayers().get(gc5.getGameModel().getCurrentPlayer());
        assertThrows(IllegalArgumentException.class, () -> gc5.setObjectiveCard(currentPlayer2.getNickname(), 3));

    }

    @Test
    void drawResources() {
        GameController gc4 = new GameController();
        gc4.createGame(2, "Player1");
        assertDoesNotThrow(() -> gc4.addPlayer("Player2"));
        gc4.getGameModel().setGameState(State.DRAWING);

        String currentPlayer1 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();

        assertDoesNotThrow(() -> gc4.drawResources(currentPlayer1));
        assertEquals(4, gc4.getGameModel().getPlayers().get(0).getHand().size());

        assertThrows(PlayerNotInTurnException.class, () -> gc4.drawResources(currentPlayer1));
        String currentPlayer2 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();

        assertThrows(InvalidStateException.class, () -> gc4.drawResources(currentPlayer2));

    }

    @Test
    void drawGold() {
        GameController gc4 = new GameController();
        gc4.createGame(2, "Player1");
        assertDoesNotThrow(() -> gc4.addPlayer("Player2"));
        gc4.getGameModel().setGameState(State.DRAWING);

        String currentPlayer1 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();

        assertDoesNotThrow(() -> gc4.drawGold(currentPlayer1));
        assertEquals(4, gc4.getGameModel().getPlayers().get(0).getHand().size());

        assertThrows(PlayerNotInTurnException.class, () -> gc4.drawGold(currentPlayer1));
        String currentPlayer2 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();

        State s = gc4.getGameModel().getGameState();

        assertThrows(InvalidStateException.class, () -> gc4.drawGold(currentPlayer2));
    }

    @Test
    void drawTable() {
        GameController gc7 = new GameController();
        gc7.createGame(2, "Player1");
        assertDoesNotThrow(() -> gc7.addPlayer("Player2"));
        gc7.getGameModel().setGameState(State.DRAWING);

        String currentPlayer1 = gc7.getGameModel().getPlayers().get(gc7.getGameModel().getCurrentPlayer()).getNickname();

        assertThrows(IllegalArgumentException.class, ()->gc7.drawTable(currentPlayer1, 0));

        assertDoesNotThrow(() -> gc7.drawTable(currentPlayer1, 1));
        assertEquals(4, gc7.getGameModel().getPlayers().get(0).getHand().size());

        assertThrows(PlayerNotInTurnException.class, () -> gc7.drawTable(currentPlayer1, 1));

        String currentPlayer2 = gc7.getGameModel().getPlayers().get(gc7.getGameModel().getCurrentPlayer()).getNickname();
        //State s = gc7.getGameModel().getGameState();
        assertThrows(InvalidStateException.class, () -> gc7.drawTable(currentPlayer2,1));
    }

    @Test
    void addPlayer() {
        GameController gc3 = new GameController();
        gc3.createGame(3, "Giorgio");
        assertEquals(1, gc3.getGameModel().getPlayers().size());

        assertDoesNotThrow(() -> gc3.addPlayer("Andrea"));
        assertEquals(2, gc3.getGameModel().getPlayers().size());
        assertEquals("Andrea", gc3.getGameModel().getPlayers().get(1).getNickname());

        assertThrows(NicknameAlreadyUsedException.class, () -> gc3.addPlayer("Andrea"));
        assertEquals(2, gc3.getGameModel().getPlayers().size());

        assertDoesNotThrow(() -> gc3.addPlayer("Marco"));
        assertEquals(3, gc3.getGameModel().getPlayers().size());
        assertThrows(FullLobbyException.class, () -> gc3.addPlayer("Giulia"));
    }


    @Test
    void placeCard() {
        GameController gc6 = new GameController();
        gc6.createGame(2, "Player1");
        assertDoesNotThrow(() -> gc6.addPlayer("Player2"));
        Player currentPlayer2;
        Player currentPlayer1 = gc6.getGameModel().getPlayers().get(gc6.getGameModel().getCurrentPlayer());
        if(currentPlayer1.equals(gc6.getGameModel().getPlayers().get(0)))
            currentPlayer2 = gc6.getGameModel().getPlayers().get(1);
        else
            currentPlayer2 = gc6.getGameModel().getPlayers().get(0);
        currentPlayer1.setPlayerBoard(new PlayerBoard(currentPlayer1));
        currentPlayer1.getPlayerBoard().placeStartingCard(currentPlayer1.getStartingCard(), true);

        assertThrows(InvalidStateException.class, () -> gc6.placeCard(currentPlayer1.getNickname(), 1, 41,41,true));

        gc6.getGameModel().setGameState(State.PLAYING);

        assertThrows(PlayerNotInTurnException.class, () -> gc6.placeCard(currentPlayer2.getNickname(), 1, 41,41,true));
        assertThrows(IllegalArgumentException.class, () -> gc6.placeCard(currentPlayer1.getNickname(), 0, 41,41,true));
        assertDoesNotThrow(() -> gc6.placeCard(currentPlayer1.getNickname(), 1, 41,41,true));
    }
}