package it.polimi.ingsw.is24am03;

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

        game1.nextTurn();
        assertEquals(State.PREPARATION_1, game1.getGameState());

        game1.nextTurn();
        assertEquals(State.PREPARATION_2, game1.getGameState());

        game1.nextTurn();
        assertEquals(State.PREPARATION_2, game1.getGameState());

        game1.nextTurn();
        assertEquals(State.PLAYING, game1.getGameState());
    }

    @Test
    void startGame() {
        Game game4 = new Game(2, "Giorgio");

        assertThrows(NotAllPlayersHaveJoinedException.class, ()->{game4.startGame();});

        Player p2 = new Player("Marco", Color.BLUE);
        game4.getPlayers().add(p2);
        assertEquals(2, game4.getPlayers().size());
        assertDoesNotThrow(()->{game4.startGame();});

        assertEquals(game4.getGameState(), State.PREPARATION_1);
        for(Player player: game4.getPlayers()) {
            assertEquals(3, player.getHand().size());
            //assertNotNull(player.getObjectiveCard());
        }
        assertEquals(4, game4.getTableCards().size());
        assertEquals(34, game4.getResourceDeck().getCards().size());
        assertEquals(0, game4.getCurrentPlayer());
        assertEquals(2, game4.getCommonObjective().size());
        assertEquals(36, game4.getGoldDeck().getCards().size());
        assertEquals(14, game4.getObjectiveDeck().getCards().size());
        assertEquals(4, game4.getStartingDeck().getCards().size());
    }

    /*
    @Test
    void endGame() {
        Game game = new Game(2, "Player1");
        game.addPlayer("Player2");
        game.endGame();

        game.getPlayers().get(0).setPlayerBoard(new PlayerBoard(game.getPlayers().get(0)));
        game.getPlayers().get(1).setPlayerBoard(new PlayerBoard(game.getPlayers().get(1)));

        assertEquals(State.ENDING, game.getGameState());
    }*/

    @Test
    void giveObjectivePoints() {
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

        game1.setGameState(State.DRAWING);
        assertDoesNotThrow(()->game1.drawResources(game1.getPlayers().get(0).getNickname()));
        assertEquals(4, game1.getPlayers().get(0).getHand().size());

        game1.getGoldDeck().setEmpty();
        game1.getResourceDeck().setEmpty();
        game1.getResourceDeck().getCards().add(new ResourceCard(0,"R",0,fungi, empty, notVisible, fungi,empty,empty,empty,empty));

        assertDoesNotThrow(() -> game1.drawResources(game1.getPlayers().get(1).getNickname()));
        assertEquals(true, game1.isEnding());
        assertThrows(EmptyDeckException.class, () -> game1.drawResources(game1.getPlayers().get(1).getNickname()));



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

        game5.setGameState(State.DRAWING);
        assertDoesNotThrow(()->game5.drawResources(game5.getPlayers().get(0).getNickname()));
        assertEquals(4, game5.getPlayers().get(0).getHand().size());

        game5.getGoldDeck().setEmpty();
        game5.getResourceDeck().setEmpty();
        game5.getGoldDeck().getCards().add(new GoldCard(40,"R",1,notVisible,empty,quill,empty,empty,empty,empty,empty,list,0,CornerItem.QUILL));

        //assertDoesNotThrow(() -> game5.drawGold(game5.getPlayers().get(1).getNickname()));
        /*assertEquals(true, game1.isEnding());
        assertThrows(EmptyDeckException.class, () -> game1.drawGold(game1.getPlayers().get(1).getNickname()));*/
    }

    @Test
    void drawTable() {
        Game game6 = new Game(2, "Giorgio");
        game6.addPlayer("Marco");

        assertDoesNotThrow(() -> game6.drawTable(game6.getPlayers().get(0).getNickname(), 0));
        assertEquals(4, game6.getTableCards().size());

        game6.getResourceDeck().setEmpty();
        assertDoesNotThrow(() -> game6.drawTable(game6.getPlayers().get(1).getNickname(), 0));
        assertNotNull(game6.getTableCards().get(0));

        game6.getGoldDeck().setEmpty();
        assertDoesNotThrow(() -> game6.drawTable(game6.getPlayers().get(1).getNickname(), 0));
        assertNull(game6.getTableCards().get(0));

        game6.getTableCards().set(0, null);
        assertThrows(NullCardSelectedException.class, () -> game6.drawTable(game6.getPlayers().get(0).getNickname(), 0));

    }

    @Test
    void placeCard() {
    }
}