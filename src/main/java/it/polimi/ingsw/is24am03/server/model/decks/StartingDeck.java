package it.polimi.ingsw.is24am03.server.model.decks;

import it.polimi.ingsw.is24am03.server.model.cards.Corner;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class employed to manage the deck which contains all Starting cards.
 */
public class StartingDeck extends Deck {

    /**
     * This field contains a list of all Starting Cards
     */
    private ArrayList<StartingCard> cards;

    /**
     * Class' constructor.
     */
    public StartingDeck(){

        this.cards = new ArrayList<>();
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");

        ArrayList<CornerItem> list = new ArrayList<CornerItem>();
        ArrayList<CornerItem> list1 = new ArrayList<CornerItem>();
        ArrayList<CornerItem> list2 = new ArrayList<CornerItem>();
        ArrayList<CornerItem> list3 = new ArrayList<CornerItem>();
        ArrayList<CornerItem> list4 = new ArrayList<CornerItem>();
        ArrayList<CornerItem> list5 = new ArrayList<CornerItem>();

        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);


        list1.add(CornerItem.FUNGI);
        StartingCard card_81= new StartingCard(81,0,plant,animal,insect,fungi,animal,empty,fungi,empty,list1);


        list2.add(CornerItem.FUNGI);
        list2.add(CornerItem.PLANT);
        StartingCard card_82= new StartingCard(82,0,insect,animal,plant,fungi,empty,empty,empty,empty,list2);


        list3.add(CornerItem.ANIMAL);
        list3.add(CornerItem.INSECT);
        StartingCard card_83= new StartingCard(83,0,plant,insect,fungi,animal,empty,empty,empty,empty,list3);


        list4.add(CornerItem.ANIMAL);
        list4.add(CornerItem.INSECT);
        list4.add(CornerItem.PLANT);
        StartingCard card_84= new StartingCard(84,0,insect,fungi,animal,plant,empty,empty,notVisible,notVisible,list4);


        list5.add(CornerItem.ANIMAL);
        list5.add(CornerItem.PLANT);
        list5.add(CornerItem.FUNGI);
        StartingCard card_85= new StartingCard(85,0,fungi,animal,insect,plant,empty,empty,notVisible,notVisible,list5);


        cards.add(card_80);
        cards.add(card_81);
        cards.add(card_82);
        cards.add(card_83);
        cards.add(card_84);
        cards.add(card_85);
    }
    /**
     * This method allows us to shuffle a deck of cards.
     */
    @Override
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    /**
     * This method allows drawing a card from one of the decks.
     * @return the card at the top of the deck.
     */
    @Override
    public StartingCard drawCard(){
        return cards.remove(0);
    }
    /**
     * This method checks whether the deck is empty.
     * @return true is the deck is empty,otherwise false.
     */
    @Override
    public boolean isEmpty(){
        return cards.isEmpty();
    }


    public ArrayList<StartingCard> getCards(){
        return cards;
    }


}
