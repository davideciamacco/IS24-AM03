package it.polimi.ingsw.is24am03;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.decks.ObjectiveDeck;
import it.polimi.ingsw.is24am03.server.model.enums.ObjectiveType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveDeckTest {

    @Test
    public void removeTest(){
        ObjectiveDeck s= new ObjectiveDeck();
        ObjectiveCard p =s.getCards().get(0);
        assertEquals(p,s.drawCard());
    }

    @Test
    public void checkSize(){
        ObjectiveDeck s= new ObjectiveDeck();
        int old_size= s.getCards().size();
        ObjectiveCard sc= s.drawCard();
        assertEquals(s.getCards().size(), old_size-1) ;
    }

    @Test
    public void isEmptyCheck(){
        ObjectiveDeck s= new ObjectiveDeck();
        for(int i=0; i<16; i++) {
            s.drawCard();
        }
        assertTrue(s.isEmpty());
    }
    @Test
    public void checkRnd(){
        ObjectiveDeck s= new ObjectiveDeck();
        ObjectiveDeck post= new ObjectiveDeck();
        post.shuffle();
        assertEquals(s.getCards().size(), post.getCards().size());

    }

    @Test
    public void getKingdomsCheck(){
        ObjectiveDeck o= new ObjectiveDeck();
        assertEquals(o.getCards().get(0).getKingdomsType(), CornerItem.FUNGI);
        assertEquals(o.getCards().get(0).getType(), ObjectiveType.PATTERNDIAGONAL);
        assertEquals(o.getCards().get(0).getRequirement(),CornerItem.FUNGI);
        assertEquals(o.getCards().get(0).getDirection(), 1);
        ObjectiveCard card = new ObjectiveCard(86,2,CornerItem.FUNGI,ObjectiveType.PATTERNDIAGONAL,CornerItem.FUNGI);
        assertEquals(card.getDirection(), -1);
        assertEquals(card.getTypeList(),-1);
        assertEquals(card.getCorner(),-1);
        assertEquals(card.getSecondColor(), CornerItem.EMPTY);
    }


    @Test
    public void checkTypeList(){
        ObjectiveDeck o= new ObjectiveDeck();
        assertEquals(o.getCards().get(8).getTypeList(),3);
    }

    @Test
    public void checkgetCorner(){
        ObjectiveDeck o= new ObjectiveDeck();
        assertEquals(o.getCards().get(4).getCorner(),2);
    }


    @Test
    public void check_SecondColor_Points_Id(){
        ObjectiveDeck o= new ObjectiveDeck();
        assertEquals(o.getCards().get(4).getSecondColor(),CornerItem.PLANT);
        assertEquals(o.getCards().get(4).getPoints(),3);
        assertEquals(o.getCards().get(4).getId(),90);

    }







}



