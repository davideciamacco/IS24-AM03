
package it.polimi.ingsw.is24am03;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.is24am03.server.model.cards.*;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.ObjectiveType;
import it.polimi.ingsw.is24am03.server.model.player.Player;
import it.polimi.ingsw.is24am03.server.model.player.PlayerBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Test class used to checks the method in Player class
 */
class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer");
    }

    /**
     * Testing that setting a player as the first in the turn order workd correctly
     */
    @Test
    void testSetFirst(){
        player.setFirstPlayer(true);
        assertEquals(true,player.isFirstPlayer());
    }

    /**
     * Testing set winner method
     */
    @Test
    void testSetWinner(){
        player.setWinner(true);
        assertEquals(true,player.isWinner());
    }

    /**
     * Testing if the objective cards are assigned correctly to a player
     */
    @Test
    void testSetObjective(){
        ObjectiveCard card_94 = new ObjectiveList(94,2, CornerItem.FUNGI, ObjectiveType.ITEM,CornerItem.EMPTY,3);
        ObjectiveCard card_95 = new ObjectiveList(94,2,CornerItem.FUNGI,ObjectiveType.ITEM,CornerItem.EMPTY,3);
        player.setObjectiveCard(card_94,card_95);
        assertEquals(card_94,player.getObjective1());
        assertEquals(card_95,player.getObjective2());
    }
    @Test
    void testConstructor() {
        assertEquals("TestPlayer", player.getNickname());
        assertEquals(0, player.getPoints());
        assertEquals(0, player.getNumObj());
    }

    /**
     * Testing add card to a player's hand method
     */
    @Test
    void testAddCard() {
        ResourceCard card = new ResourceCard(0,"R",0,null,null,null,null,null,null,null,null);
        player.addCard(card);
        ArrayList<ResourceCard> hand = player.getHand();
        assertTrue(hand.contains(card));
    }

    /**
     * Testing if removing a card from a player's hand works correctly
     */
    @Test
    void testRemoveCard() {
        ResourceCard card = new ResourceCard(0,"R",0,null,null,null,null,null,null,null,null);
        player.addCard(card);
        assertTrue(player.getHand().contains(card));

        player.removeCard(card);
        assertFalse(player.getHand().contains(card));
    }

    /**
     * Testing if a player's score is updated correctly
     */
    @Test
    void testAddPoints() {
        PlayableCard card = new ResourceCard(0,"R",5,null,null,null,null,null,null,null,null); // Carta che aggiunge 5 punti
        player.addPoints(card.getPoints());
        assertEquals(5, player.getPoints());
    }

    /**
     * Testing if a starting card is assigned correctly
     */
    @Test
    void testSetStartingCard() {
        ArrayList<CornerItem> kingdomList=new ArrayList<CornerItem>();
        StartingCard startingCard = new StartingCard(80,0,null,null,null,null,null,null,null,null, kingdomList);
        player.setStartingCard(startingCard);
        assertEquals(startingCard, player.getStartingCard());
    }

    /**
     * Testing if a board is assigned correctly to a player
     */
    @Test
    void testSetPlayerBoard() {
        PlayerBoard playerBoard = new PlayerBoard(player);
        player.setPlayerBoard(playerBoard);
        assertEquals(playerBoard, player.getPlayerBoard());
    }

    /**
     * Testing increasing number of completed objectives by a player
     */
    @Test
    void testIncreaseNumObjective() {
        assertEquals(0, player.getNumObj());
        int n = 5;
        player.increaseNumObjective(n);
        assertEquals(n, player.getNumObj());
    }


}