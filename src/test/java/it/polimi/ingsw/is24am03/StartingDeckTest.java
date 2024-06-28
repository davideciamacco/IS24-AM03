package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.decks.StartingDeck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class used to check if the resource deck is configured correctly
 */
class StartingDeckTest {



    @Test
    public void removeTest() {
        StartingDeck s = new StartingDeck();
        StartingCard sc = s.getCards().get(0);
        assertEquals(sc, s.drawCard());
    }

    @Test
    public void checkSize() {
        StartingDeck s = new StartingDeck();
        int old_size = s.getCards().size();
        s.drawCard();
        assertEquals(s.getCards().size(), old_size - 1);
    }

    @Test
    public void isEmptyCheck() {
        StartingDeck s = new StartingDeck();
        s.drawCard();
        s.drawCard();
        s.drawCard();
        s.drawCard();
        s.drawCard();
        s.drawCard();
        assertTrue(s.isEmpty());
    }

    @Test
    public void checkRnd() {
        StartingDeck s = new StartingDeck();
        StartingDeck post = new StartingDeck();
        post.shuffle();
        assertEquals(s.getCards().size(), post.getCards().size());

    }
    @Test
    public void checkreq() {
        StartingDeck s = new StartingDeck();
        for (StartingCard sc : s.getCards()) {
            assertTrue(sc.getRequirements().isEmpty());
        }
    }

    @Test
    public void checkType() {
        StartingDeck s = new StartingDeck();
        for (StartingCard sc : s.getCards()) {
            assertEquals(sc.getType(), 0);
        }
    }

    @Test
    public void checkGetter() {
        StartingDeck s = new StartingDeck();
        assertTrue(s.getCards().get(0).getKingdomList().contains(CornerItem.INSECT));

    }
    @Test
    public void checkGetObject(){
        StartingDeck s=new StartingDeck();
        for (StartingCard sc : s.getCards()) {
            assertEquals(sc.getObject(), CornerItem.EMPTY);
        }
    }
    @Test
    public void checkKing(){
        StartingDeck s=new StartingDeck();
        for (StartingCard sc : s.getCards()) {
            assertEquals(sc.getKingdomsType(), CornerItem.EMPTY);
        }
    }

    @Test
    public void checkRequirementsEmpty(){
        StartingDeck s=new StartingDeck();
        for (StartingCard sc : s.getCards()) {
            assertTrue(sc.getRequirements().isEmpty());
        }
    }

    @Test
    public void checkScoringType(){
        StartingDeck s=new StartingDeck();
        for (StartingCard sc : s.getCards()) {
            assertEquals(sc.getScoringType(),-1);
        }
    }

}