package it.polimi.ingsw.is24am03;


import it.polimi.ingsw.is24am03.server.model.cards.Corner;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.decks.GoldDeck;
import it.polimi.ingsw.is24am03.server.model.decks.ResourceDeck;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class used to check if the resource deck is configured correctly
 */
class ResourceDeckTest {

    @Test
    public void removeTest() {
        ResourceDeck s = new ResourceDeck();
        PlayableCard p = s.getCards().get(0);
        assertEquals(p, s.drawCard());
    }

    @Test
    public void checkSize() {
        ResourceDeck s = new ResourceDeck();
        int old_size = s.getCards().size();
        PlayableCard sc = s.drawCard();
        assertEquals(s.getCards().size(), old_size - 1);
    }

    @Test
    public void isEmptyCheck() {
        ResourceDeck s = new ResourceDeck();
        for (int i = 0; i < 40; i++) {
            s.drawCard();
        }
        assertTrue(s.isEmpty());
    }

    @Test
    public void checkRnd() {
        ResourceDeck s = new ResourceDeck();
        ResourceDeck post = new ResourceDeck();
        post.shuffle();
        assertEquals(s.getCards().size(), post.getCards().size());

    }

    @Test
    public void checkType() {
        ResourceDeck s = new ResourceDeck();
        for (int i = 0; i < 40; i++) {
            assertEquals(s.getCards().get(i).getType(), 1);
        }

    }

    @Test
    public void checkScoringType() {
        ResourceDeck s = new ResourceDeck();
        for (int i = 0; i < 40; i++) {
            assertEquals(s.getCards().get(i).getScoringType(), -1);
        }

    }

    @Test
    public void checkRes() {
        ResourceDeck s = new ResourceDeck();
        assertEquals(s.getCards().get(0).getKingdomsType(), CornerItem.FUNGI);
    }

    @Test
    public void checkObj() {
        ResourceDeck s = new ResourceDeck();
        for (int i = 0; i < 40; i++) {
            assertEquals(s.getCards().get(i).getObject(), CornerItem.EMPTY);
        }

    }

    @Test
    public void checkList() {
        ResourceDeck s = new ResourceDeck();
        for (int i = 0; i < 40; i++) {
            assertTrue(s.getCards().get(i).getRequirements().isEmpty());
        }

    }
    @Test
    public void checkException(){
        Corner fungi =new Corner("F");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        IllegalArgumentException e= assertThrows(IllegalArgumentException.class, () -> new ResourceCard(0,"K",0,fungi, empty, notVisible, fungi,empty,empty,empty,empty), "Expected thrown, but it didn't");
        assertEquals(e.getMessage(), "Opzione non valida: " + "K");
    }

    @Test
    public void checkGetterCorners(){
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");

        PlayableCard card_0 = new ResourceCard(0,"R",0,fungi, empty, notVisible, fungi,empty,empty,empty,empty);
        assertEquals(card_0.getFrontCorner(0), fungi);
        assertEquals(card_0.getBackCorner(0), empty);
        card_0.rotate();
        assertFalse(card_0.getFace());
        card_0.setCardAlreadyPlaced();
        assertTrue(card_0.getCardAlreadyPlaced());
    }
    @Test
    public void checksettingCoverage(){
        Corner fungi =new Corner("F");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        PlayableCard card_0 = new ResourceCard(0,"R",0,fungi, empty, notVisible, fungi,empty,empty,empty,empty);
        card_0.setCoverage(0);
        assertTrue(card_0.getFront().get(0).getValue());
        card_0.rotate();
        card_0.setCoverage(0);
        assertTrue(card_0.getBack().get(0).getValue());

    }

    void drawCard(){
        ResourceDeck d=new ResourceDeck();
        d.getCards().get(0).drawCard();
    }
}

