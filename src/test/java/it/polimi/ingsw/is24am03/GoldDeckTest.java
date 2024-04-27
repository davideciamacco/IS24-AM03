package it.polimi.ingsw.is24am03;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.decks.GoldDeck;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class GoldDeckTest {
    @Test
    public void removeTest(){
        // controllo che la prima carta sia quella che è effettivamente la prima
        GoldDeck s= new GoldDeck();
        PlayableCard p =s.getCards().get(0);
        assertEquals(p,s.drawCard());
    }

    @Test
    public void checkSize(){
        //controllo che dopo remove la size sia diminuita di 1
        GoldDeck s= new GoldDeck();
        int old_size= s.getCards().size();
        PlayableCard sc= s.drawCard();
        assertEquals(s.getCards().size(), old_size-1) ;
    }

    // controllo che mi dica correttamente se il deck è empty
    @Test
    public void isEmptyCheck(){
        //controllo che dopo 6 estrazioni ho il deck effettivamente vuoto
        GoldDeck s= new GoldDeck();
        for(int i=0; i<40; i++) {
            s.drawCard();
        }
        assertTrue(s.isEmpty());
    }
    //mi assicuro che dopo il random tutte le carte contenute prima siano le stesse contenute ora
    @Test
    public void checkRnd(){
        GoldDeck s= new GoldDeck();
        GoldDeck post= new GoldDeck();
        post.shuffle();
        assertEquals(s.getCards().size(), post.getCards().size());

    }
    @Test
    public void checkReqList(){
        GoldDeck s= new GoldDeck();
        ArrayList<CornerItem> a= new ArrayList<CornerItem>();
        a.add(CornerItem.FUNGI);
        a.add(CornerItem.FUNGI);
        a.add(CornerItem.ANIMAL);
        assertEquals(s.getCards().get(0).getRequirements(), a);

    }
    @Test
    public void checkGetObj(){
        GoldDeck s= new GoldDeck();
        assertEquals(s.getCards().get(0).getObject(),CornerItem.QUILL);
    }

    @Test
    public void checkScoringType(){
        GoldDeck s=new GoldDeck();
        assertEquals(s.getCards().get(0).getScoringType(), 0);
    }
    @Test
    public void checkType(){
        GoldDeck d=new GoldDeck();
        for(PlayableCard g: d.getCards()){
            assertEquals(g.getType(),-1);
        }
    }




}


