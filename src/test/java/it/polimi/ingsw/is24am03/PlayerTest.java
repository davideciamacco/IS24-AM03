package it.polimi.ingsw.is24am03;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", Color.RED);
    }

    @Test
    void testConstructor() {
        assertEquals("TestPlayer", player.getNickname());
        assertEquals(0, player.getPoints());
        assertEquals(0, player.getNumObj());
        assertNull(player.getObjectiveCard());
        assertNull(player.getPlayerBoard());
    }

    @Test
    void testAddCard() {
        PlayableCard card = new ResourceCard(0,"R",0,null,null,null,null,null,null,null,null);
        player.addCard(card);
        ArrayList<PlayableCard> hand = player.getHand();
        assertTrue(hand.contains(card));
    }
    @Test
    void testRemoveCard() {
        PlayableCard card = new ResourceCard(0,"R",0,null,null,null,null,null,null,null,null);
        player.addCard(card);
        assertTrue(player.getHand().contains(card));

        player.removeCard(card);
        assertFalse(player.getHand().contains(card));
    }
    @Test
    void testAddPoints() {
        PlayableCard card = new ResourceCard(0,"R",5,null,null,null,null,null,null,null,null); // Carta che aggiunge 5 punti
        player.addPoints(card);
        assertEquals(5, player.getPoints());
    }
    @Test
    void testSetStartingCard() {
        ArrayList<CornerItem> kingdomList=new ArrayList<CornerItem>();
        StartingCard startingCard = new StartingCard(80,0,null,null,null,null,null,null,null,null, kingdomList);
        player.setStartingCard(startingCard);
        assertEquals(startingCard, player.getStartingCard());
    }
    @Test
    void testSetPlayerBoard() {
        assertNull(player.getPlayerBoard());
        PlayerBoard playerBoard = new PlayerBoard(player);
        player.setPlayerBoard(playerBoard);
        assertEquals(playerBoard, player.getPlayerBoard());
    }
    @Test
    void testIncreaseNumObjective() {
        assertEquals(0, player.getNumObj());
        int n = 5;
        player.increaseNumObjective(n);
        assertEquals(n, player.getNumObj());
    }


}
