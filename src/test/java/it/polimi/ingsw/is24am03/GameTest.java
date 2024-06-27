package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.Subscribers.*;
import it.polimi.ingsw.is24am03.server.model.cards.Corner;
import it.polimi.ingsw.is24am03.server.model.cards.GoldCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.chat.Chat;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.is24am03.server.model.exceptions.NullCardSelectedException;
import it.polimi.ingsw.is24am03.server.model.game.Game;
import it.polimi.ingsw.is24am03.server.model.player.Player;
import it.polimi.ingsw.is24am03.server.model.player.PlayerBoard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testConstructor()
    {
        boolean result;
        String host = "Giacomo";
        Game game = new Game(2, host);

        assertFalse(game.isEnding());
        assertFalse(game.isLastRound());
        assertEquals(game.getGameState(), State.WAITING);
        assertEquals(game.getNumPlayers(), 2);
        assertNotNull(game.getGoldDeck());
        assertNotNull(game.getCommonObjective());
        assertNotNull(game.getResourceDeck());
        assertNotNull(game.getTableCards());
        assertNotNull(game.getObjectiveDeck());
        assertNotNull(game.getStartingDeck());
        assertEquals(1, game.getPlayers().size());
        assertEquals("Giacomo", game.getPlayers().get(0).getNickname());
        assertEquals(1, game.getNumPlayersConnected());
        assertFalse(game.getGoldDeck().isEmpty());
        assertFalse(game.getResourceDeck().isEmpty());
        assertFalse(game.getObjectiveDeck().isEmpty());
        assertFalse(game.getStartingDeck().isEmpty());
    }


    @Test
    void checkWinner() {
        boolean result;

        ArrayList<Player> w1;
        ArrayList<Player> cw1 = new ArrayList<>();
        Game game1 = new Game(2, "Giorgio");
        game1.addPlayer("Marco");
        game1.getPlayers().get(0).setPoints(21);
        game1.getPlayers().get(1).setPoints(21);
        game1.getPlayers().get(0).increaseNumObjective(10);
        game1.getPlayers().get(1).increaseNumObjective(11);
        cw1.add(game1.getPlayers().get(1));
        w1=game1.checkWinner();
        assertEquals(w1, cw1);

        ArrayList<Player> w2;
        //ArrayList<Player> cw2 = new ArrayList<>();
        Game game2 = new Game(2, "Giorgio");
        game2.addPlayer("Marco");
        game2.getPlayers().get(0).setPoints(21);
        game2.getPlayers().get(1).setPoints(21);
        game2.getPlayers().get(0).increaseNumObjective(11);
        game2.getPlayers().get(1).increaseNumObjective(11);
        //cw2.add(p3);
        //cw2.add(p4);
        w2=game2.checkWinner();
        result = w2.contains(game2.getPlayers().get(0));
        assertTrue(result);
        result = w2.contains(game2.getPlayers().get(1));
        assertTrue(result);

        ArrayList<Player> w3;
        //ArrayList<Player> cw2 = new ArrayList<>();
        Game game3 = new Game(2, "Giorgio");
        game3.addPlayer("Marco");
        game3.getPlayers().get(0).setPoints(21);
        game3.getPlayers().get(1).setPoints(21);
        game3.getPlayers().get(0).increaseNumObjective(10);
        game3.getPlayers().get(1).increaseNumObjective(11);
        //cw2.add(p3);
        //cw2.add(p4);
        w3=game3.checkWinner();
        result = w3.contains(game3.getPlayers().get(0));
        assertFalse(result);
        result = w3.contains(game3.getPlayers().get(1));
        assertTrue(result);
    }

    @Test
    void nextTurn() {

        Game game = new Game(2, "Player1");
        game.addPlayer("Player2");
        GameSub gs11 = new GeneralSubscriber("Player1");
        GameSub gs21 = new GeneralSubscriber("Player2");
        PlayerSub ps11 = new GeneralSubscriber("Player1");
        PlayerSub ps21 = new GeneralSubscriber("Player2");
        PlayerBoardSub pb11 = new GeneralSubscriber("Player1");
        PlayerBoardSub pb21 = new GeneralSubscriber("Player2");
        ChatSub cs11 = new GeneralSubscriber("Player1");
        ChatSub cs21 = new GeneralSubscriber("Player2");
        game.addSub(pb11);
        game.addSub(pb21);
        game.addSub(cs11);
        game.addSub(cs21);
        game.addSub(gs11);
        game.addSub(gs21);
        game.addSub(ps11);
        game.addSub(ps21);
        game.startGame();
        game.setGameState(State.PLAYING);

        assertEquals(0, game.getCurrentPlayer());

        game.nextTurn();

        assertEquals(1, game.getCurrentPlayer());

        game.nextTurn();

        assertEquals(0, game.getCurrentPlayer());

        game.setEnding();
        game.nextTurn();

        assertTrue(game.isEnding());
        assertFalse(game.isLastRound());

        game.nextTurn();
        assertTrue(game.isEnding());
        assertTrue(game.isLastRound());


        Game game1 = new Game(2, "Player1");
        game1.addPlayer("Player2");
        GameSub gs12 = new GeneralSubscriber("Player1");
        GameSub gs22 = new GeneralSubscriber("Player2");
        PlayerSub ps12 = new GeneralSubscriber("Player1");
        PlayerSub ps22 = new GeneralSubscriber("Player2");
        game1.addSub(gs12);
        game1.addSub(gs22);
        game1.addSub(ps12);
        game1.addSub(ps22);
        game1.startGame();

        game1.nextTurn();
        assertEquals(State.STARTING, game1.getGameState());

        game1.nextTurn();
        assertEquals(State.COLOR, game1.getGameState());

        game1.nextTurn();
        assertEquals(State.COLOR, game1.getGameState());

        game1.nextTurn();
        assertEquals(State.OBJECTIVE, game1.getGameState());

        game1.nextTurn();
        assertEquals(State.OBJECTIVE, game1.getGameState());

        game1.nextTurn();
        assertEquals(State.PLAYING, game1.getGameState());
    }

    @Test
    void startGame() {
        Game game4 = new Game(2, "Giorgio");

        //assertThrows(NotAllPlayersHaveJoinedException.class, ()->{game4.startGame();});

        Player p2 = new Player("Marco");
        game4.getPlayers().add(p2);
        assertEquals(2, game4.getPlayers().size());
        GameSub gs41 = new GeneralSubscriber("Giorgio");
        GameSub gs42 = new GeneralSubscriber("Marco");
        PlayerSub ps41 = new GeneralSubscriber("Giorgio");
        PlayerSub ps42 = new GeneralSubscriber("Marco");
        ChatSub cs41=new GeneralSubscriber("Giorgio");
        ChatSub cs42=new GeneralSubscriber("Marco");
        game4.addSub(cs41);
        game4.addSub(cs42);
        game4.addSub(gs41);
        game4.addSub(gs42);
        game4.addSub(ps41);
        game4.addSub(ps42);
        assertDoesNotThrow(()->{game4.startGame();});

        assertEquals(game4.getGameState(), State.STARTING);
        for(Player player: game4.getPlayers()) {
            assertEquals(3, player.getHand().size());
            //assertNotNull(player.getObjectiveCard());
        }
        assertEquals(4, game4.getTableCards().size());
        assertEquals(34, game4.getResourceDeck().getCards().size());
        assertEquals(0, game4.getCurrentPlayer());
        assertEquals(2, game4.getCommonObjective().size());
        assertEquals(36, game4.getGoldDeck().getCards().size());
        assertEquals(10, game4.getObjectiveDeck().getCards().size());
        assertEquals(4, game4.getStartingDeck().getCards().size());
    }


    @Test
    void endGame() {
        Game game = new Game(2, "Player1");
        game.addPlayer("Player2");
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        game.addSub(gs1);
        game.addSub(gs2);
        game.addSub(ps1);
        game.addSub(ps2);
        game.startGame();
        game.getPlayers().get(0).setPlayerBoard(new PlayerBoard(game.getPlayers().get(0)));
        game.getPlayers().get(1).setPlayerBoard(new PlayerBoard(game.getPlayers().get(1)));
        game.endGame();

        assertEquals(State.ENDING, game.getGameState());


        Game game1 = new Game(2, "Player1");
        game1.addPlayer("Player2");
        GameSub gs11 = new GeneralSubscriber("Player1");
        GameSub gs12 = new GeneralSubscriber("Player2");
        PlayerSub ps11 = new GeneralSubscriber("Player1");
        PlayerSub ps12 = new GeneralSubscriber("Player2");
        game1.addSub(gs11);
        game1.addSub(gs12);
        game1.addSub(ps11);
        game1.addSub(ps12);
        game1.startGame();
        game1.setNumPlayersConnected(2);
        game1.getPlayers().get(0).setPoints(10);
        game1.getPlayers().get(1).setPoints(2);
        game1.endGame();
        assertTrue(game1.getPlayers().get(0).isWinner());
        assertEquals(State.ENDING, game.getGameState());

    }


    @Test
    void giveObjectivePoints() {
        Game game = new Game(2, "Player1");
        game.addPlayer("Player2");
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        game.addSub(gs1);
        game.addSub(gs2);
        game.addSub(ps1);
        game.addSub(ps2);
        game.startGame();

        game.getPlayers().get(0).setPlayerBoard(new PlayerBoard(game.getPlayers().get(0)));
        game.getPlayers().get(1).setPlayerBoard(new PlayerBoard(game.getPlayers().get(1)));

        game.giveObjectivePoints();

        assertEquals(0, game.getPlayers().get(0).getPoints());
        assertEquals(0, game.getPlayers().get(1).getPoints());
        assertEquals(0, game.getPlayers().get(0).getNumObj());
        assertEquals(0, game.getPlayers().get(1).getNumObj());
    }

    @Test
    void drawResources() {
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");

        Game game1 = new Game(2, "Giorgio");
        game1.addPlayer("Marco");
        GameSub gs1 = new GeneralSubscriber("Giorgio");
        GameSub gs2 = new GeneralSubscriber("Marco");
        PlayerSub ps1 = new GeneralSubscriber("Giorgio");
        PlayerSub ps2 = new GeneralSubscriber("Marco");
        game1.addSub(gs1);
        game1.addSub(gs2);
        game1.addSub(ps1);
        game1.addSub(ps2);

        game1.startGame();
        game1.setGameState(State.DRAWING);
        assertDoesNotThrow(()->game1.drawResources(game1.getPlayers().get(0).getNickname()));
        assertEquals(4, game1.getPlayers().get(0).getHand().size());

        game1.getGoldDeck().setEmpty();
        game1.getResourceDeck().setEmpty();
        game1.getResourceDeck().getCards().add(new ResourceCard(0,"R",0,fungi, empty, notVisible, fungi,empty,empty,empty,empty));

        assertDoesNotThrow(() -> game1.drawResources(game1.getPlayers().get(1).getNickname()));
        assertEquals(true, game1.isEnding());
        //assertThrows(EmptyDeckException.class, () -> game1.drawResources(game1.getPlayers().get(0).getNickname()));

    }

    @Test
    void drawGold() {
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");

        ArrayList<CornerItem> list = new ArrayList<>();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.ANIMAL);

        Game game5 = new Game(2, "Giorgio");
        game5.addPlayer("Marco");
        GameSub gs51 = new GeneralSubscriber("Giorgio");
        GameSub gs52 = new GeneralSubscriber("Marco");
        PlayerSub ps51 = new GeneralSubscriber("Giorgio");
        PlayerSub ps52 = new GeneralSubscriber("Marco");
        game5.addSub(gs51);
        game5.addSub(gs52);
        game5.addSub(ps51);
        game5.addSub(ps52);
        game5.startGame();
        game5.setGameState(State.DRAWING);
        assertDoesNotThrow(()->game5.drawGold(game5.getPlayers().get(0).getNickname()));
        assertEquals(4, game5.getPlayers().get(0).getHand().size());

        game5.getGoldDeck().setEmpty();
        game5.getResourceDeck().setEmpty();
        game5.getGoldDeck().getCards().add(new GoldCard(40,"R",1,notVisible,empty,quill,empty,empty,empty,empty,empty,list,0,CornerItem.QUILL));

        assertDoesNotThrow(() -> game5.drawGold(game5.getPlayers().get(1).getNickname()));
        assertEquals(true, game5.isEnding());
        //assertThrows(EmptyDeckException.class, () -> game5.drawGold(game5.getPlayers().get(1).getNickname()));
    }

    @Test
    void drawTable() {
        Game game6 = new Game(2, "Giorgio");
        game6.addPlayer("Marco");
        GameSub gs61 = new GeneralSubscriber("Giorgio");
        GameSub gs62 = new GeneralSubscriber("Marco");
        PlayerSub ps61 = new GeneralSubscriber("Giorgio");
        PlayerSub ps62 = new GeneralSubscriber("Marco");
        game6.addSub(gs61);
        game6.addSub(gs62);
        game6.addSub(ps61);
        game6.addSub(ps62);
        game6.startGame();
        assertDoesNotThrow(() -> game6.drawTable(game6.getPlayers().get(0).getNickname(), 1));
        assertEquals(4, game6.getTableCards().size());

        game6.getResourceDeck().setEmpty();
        assertDoesNotThrow(() -> game6.drawTable(game6.getPlayers().get(1).getNickname(), 1));
        assertNotNull(game6.getTableCards().get(0));

        game6.getGoldDeck().setEmpty();
        assertDoesNotThrow(() -> game6.drawTable(game6.getPlayers().get(1).getNickname(), 1));
        assertNull(game6.getTableCards().get(0));

        //game6.getTableCards().set(0, null);
        //assertThrows(NullCardSelectedException.class, () -> game6.drawTable(game6.getPlayers().get(0).getNickname(), 1));

        Game game7 = new Game(2, "Giorgio");
        game7.addPlayer("Marco");
        GameSub gs71 = new GeneralSubscriber("Giorgio");
        GameSub gs72 = new GeneralSubscriber("Marco");
        PlayerSub ps71 = new GeneralSubscriber("Giorgio");
        PlayerSub ps72 = new GeneralSubscriber("Marco");
        game7.addSub(gs71);
        game7.addSub(gs72);
        game7.addSub(ps71);
        game7.addSub(ps72);
        game7.startGame();
        assertDoesNotThrow(() -> game7.drawTable(game7.getPlayers().get(0).getNickname(), 2));
        assertEquals(4, game7.getTableCards().size());

        game7.getResourceDeck().setEmpty();
        assertDoesNotThrow(() -> game7.drawTable(game7.getPlayers().get(1).getNickname(), 2));
        assertNotNull(game7.getTableCards().get(1));

        game7.getGoldDeck().setEmpty();
        assertDoesNotThrow(() -> game7.drawTable(game7.getPlayers().get(1).getNickname(), 2));
        assertNull(game7.getTableCards().get(1));

        Game game8 = new Game(2, "Giorgio");
        game8.addPlayer("Marco");
        GameSub gs81 = new GeneralSubscriber("Giorgio");
        GameSub gs82 = new GeneralSubscriber("Marco");
        PlayerSub ps81 = new GeneralSubscriber("Giorgio");
        PlayerSub ps82 = new GeneralSubscriber("Marco");
        game8.addSub(gs81);
        game8.addSub(gs82);
        game8.addSub(ps81);
        game8.addSub(ps82);
        game8.startGame();
        assertDoesNotThrow(() -> game8.drawTable(game8.getPlayers().get(0).getNickname(), 3));
        assertEquals(4, game8.getTableCards().size());

        game8.getGoldDeck().setEmpty();
        assertDoesNotThrow(() -> game8.drawTable(game8.getPlayers().get(1).getNickname(), 3));
        assertNotNull(game8.getTableCards().get(2));

        game8.getResourceDeck().setEmpty();
        assertDoesNotThrow(() -> game8.drawTable(game8.getPlayers().get(1).getNickname(), 3));
        assertNull(game8.getTableCards().get(2));

        Game game9 = new Game(2, "Giorgio");
        game9.addPlayer("Marco");
        GameSub gs91 = new GeneralSubscriber("Giorgio");
        GameSub gs92 = new GeneralSubscriber("Marco");
        PlayerSub ps91 = new GeneralSubscriber("Giorgio");
        PlayerSub ps92 = new GeneralSubscriber("Marco");
        game9.addSub(gs91);
        game9.addSub(gs92);
        game9.addSub(ps91);
        game9.addSub(ps92);
        game9.startGame();
        assertDoesNotThrow(() -> game9.drawTable(game9.getPlayers().get(0).getNickname(), 4));
        assertEquals(4, game9.getTableCards().size());

        game9.getGoldDeck().setEmpty();
        assertDoesNotThrow(() -> game9.drawTable(game9.getPlayers().get(1).getNickname(), 4));
        assertNotNull(game9.getTableCards().get(3));

        game9.getResourceDeck().setEmpty();
        assertDoesNotThrow(() -> game9.drawTable(game9.getPlayers().get(1).getNickname(), 4));
        assertNull(game9.getTableCards().get(3));
    }


    @Test
    void placeCard() {
        Game game = new Game(2, "Player1");
        game.addPlayer("Player2");
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        game.addSub(gs1);
        game.addSub(gs2);
        game.addSub(ps1);
        game.addSub(ps2);
        game.startGame();
        game.getPlayers().get(0).setPlayerBoard(new PlayerBoard(game.getPlayers().get(0)));
        game.getPlayers().get(1).setPlayerBoard(new PlayerBoard(game.getPlayers().get(1)));
        Player currentPlayer = game.getPlayers().get(game.getCurrentPlayer());
        //currentPlayer.getPlayerBoard().getBoard()[40][40] = currentPlayer.getStartingCard();
        game.selectStartingFace(currentPlayer.getNickname(), true);
        assertEquals(currentPlayer.getStartingCard(), currentPlayer.getPlayerBoard().getBoard()[40][40]);
        game.nextTurn();
        assertThrows(Exception.class, ()->game.placeCard(currentPlayer.getNickname(), 0, 10, 10, true));
        assertDoesNotThrow(()->game.placeCard(currentPlayer.getNickname(), 0, 41, 41, true));


        Game game1 = new Game(2, "Player1");
        game1.addPlayer("Player2");
        GameSub gs11 = new GeneralSubscriber("Player1");
        GameSub gs12 = new GeneralSubscriber("Player2");
        PlayerSub ps11 = new GeneralSubscriber("Player1");
        PlayerSub ps12 = new GeneralSubscriber("Player2");
        game1.addSub(gs11);
        game1.addSub(gs12);
        game1.addSub(ps11);
        game1.addSub(ps12);
        game1.startGame();
        game1.getPlayers().get(0).setPlayerBoard(new PlayerBoard(game1.getPlayers().get(0)));
        game1.getPlayers().get(1).setPlayerBoard(new PlayerBoard(game1.getPlayers().get(1)));
        Player currentPlayer1 = game.getPlayers().get(game1.getCurrentPlayer());
        //game1.selectStartingFace(currentPlayer1.getNickname(), false);
        game1.selectStartingFace(currentPlayer1.getNickname(), true);
        game1.setGameState(State.PLAYING);
        game1.setLastRound();
        assertDoesNotThrow(()->game1.placeCard(currentPlayer1.getNickname(), 0, 41, 41, true));
        assertEquals(State.PLAYING, game1.getGameState());
        assertDoesNotThrow(()->game1.placeCard(currentPlayer1.getNickname(), 0, 41, 41, true));
        assertEquals(State.ENDING, game1.getGameState());
    }


    @Test
    void selectStartingFace() {
        Game game = new Game(2, "Player1");
        game.addPlayer("Player2");
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        game.addSub(gs1);
        game.addSub(gs2);
        game.addSub(ps1);
        game.addSub(ps2);
        game.startGame();
        game.getPlayers().get(0).setPlayerBoard(new PlayerBoard(game.getPlayers().get(0)));
        game.getPlayers().get(1).setPlayerBoard(new PlayerBoard(game.getPlayers().get(1)));

        Player currentPlayer = game.getPlayers().get(game.getCurrentPlayer());
        game.selectStartingFace(currentPlayer.getNickname(), true);

        assertEquals(currentPlayer.getStartingCard(), currentPlayer.getPlayerBoard().getBoard()[40][40]);
        assertEquals(currentPlayer.getStartingCard().getFace(), true);

        assertNotEquals(currentPlayer, game.getPlayers().get(game.getCurrentPlayer()));
    }

    @Test
    void setColor(){
        Game game = new Game(2, "Player1");
        game.addPlayer("Player2");
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        game.addSub(gs1);
        game.addSub(gs2);
        game.addSub(ps1);
        game.addSub(ps2);
        game.startGame();
        game.setGameState(State.COLOR);
        assertDoesNotThrow(()->game.setColor(Color.RED));
        assertFalse(game.getAvailableColors().contains(Color.RED));
    }

    @Test
    void setObjectiveCard() {
        Game game = new Game(2, "Player1");
        game.addPlayer("Player2");
        GameSub gs1 = new GeneralSubscriber("Player1");
        GameSub gs2 = new GeneralSubscriber("Player2");
        PlayerSub ps1 = new GeneralSubscriber("Player1");
        PlayerSub ps2 = new GeneralSubscriber("Player2");
        game.addSub(gs1);
        game.addSub(gs2);
        game.addSub(ps1);
        game.addSub(ps2);
        game.startGame();
        Player currentPlayer = game.getPlayers().get(game.getCurrentPlayer());
        game.setObjectiveCard(currentPlayer.getNickname(), 1);

        assertNotNull(currentPlayer.getObjective1());
        assertNotNull(currentPlayer.getObjective2());
        assertEquals(currentPlayer.getObjective1(), currentPlayer.getObjectiveCard());

        currentPlayer = game.getPlayers().get(game.getCurrentPlayer());
        game.setObjectiveCard(currentPlayer.getNickname(), 2);

        assertNotNull(currentPlayer.getObjective1());
        assertNotNull(currentPlayer.getObjective2());
        assertEquals(currentPlayer.getObjective2(), currentPlayer.getObjectiveCard());
    }

    @Test
    void extractNicknames(){
        Game game = new Game(2, "Player1");
        game.addPlayer("Player2");
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Player1");
        nicknames.add("Player2");
        assertEquals(nicknames, game.extractNicknames());
    }
}