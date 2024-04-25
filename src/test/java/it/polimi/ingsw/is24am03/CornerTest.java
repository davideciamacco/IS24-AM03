package it.polimi.ingsw.is24am03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CornerTest {
    //testo il fatto che mi venga lanciata un eccezione se metto un valore che non è tra quelli A,F,I,P,M,K,Q,E,X

    @Test
    public void checkValues(){
        IllegalArgumentException e=
                assertThrows(IllegalArgumentException.class, () -> new Corner("o"), "Expected thrown, but it didn't");
        assertEquals(e.getMessage(), "Opzione non valida: " + "o");
    }
    //Faccio check che per ogni valore corretto mi venga effettivamente restituito un angolo che abbia quel corner item
    @Test
    public void checkCornerItem(){
        assertEquals(new Corner("A").getItem(), CornerItem.ANIMAL);
        assertEquals(new Corner("F").getItem(),CornerItem.FUNGI);
        assertEquals(new Corner("I").getItem(),CornerItem.INSECT);
        assertEquals(new Corner("P").getItem(),CornerItem.PLANT);
        assertEquals(new Corner("M").getItem(),CornerItem.MANUSCRIPT);
        assertEquals(new Corner("K").getItem(),CornerItem.INKWELL);
        assertEquals(new Corner("Q").getItem(),CornerItem.QUILL);
        assertEquals(new Corner("E").getItem(),CornerItem.EMPTY);
        assertEquals(new Corner("X").getItem(),CornerItem.EMPTY);
    }
//per ogni corner faccio in modo che check visibilità sia effettivamente quella richiesta

    @Test
    void isVisible() {
        //per A,F,I,P,M,K,Q,E is visible è true
        assertTrue(new Corner("A").isVisible());
        assertTrue(new Corner("F").isVisible());
        assertTrue(new Corner("I").isVisible());
        assertTrue(new Corner("P").isVisible());
        assertTrue(new Corner("M").isVisible());
        assertTrue(new Corner("K").isVisible());
        assertTrue(new Corner("Q").isVisible());
        assertTrue(new Corner("E").isVisible());
        assertFalse(new Corner("X").isVisible());

    }

    @Test
    void isEmpty() {
        assertTrue(new Corner("E").isEmpty());
        assertTrue(new Corner("X").isEmpty());
        assertFalse(new Corner("A").isEmpty());
        assertFalse(new Corner("F").isEmpty());
        assertFalse(new Corner("I").isEmpty());
        assertFalse(new Corner("P").isEmpty());
        assertFalse(new Corner("M").isEmpty());
        assertFalse(new Corner("K").isEmpty());
        assertFalse(new Corner("Q").isEmpty());

    }

}