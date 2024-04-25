package it.polimi.ingsw.is24am03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartingDeckTest {



    @Test
    public void removeTest() {
        // controllo che la prima carta sia quella che è effettivamente la prima
        StartingDeck s = new StartingDeck();
        StartingCard sc = s.getCards().get(0);
        assertEquals(sc, s.drawCard());
    }

    @Test
    public void checkSize() {
        //controllo che dopo remove la size sia diminuita di 1
        StartingDeck s = new StartingDeck();
        int old_size = s.getCards().size();
        s.drawCard();
        assertEquals(s.getCards().size(), old_size - 1);
    }

    // controllo che mi dica correttamente se il deck è empty
    @Test
    public void isEmptyCheck() {
        //controllo che dopo 6 estrazioni ho il deck effettivamente vuoto
        StartingDeck s = new StartingDeck();
        s.drawCard();
        s.drawCard();
        s.drawCard();
        s.drawCard();
        s.drawCard();
        s.drawCard();
        assertTrue(s.isEmpty());
    }

    //mi assicuro che dopo il random tutte le carte contenute prima siano le stesse contenute ora
    @Test
    public void checkRnd() {
        StartingDeck s = new StartingDeck();
        StartingDeck post = new StartingDeck();
        post.shuffle();
        assertEquals(s.getCards().size(), post.getCards().size());

    }
    @Test //check for empty list in req
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