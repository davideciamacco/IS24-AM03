package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerBoardSub;
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

    /**
     * Checking the correct creation of a game
     * @throws RemoteException RMI Exception
     */
    @Test
    void createGame() throws RemoteException {
        GameController gc1 = new GameController();
        assertDoesNotThrow(() -> gc1.createGame(3, "HostPlayer", "TCP"));
        assertThrows(GameAlreadyCreatedException.class, ()->gc1.createGame(3,"Host", "TCP"));
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

    /**
     * Testing the correct selection of the starting card side
     * @throws RemoteException RMI Exception
     */
    @Test
    void selectStartingFace() throws RemoteException {
        GameController gc5 = new GameController();
        assertThrows(GameNotExistingException.class, ()->gc5.canSelectStartingFace("Player1", "FRONT"));
        assertDoesNotThrow(() -> gc5.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc5.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc5.addToObserver(gs1);
        gc5.addToObserver(gs2);
        gc5.addToObserver(ps1);
        gc5.addToObserver(ps2);
        gc5.canStart();
        gc5.getGameModel().setGameState(State.PLAYING);
        Player currentPlayer1 = gc5.getGameModel().getPlayers().get(gc5.getGameModel().getCurrentPlayer());
        currentPlayer1.setPlayerBoard(new PlayerBoard(currentPlayer1));
        assertThrows(InvalidStateException.class, ()->gc5.canSelectStartingFace(currentPlayer1.getNickname(), "FRONT"));
        gc5.getGameModel().setGameState(State.STARTING);
        assertThrows(UnknownPlayerException.class, ()->gc5.canSelectStartingFace("Player3", "FRONT"));
        assertThrows(ArgumentException.class, ()->gc5.canSelectStartingFace(currentPlayer1.getNickname(), "ciao"));
        assertDoesNotThrow(() -> gc5.canSelectStartingFace(currentPlayer1.getNickname(), "FRONT"));
        gc5.selectStartingFace(currentPlayer1.getNickname(), "FRONT");
        assertThrows(PlayerNotInTurnException.class, () -> gc5.canSelectStartingFace(currentPlayer1.getNickname(), "FRONT"));
        Player currentPlayer2 = gc5.getGameModel().getPlayers().get(gc5.getGameModel().getCurrentPlayer());
        currentPlayer1.setPlayerBoard(new PlayerBoard(currentPlayer1));
        assertDoesNotThrow(() -> gc5.canSelectStartingFace(currentPlayer2.getNickname(), "BACK"));
        gc5.selectStartingFace(currentPlayer2.getNickname(), "BACK");
        gc5.getGameModel().getPlayers().get(0).setConnected(false);
        gc5.getGameModel().getPlayers().get(1).setConnected(false);
        assertThrows(UnknownPlayerException.class, ()->gc5.canSelectStartingFace("Player1", "FRONT"));
    }

    /**
     * Testing the correct setting of the objective card for a certain player
     * @throws RemoteException RMI Exception
     */
    @Test
    void setObjectiveCard() throws RemoteException {
        GameController gc5 = new GameController();
        assertThrows(GameNotExistingException.class, ()->gc5.canSetObjectiveCard("Player1", 1));
        assertDoesNotThrow(() -> gc5.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc5.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc5.addToObserver(gs1);
        gc5.addToObserver(gs2);
        gc5.addToObserver(ps1);
        gc5.addToObserver(ps2);
        gc5.canStart();
        Player currentPlayer1 = gc5.getGameModel().getPlayers().get(gc5.getGameModel().getCurrentPlayer());
        assertThrows(InvalidStateException.class, ()->gc5.canSetObjectiveCard(currentPlayer1.getNickname(), 1));
        gc5.getGameModel().setGameState(State.OBJECTIVE);
        assertThrows(UnknownPlayerException.class, ()->gc5.canSetObjectiveCard("Player3", 1));

        assertDoesNotThrow(() -> gc5.canSetObjectiveCard(currentPlayer1.getNickname(), 1));
        gc5.setObjectiveCard(currentPlayer1.getNickname(), 1);
        assertThrows(PlayerNotInTurnException.class, () -> gc5.canSetObjectiveCard(currentPlayer1.getNickname(), 2));

        Player currentPlayer2 = gc5.getGameModel().getPlayers().get(gc5.getGameModel().getCurrentPlayer());
        assertThrows(IllegalArgumentException.class, () -> gc5.canSetObjectiveCard(currentPlayer2.getNickname(), 3));

    }

    /**
     * Testing the correct drawing from the resource deck
     * @throws RemoteException RMI Exception
     */
    @Test
    void drawResources() throws RemoteException {
        GameController gc4 = new GameController();
        assertThrows(GameNotExistingException.class, () -> gc4.canDrawResources(null));
        assertDoesNotThrow(() -> gc4.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc4.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc4.addToObserver(gs1);
        gc4.addToObserver(gs2);
        gc4.addToObserver(ps1);
        gc4.addToObserver(ps2);
        gc4.canStart();
        gc4.getGameModel().setGameState(State.DRAWING);
        assertThrows(UnknownPlayerException.class, () -> gc4.canDrawResources("Player3"));
        String currentPlayer1 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();

        assertDoesNotThrow(() -> gc4.canDrawResources(currentPlayer1));
        gc4.drawResources(currentPlayer1);
        assertEquals(4, gc4.getGameModel().getPlayers().get(0).getHand().size());

        assertThrows(PlayerNotInTurnException.class, () -> gc4.canDrawResources(currentPlayer1));
        String currentPlayer2 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();

        assertThrows(InvalidStateException.class, () -> gc4.canDrawResources(currentPlayer2));
        gc4.getGameModel().setGameState(State.DRAWING);
        gc4.getGameModel().getResourceDeck().setEmpty();
        assertThrows(EmptyDeckException.class, () -> gc4.canDrawResources(currentPlayer2));
    }

    /**
     * Testing the correct drawing from the gold deck
     * @throws RemoteException RMI Exception
     */
    @Test
    void drawGold() throws RemoteException {
        GameController gc4 = new GameController();
        assertThrows(GameNotExistingException.class, () -> gc4.canDrawGold(null));
        assertDoesNotThrow(() -> gc4.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc4.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc4.addToObserver(gs1);
        gc4.addToObserver(gs2);
        gc4.addToObserver(ps1);
        gc4.addToObserver(ps2);
        gc4.canStart();
        gc4.getGameModel().setGameState(State.DRAWING);
        assertThrows(UnknownPlayerException.class, () -> gc4.canDrawGold("Player3"));
        String currentPlayer1 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();
        assertDoesNotThrow(() -> gc4.canDrawGold(currentPlayer1));
        gc4.drawGold(currentPlayer1);
        assertEquals(4, gc4.getGameModel().getPlayers().get(0).getHand().size());
        assertThrows(PlayerNotInTurnException.class, () -> gc4.canDrawGold(currentPlayer1));
        String currentPlayer2 = gc4.getGameModel().getPlayers().get(gc4.getGameModel().getCurrentPlayer()).getNickname();
        assertThrows(InvalidStateException.class, () -> gc4.canDrawGold(currentPlayer2));
        gc4.getGameModel().setGameState(State.DRAWING);
        gc4.getGameModel().getGoldDeck().setEmpty();
        assertThrows(EmptyDeckException.class, () -> gc4.canDrawGold(currentPlayer2));
    }

    /**
     * Testing the correct drawing from the cards on the common table
     * @throws RemoteException RMI Exception
     */
    @Test
    void drawTable() throws RemoteException {
        GameController gc7 = new GameController();
        assertThrows(GameNotExistingException.class, () -> gc7.canDrawTable(null, 0));
        assertDoesNotThrow(() -> gc7.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc7.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc7.addToObserver(gs1);
        gc7.addToObserver(gs2);
        gc7.addToObserver(ps1);
        gc7.addToObserver(ps2);
        gc7.canStart();
        gc7.getGameModel().setGameState(State.DRAWING);
        assertThrows(UnknownPlayerException.class, () -> gc7.canDrawTable("Player3", 0));
        String currentPlayer1 = gc7.getGameModel().getPlayers().get(gc7.getGameModel().getCurrentPlayer()).getNickname();
        assertThrows(IllegalArgumentException.class, () -> gc7.canDrawTable(currentPlayer1, 0));
        assertDoesNotThrow(() -> gc7.canDrawTable(currentPlayer1, 1));
        gc7.drawTable(currentPlayer1, 1);
        assertEquals(4, gc7.getGameModel().getPlayers().get(0).getHand().size());
        assertThrows(PlayerNotInTurnException.class, () -> gc7.canDrawTable(currentPlayer1, 1));
        String currentPlayer2 = gc7.getGameModel().getPlayers().get(gc7.getGameModel().getCurrentPlayer()).getNickname();
        assertThrows(InvalidStateException.class, () -> gc7.canDrawTable(currentPlayer2, 1));
        gc7.getGameModel().setGameState(State.DRAWING);
        gc7.getGameModel().getTableCards().set(0, null);
        assertThrows(NullCardSelectedException.class, () -> gc7.canDrawTable(currentPlayer2, 1));
    }

    /**
     * Testing the correct adding of a player in the game
     * @throws RemoteException
     */
    @Test
    void addPlayer() throws RemoteException {
        GameController gc3 = new GameController();
        assertThrows(GameNotExistingException.class, () -> gc3.addPlayer("Andrea", "TCP"));
        assertDoesNotThrow(() -> gc3.createGame(3, "Giorgio", "TCP"));
        assertEquals(1, gc3.getGameModel().getPlayers().size());

        assertDoesNotThrow(() -> gc3.addPlayer("Andrea", "TCP"));
        assertEquals(2, gc3.getGameModel().getPlayers().size());
        assertEquals("Andrea", gc3.getGameModel().getPlayers().get(1).getNickname());

        assertThrows(NicknameAlreadyUsedException.class, () -> gc3.addPlayer("Andrea", "TCP"));
        assertEquals(2, gc3.getGameModel().getPlayers().size());

        assertDoesNotThrow(() -> gc3.addPlayer("Marco", "RMI"));
        assertEquals(3, gc3.getGameModel().getPlayers().size());
        assertThrows(FullLobbyException.class, () -> gc3.addPlayer("Giulia", "TCP"));
    }


    /**
     * Testing the correct placing of a card for a certain player
     * @throws RemoteException RMI Exception
     */
    @Test
    void placeCard() throws RemoteException {
        GameController gc6 = new GameController();
        assertThrows(GameNotExistingException.class, () -> gc6.placeCard(null, 1, 41, 41, "FRONT"));
        assertDoesNotThrow(() -> gc6.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc6.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc6.addToObserver(gs1);
        gc6.addToObserver(gs2);
        gc6.addToObserver(ps1);
        gc6.addToObserver(ps2);
        gc6.canStart();
        Player currentPlayer2;
        Player currentPlayer1 = gc6.getGameModel().getPlayers().get(gc6.getGameModel().getCurrentPlayer());
        if (currentPlayer1.equals(gc6.getGameModel().getPlayers().get(0)))
            currentPlayer2 = gc6.getGameModel().getPlayers().get(1);
        else
            currentPlayer2 = gc6.getGameModel().getPlayers().get(0);
        currentPlayer1.setPlayerBoard(new PlayerBoard(currentPlayer1));
        currentPlayer1.getPlayerBoard().placeStartingCard(currentPlayer1.getStartingCard(), true);
        currentPlayer2.setPlayerBoard(new PlayerBoard(currentPlayer2));

        assertThrows(InvalidStateException.class, () -> gc6.placeCard(currentPlayer1.getNickname(), 1, 41, 41, "FRONT"));

        gc6.getGameModel().setGameState(State.PLAYING);
        assertThrows(UnknownPlayerException.class, () -> gc6.placeCard("Piero", 1, 41, 41, "FRONT"));
        assertThrows(PlayerNotInTurnException.class, () -> gc6.placeCard(currentPlayer2.getNickname(), 1, 41, 41, "FRONT"));
        assertThrows(ArgumentException.class, () -> gc6.placeCard(currentPlayer1.getNickname(), 0, 41, 41, "FRONT"));
        assertThrows(ArgumentException.class, () -> gc6.placeCard(currentPlayer1.getNickname(), 1, 41, 41, "ciao"));
        assertDoesNotThrow(() -> gc6.placeCard(currentPlayer1.getNickname(), 1, 41, 41, "FRONT"));
        gc6.getGameModel().nextTurn();
        gc6.getGameModel().setGameState(State.PLAYING);
        assertThrows(Exception.class,() -> gc6.placeCard(currentPlayer2.getNickname(), 1, 41, 41, "BACK"));

        currentPlayer2.getPlayerBoard().placeStartingCard(currentPlayer1.getStartingCard(), true);
        assertDoesNotThrow(() -> gc6.placeCard(currentPlayer2.getNickname(), 1, 41, 41, "BACK"));
    }

    /**
     * Testing the correct picking color for a certain player
     * @throws RemoteException RMI Exception
     */
    @Test
    void pickColor() throws RemoteException {
        GameController gc = new GameController();
        assertThrows(GameNotExistingException.class, () -> gc.canPickColor(null, "RED"));
        assertDoesNotThrow(() -> gc.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc.addToObserver(gs1);
        gc.addToObserver(gs2);
        gc.addToObserver(ps1);
        gc.addToObserver(ps2);
        gc.canStart();

        assertThrows(InvalidStateException.class, ()->gc.canPickColor(gc.getGameModel().getPlayers().get(0).getNickname(), "RED"));
        gc.getGameModel().setGameState(State.COLOR);
        assertThrows(UnknownPlayerException.class, ()->gc.canPickColor(null, "RED"));
        assertThrows(PlayerNotInTurnException.class, ()->gc.canPickColor(gc.getGameModel().getPlayers().get(1).getNickname(), "RED"));
        assertThrows(IllegalArgumentException.class, ()->gc.canPickColor(gc.getGameModel().getPlayers().get(0).getNickname(), "rd"));
        assertDoesNotThrow(()->gc.canPickColor(gc.getGameModel().getPlayers().get(0).getNickname(), "RED"));
        gc.pickColor(gc.getGameModel().getPlayers().get(0).getNickname(), "RED");
        assertThrows(ColorAlreadyPickedException.class, ()->gc.canPickColor(gc.getGameModel().getPlayers().get(1).getNickname(), "RED"));

    }

    /**
     * Testing methods used to handle disconnection of a player during the game
     * @throws RemoteException RMI Exception
     * @throws InterruptedException
     */
    @Test
    void handleCrashedPlayer() throws RemoteException, InterruptedException{
        GameController gc = new GameController();
        assertThrows(GameNotExistingException.class, () -> gc.rejoinGame("Player1", "TCP"));
        assertDoesNotThrow(() -> gc.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        PlayerBoardSub pb11 = new GeneralSubscriber("Player1");
        PlayerBoardSub pb21 = new GeneralSubscriber("Player2");
        ChatSub cs11 = new GeneralSubscriber("Player1");
        ChatSub cs21 = new GeneralSubscriber("Player2");
        gc.addToObserver(pb11);
        gc.addToObserver(pb21);
        gc.addToObserver(cs11);
        gc.addToObserver(cs21);
        gc.addToObserver(gs1);
        gc.addToObserver(gs2);
        gc.addToObserver(ps1);
        gc.addToObserver(ps2);
        gc.canStart();
        gc.getGameModel().setGameState(State.PLAYING);
        gc.handleCrashedPlayer(gc.getGameModel().getPlayers().get(0).getNickname());
        assertTrue(gc.isTimer());
        assertEquals(1, gc.getGameModel().getNumPlayersConnected());
        assertFalse(gc.getGameModel().getPlayers().get(0).getConnected());
        gc.removeSub(pb11);
        gc.removeSub(pb21);
        gc.removeSub(cs11);
        gc.removeSub(cs21);
        gc.removeSub(gs1);
        gc.removeSub(gs2);
        gc.removeSub(ps1);
        gc.removeSub(ps2);
        assertThrows(UnknownPlayerException.class, ()->gc.rejoinGame("Player3", "TCP"));

        assertDoesNotThrow(()->gc.rejoinGame(gc.getGameModel().getPlayers().get(0).getNickname(), "TCP"));
        assertFalse(gc.isTimer());
        assertEquals(2, gc.getGameModel().getNumPlayersConnected());
        assertTrue(gc.getGameModel().getPlayers().get(0).getConnected());
        gc.rejoinedChief(gc.getGameModel().getPlayers().get(0).getNickname());
        gc.handleCrashedPlayer(gc.getGameModel().getPlayers().get(0).getNickname());
        Thread.sleep(61000);
        assertThrows(InvalidStateException.class, ()->gc.rejoinGame(gc.getGameModel().getPlayers().get(0).getNickname(), "TCP"));
        assertEquals(State.ENDING, gc.getGameModel().getGameState());


        GameController gc1 = new GameController();
        assertDoesNotThrow(() -> gc1.createGame(3, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc1.addPlayer("Player2", "TCP"));
        assertDoesNotThrow(() -> gc1.addPlayer("Player3", "TCP"));
        GameSub gs11 = new GeneralSubscriber("Player1");
        GameSub gs12 = new GeneralSubscriber("Player2");
        GameSub gs13 = new GeneralSubscriber("Player3");
        PlayerSub ps11 = new GeneralSubscriber("Player1");
        PlayerSub ps12 = new GeneralSubscriber("Player2");
        PlayerSub ps13 = new GeneralSubscriber("Player3");
        gc1.addToObserver(gs11);
        gc1.addToObserver(gs12);
        gc1.addToObserver(gs13);
        gc1.addToObserver(ps11);
        gc1.addToObserver(ps12);
        gc1.addToObserver(ps13);
        gc1.canStart();
        Player currentplayer = gc1.getGameModel().getPlayers().get(gc1.getGameModel().getCurrentPlayer());
        gc1.getGameModel().setGameState(State.PLAYING);
        gc1.handleCrashedPlayer(currentplayer.getNickname());
        assertFalse(gc1.isTimer());
        assertEquals(1, gc1.getGameModel().getCurrentPlayer());
        Player currentplayer1 = gc1.getGameModel().getPlayers().get(gc1.getGameModel().getCurrentPlayer());
        assertDoesNotThrow(()->gc1.rejoinGame(currentplayer.getNickname(), "TCP"));
        gc1.rejoinedChief(currentplayer.getNickname());
        gc1.getGameModel().setGameState(State.DRAWING);
        gc1.handleCrashedPlayer(currentplayer1.getNickname());
        assertFalse(gc1.isTimer());
        assertEquals(2, gc1.getGameModel().getCurrentPlayer());
        assertEquals(State.PLAYING,gc1.getGameModel().getGameState());


        GameController gc2 = new GameController();
        assertDoesNotThrow(() -> gc2.createGame(2, "APlayer1", "RMI"));
        assertDoesNotThrow(() -> gc2.addPlayer("Player2", "TCP"));
        GameSub gs21 = new GeneralSubscriber("APlayer1");
        GameSub gs22 = new GeneralSubscriber("Player2");
        PlayerSub ps21 = new GeneralSubscriber("APlayer1");
        PlayerSub ps22 = new GeneralSubscriber("Player2");
        gc2.addToObserver(gs21);
        gc2.addToObserver(gs22);
        gc2.addToObserver(ps21);
        gc2.addToObserver(ps22);
        gc2.canStart();
        gc2.getGameModel().setGameState(State.PLAYING);
        gc2.handleCrashedPlayer("APlayer1");
        assertTrue(gc2.isTimer());
        assertDoesNotThrow(()->gc2.rejoinGame("APlayer1", "RMI"));
        gc2.rejoinedChief("APlayer1");
        assertFalse(gc2.isTimer());
        assertEquals(2, gc2.getGameModel().getNumPlayersConnected());
        assertTrue(gc2.getGameModel().getPlayers().get(0).getConnected());
    }

    /**
     * Testing the correct sending of a private chat text
     * @throws RemoteException RMI Exception
     */
    @Test
    void canSendPrivateChat() throws RemoteException{
        GameController gc = new GameController();
        assertThrows(GameNotExistingException.class, ()->gc.canSendPrivateChat("Player1", "Player2", "Ciao"));

        assertDoesNotThrow(() -> gc.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc.addToObserver(gs1);
        gc.addToObserver(gs2);
        gc.addToObserver(ps1);
        gc.addToObserver(ps2);
        assertThrows(InvalidStateException.class, ()->gc.canSendPrivateChat("Player1", "Player2", "Ciao"));
        gc.canStart();
        assertThrows(UnknownPlayerException.class, ()->gc.canSendPrivateChat(null, "Player2", "Ciao"));
        assertThrows(PlayerAbsentException.class, ()->gc.canSendPrivateChat("Player1", "Player3", "Ciao"));
        assertThrows(BadTextException.class, ()->gc.canSendPrivateChat("Player1", "Player2", ""));
        assertThrows(ParametersException.class, ()->gc.canSendPrivateChat("Player1", "Player1", "Ciao"));
        assertDoesNotThrow(()->gc.canSendPrivateChat("Player1", "Player2", "Ciao"));
        gc.sendPrivateText("Player1", "Player2","Ciao");
    }

    /**
     * Testing the correct sending of a group chat text
     * @throws RemoteException RMI Exception
     */
    @Test
    void canSendGroupChat() throws RemoteException{
        GameController gc = new GameController();
        assertThrows(GameNotExistingException.class, ()->gc.canSendGroupChat("Player1", "Ciao"));
        assertDoesNotThrow(() -> gc.createGame(2, "Player1", "TCP"));
        assertDoesNotThrow(() -> gc.addPlayer("Player2", "TCP"));
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        gc.addToObserver(gs1);
        gc.addToObserver(gs2);
        gc.addToObserver(ps1);
        gc.addToObserver(ps2);
        assertThrows(InvalidStateException.class, ()->gc.canSendGroupChat("Player1", "Ciao"));
        gc.canStart();
        assertThrows(UnknownPlayerException.class, ()->gc.canSendGroupChat(null, "Ciao"));
        assertThrows(BadTextException.class, ()->gc.canSendGroupChat("Player1",  ""));
        assertDoesNotThrow(()->gc.canSendGroupChat("Player1", "Ciao"));
        gc.sendGroupText("Player1", "Ciao");
    }
}