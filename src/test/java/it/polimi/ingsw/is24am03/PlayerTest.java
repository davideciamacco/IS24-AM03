package it.polimi.ingsw.is24am03;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private ObjectiveCard objectiveCard;
    private StartingCard startingCard;
    private PlayableCard playableCard;

    @BeforeEach
    public void setUp() {
        player = new Player("TestPlayer", Color.RED, false);
        objectiveCard = new ObjectiveCard();
        startingCard = new StartingCard();
        playableCard = new PlayableCard();
    }

    @Test
    public void testConstructor() {
        assertEquals("TestPlayer", player.getNickname());
        assertFalse(player.isFirstPlayer());
        assertFalse(player.isWinner());
        assertEquals(0, player.getPoints());
        assertEquals(0, player.getNumObj());
        assertNull(player.getPlayerBoard());
        assertNull(player.getObjective());
        assertNull(player.getStartingCard());
        assertNull(player.getHand());
    }

    @Test
    public void testSetObjective() {
        player.setObjective(objectiveCard);
        assertEquals(objectiveCard, player.getObjective());
    }

    @Test
    public void testSetStartingCard() {
        player.setStartingCard(startingCard);
        assertEquals(startingCard, player.getStartingCard());
    }

    @Test
    public void testSetWinner() {
        player.setWinner(true);
        assertTrue(player.isWinner());
    }

    @Test
    public void testSetPlayerBoard() {
        PlayerBoard playerBoard = new PlayerBoard(player);
        player.setPlayerBoard(playerBoard);
        assertEquals(playerBoard, player.getPlayerBoard());
    }

    @Test
    public void testAddCard() {
        player.addCard(playableCard);
        assertTrue(player.getHand().contains(playableCard));
    }

    @Test
    public void testAddPoints() {
        player.addPoints(playableCard);
        assertEquals(3, player.getPoints());
    }

    @Test
    public void testRemoveCard() {
        player.addCard(playableCard);
        player.removeCard(playableCard);
        assertFalse(player.getHand().contains(playableCard));
    }

    @Test
    public void testSetFirstPlayer() {
        player.setFirstPlayer(false);
        assertFalse(player.isFirstPlayer());
    }

    @Test
    public void testGetNumObj() {
        assertEquals(0, player.getNumObj());
    }
}
