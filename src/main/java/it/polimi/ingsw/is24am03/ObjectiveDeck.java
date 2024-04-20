package it.polimi.ingsw.is24am03;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class employed to manage the deck which contains all Objective cards.
 */
public class ObjectiveDeck extends Deck {

    /**
     * This field contains a list of all Objective cards.
     */
    private final ArrayList<ObjectiveCard> cards;

    /**
     * Class' constructor.
     */
    public ObjectiveDeck(){
        ArrayList<ObjectiveCard> cards = new ArrayList<>();

        ObjectiveCard card_86 = new Dshaped(86,2,CornerItem.FUNGI,ObjectiveType.PATTERNDIAGONAL,CornerItem.FUNGI,1);
        ObjectiveCard card_87 = new Dshaped(87,2,CornerItem.PLANT,ObjectiveType.PATTERNDIAGONAL,CornerItem.PLANT,0);
        ObjectiveCard card_88 = new Dshaped(88,2,CornerItem.ANIMAL,ObjectiveType.PATTERNDIAGONAL,CornerItem.ANIMAL,1);
        ObjectiveCard card_89 = new Dshaped(89,2,CornerItem.INSECT,ObjectiveType.PATTERNDIAGONAL,CornerItem.INSECT,0);

        ObjectiveCard card_90 = new Lshaped(90,3,CornerItem.FUNGI,ObjectiveType.PATTERNL,CornerItem.FUNGI,2,CornerItem.PLANT);
        ObjectiveCard card_91 = new Lshaped(91,3,CornerItem.PLANT,ObjectiveType.PATTERNL,CornerItem.PLANT,3,CornerItem.INSECT);
        ObjectiveCard card_92 = new Lshaped(92,3,CornerItem.ANIMAL,ObjectiveType.PATTERNL,CornerItem.ANIMAL,1,CornerItem.FUNGI);
        ObjectiveCard card_93 = new Lshaped(93,3,CornerItem.INSECT,ObjectiveType.PATTERNL,CornerItem.INSECT,0,CornerItem.ANIMAL);

        ObjectiveCard card_94 = new ObjectiveList(94,2,CornerItem.FUNGI,ObjectiveType.ITEM,CornerItem.EMPTY,3);
        ObjectiveCard card_95 = new ObjectiveList(95,2,CornerItem.PLANT,ObjectiveType.ITEM,CornerItem.EMPTY,3);
        ObjectiveCard card_96 = new ObjectiveList(96,2,CornerItem.ANIMAL,ObjectiveType.ITEM,CornerItem.EMPTY,3);
        ObjectiveCard card_97 = new ObjectiveList(97,2,CornerItem.INSECT,ObjectiveType.ITEM,CornerItem.EMPTY,3);
        ObjectiveCard card_98 = new ObjectiveList(98,3,CornerItem.EMPTY,ObjectiveType.ITEM,CornerItem.EMPTY,2);
        ObjectiveCard card_99 = new ObjectiveList(99,2,CornerItem.EMPTY,ObjectiveType.ITEM,CornerItem.EMPTY,1);
        ObjectiveCard card_100 = new ObjectiveList(100,2,CornerItem.EMPTY,ObjectiveType.ITEM,CornerItem.EMPTY,1);
        ObjectiveCard card_101 = new ObjectiveList(101,2,CornerItem.EMPTY,ObjectiveType.ITEM,CornerItem.EMPTY,1);

        cards.add(card_86);
        cards.add(card_87);
        cards.add(card_88);
        cards.add(card_89);
        cards.add(card_90);
        cards.add(card_91);
        cards.add(card_92);
        cards.add(card_93);
        cards.add(card_94);
        cards.add(card_95);
        cards.add(card_96);
        cards.add(card_97);
        cards.add(card_98);
        cards.add(card_99);
        cards.add(card_100);
        cards.add(card_101);

        this.cards=cards;

    }
    /**
     * This method allows us to shuffle a deck of cards.
     */
    @Override
    public void shuffle() { //va aggiunta un eccezione per verificare che ho effettivamente mischiato le carte
        Collections.shuffle(this.cards, new Random());
    }
    /**
     * This method allows drawing a card from one of the decks.
     * @return the card at the top of the deck.
     */
    @Override
    public ObjectiveCard drawCard(){
        return this.cards.remove(0);
    }
    /**
     * This method checks whether the deck is empty.
     * @return true is the deck is empty,otherwise false.
     */
    @Override
    public boolean isEmpty(){
        return cards.isEmpty();
    }
}
