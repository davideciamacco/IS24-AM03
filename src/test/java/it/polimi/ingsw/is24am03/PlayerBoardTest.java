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
        player = new Player("TestPlayer");
        playerBoard = new PlayerBoard(player);
        player.setPlayerBoard(playerBoard);
        assertNotNull(playerBoard);
        assertEquals(player, playerBoard.getPlayer());
        assertNotNull(playerBoard.getBoard());
        assertEquals(81, playerBoard.getBoard().length);
        assertEquals(81, playerBoard.getBoard()[0].length);
        // Check that every index of availableItems has been initialized with value 0
        Map<CornerItem, Integer> availableItems = playerBoard.getAvailableItems();
        assertNotNull(availableItems);
        for (Integer value : availableItems.values()) {
            assertEquals(0, (int) value);
        }
    }
    @Test
    public void testCheckFrontCornerVisibilityNotVisible() {
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        player.setPlayerBoard(playerBoard);
        Corner corn= new Corner();
        PlayableCard card = new PlayableCard();
        //setto come corner 0 il corner creato FRONT per la carta e lo imposto non visibile
        playerBoard.getBoard()[0][0] = card;
        //la face della carta dev essere impostata true

        // Verifica che il metodo lanci correttamente un'eccezione quando il corner non è visibile
        assertThrows(IllegalArgumentException.class, () -> {
            playerBoard.checkCornerVisibility(0, 0, 0);
        });
    }
    @Test
    public void testCheckBackCornerVisibilityNotVisible() {
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        player.setPlayerBoard(playerBoard);
        Corner corn= new Corner();
        PlayableCard card = new PlayableCard();
        //setto come corner 0 il corner BACK creato per la carta e lo imposto non visibile
        playerBoard.getBoard()[0][0] = card;
        //la face della carta dev essere impostata true

        // Verifica che il metodo lanci correttamente un'eccezione quando il corner non è visibile
        assertThrows(IllegalArgumentException.class, () -> {
            playerBoard.checkCornerVisibility(0, 0, 0);
        });
    }
    public void testIncreaseItemCount() {
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        player.setPlayerBoard(playerBoard);

        assertEquals(0, playerBoard.getAvailableItems().get(CornerItem.QUILL));
        assertEquals(0, playerBoard.getAvailableItems().get(CornerItem.INKWELL));
        assertEquals(0, playerBoard.getAvailableItems().get(CornerItem.MANUSCRIPT));
        assertEquals(0, playerBoard.getAvailableItems().get(Resources.INSECT));
        assertEquals(0, playerBoard.getAvailableItems().get(Resources.FINGI));
        assertEquals(0, playerBoard.getAvailableItems().get(Resources.PLANT));
        assertEquals(0, playerBoard.getAvailableItems().get(Resources.ANIMAL));
        PlayableCard card = new PlayableCard();
        // Aggiungi la carta al tavolo del giocatore
        playerBoard.getBoard()[0][0] = card;
        playerBoard.increaseItemCount(card);
        //SUPPONGO DI AVER AGGIUNTO UNA CARTA CON SOLO UN OGGETTO SU UN CORNER DI TIPO PLANT
        // Verifica che il conteggio degli oggetti disponibili sia aumentato correttamente dopo l'aggiunta della carta
        assertEquals(0, playerBoard.getAvailableItems().get(CornerItem.QUILL));
        assertEquals(0, playerBoard.getAvailableItems().get(CornerItem.INKWELL));
        assertEquals(0, playerBoard.getAvailableItems().get(CornerItem.MANUSCRIPT));
        assertEquals(0, playerBoard.getAvailableItems().get(Resources.INSECT));
        assertEquals(0, playerBoard.getAvailableItems().get(Resources.FINGI));
        assertEquals(1, playerBoard.getAvailableItems().get(Resources.PLANT));
        assertEquals(0, playerBoard.getAvailableItems().get(Resources.ANIMAL));
    }
    @Test
    public void testUpdateItemCount() {
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        player.setPlayerBoard(playerBoard);
        // Crea una carta e aggiungila al tavolo del giocatore
        PlayableCard card = new PlayableCard();
        //la carta dovrà avere nel proprio angolo in alto a sinistra un oggetto di tipo plant
        playerBoard.getBoard()[2][2] = card;
        playerBoard.increaseItemCount(card);
        // Controlla che il conteggio degli oggetti disponibili sia stato aumentato correttamente
        assertEquals(1, playerBoard.getAvailableItems().get(Resources.PLANT));
        PlayableCard card2 = new PlayableCard();
        playerBoard.getBoard()[1][1] = card;
        //la carta che ho aggiunto copre l oggetto pianta di prima
        // Esegui il metodo updateItemCount
        playerBoard.updateItemCount(1, 1);
        // Controlla che il conteggio degli oggetti disponibili sia diminuito correttamente dopo l'aggiornamento
        assertEquals(0, playerBoard.getAvailableItems().get(Resources.PLANT));
    }
    @Test
    public void testCheckRequirements() {
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        player.setPlayerBoard(playerBoard);
        // Crea una lista di requisiti per una carta
        ArrayList<CornerItem> cardRequirements = new ArrayList<>();
        cardRequirements.add(CornerItem.QUILL);
        cardRequirements.add(CornerItem.QUILL);

        // Aggiungi risorse alla plancia giocatore per soddisfare i requisiti
        playerBoard.getAvailableItems().put(CornerItem.QUILL, 2);
        // Verifica che il metodo restituisca true quando i requisiti sono soddisfatti
        assertTrue(playerBoard.checkRequirements(cardRequirements));

        // Rimuovi una risorsa per non soddisfare più i requisiti
        playerBoard.getAvailableItems().put(CornerItem.QUILL, 0);

        // Verifica che il metodo restituisca false quando i requisiti non sono soddisfatti
        assertFalse(playerBoard.checkRequirements(cardRequirements));
    }
    @Test
    public void testGiveCardPoints() {

    }
    @Test
    public void testcheckObjectiveList() {

    }
    @Test
    public void testcheckObjectiveDshaped() {

    }
    @Test
    public void testcheckObjectiveLShaped() {

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

}