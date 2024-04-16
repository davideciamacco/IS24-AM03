package it.polimi.ingsw.is24am03;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
class PlayerBoardTest {
    private PlayerBoard playerBoard;
    private Player player;
    private static final int MAX_ROWS = 81;
    private static final int MAX_COLS = 81;
    @Test
    public void testConstructor() {
        // Test constructor initialization
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        assertEquals(player, playerBoard.getPlayer());
        assertEquals(null, playerBoard.getBoard()); // Assuming board is initialized as null
    }
    @Test
    public void testPlaceCard() {
        PlayableCard[][] board1=null;
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        PlayableCard card1 = new PlayableCard();
        player.setPlayerBoard(playerBoard);
        board1=player.getPlayerBoard().getBoard();
        board1[5][5]=card1;
        // Test placing a car
        PlayableCard card = new PlayableCard(); // Assuming a valid PlayableCard instance
        int result = playerBoard.placeCard(card, 6, 6, true); // Assuming valid coordinates
        assertEquals(0, result);
        assertNotNull(playerBoard.getBoard()[6][6]); // Assuming getBoard() returns the board
    }
    @Test
    public void testPlaceCardOccupiedPosition() {
        // Test placing a card on an already occupied position
        PlayableCard[][] board1=null;
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        PlayableCard card1 = new PlayableCard();
        player.setPlayerBoard(playerBoard);
        board1=player.getPlayerBoard().getBoard();
        board1[5][5]=card1;
        PlayableCard card2 = new PlayableCard();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            playerBoard.placeCard(card2, 5, 5, true); // Trying to place another card on the same position
        });
        assertEquals("Position already occupied/unavailable", exception.getMessage());
    }
    @Test
    public void testPlaceCardNoAdjacentCards() {
        // Test placing a card with no adjacent cards
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        PlayableCard card = new PlayableCard();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            playerBoard.placeCard(card, 5, 5, true); // Trying to place a card with no adjacent cards
        });
        assertEquals("There are no cards available to attach to", exception.getMessage());
    }
    @Test
    public void testCheckCornerVisibility(){
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);


    }
}