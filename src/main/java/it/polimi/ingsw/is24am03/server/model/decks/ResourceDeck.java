package it.polimi.ingsw.is24am03.server.model.decks;


import it.polimi.ingsw.is24am03.server.model.cards.Corner;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class employed to manage the deck which contains all Resource cards.
 */
public class ResourceDeck extends Deck {

    /**
     * This field contains a list of all Resource Cards.
     */
    private final ArrayList<PlayableCard> cards;

    /**
     * Class' constructor.
     */
    public ResourceDeck() {
        this.cards = new ArrayList<>();

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
        PlayableCard card_1 = new ResourceCard(1,"R",0,fungi, fungi, empty, notVisible,empty,empty,empty,empty);
        PlayableCard card_2 = new ResourceCard(2,"R",0,empty, notVisible, fungi, fungi,empty,empty,empty,empty);
        PlayableCard card_3 = new ResourceCard(3,"R",0,notVisible, fungi, fungi, empty,empty,empty,empty,empty);
        PlayableCard card_4 = new ResourceCard(4,"R",0,notVisible, quill, fungi, plant,empty,empty,empty,empty);
        PlayableCard card_5 = new ResourceCard(5,"R",0,inkwell, fungi, animal, notVisible,empty,empty,empty,empty);
        PlayableCard card_6 = new ResourceCard(6,"R",0,fungi, insect, empty, manuscript,empty,empty,empty,empty);
        PlayableCard card_7 = new ResourceCard(7,"R",1,empty, fungi, notVisible, empty,empty,empty,empty,empty);
        PlayableCard card_8 = new ResourceCard(8,"R",1,fungi, notVisible, empty, empty,empty,empty,empty,empty);
        PlayableCard card_9 = new ResourceCard(9,"R",1,notVisible, empty, empty, fungi,empty,empty,empty,empty);
        PlayableCard card_10 = new ResourceCard(10,"G",0,plant, empty, notVisible, plant,empty,empty,empty,empty);
        PlayableCard card_11 = new ResourceCard(11,"G",0,plant, plant, empty, notVisible,empty,empty,empty,empty);
        PlayableCard card_12 = new ResourceCard(12,"G",0,empty, notVisible, plant, plant,empty,empty,empty,empty);
        PlayableCard card_13 = new ResourceCard(13,"G",0,notVisible, plant, plant, empty,empty,empty,empty,empty);
        PlayableCard card_14 = new ResourceCard(14,"G",0,notVisible, insect, plant, quill,empty,empty,empty,empty);
        PlayableCard card_15 = new ResourceCard(15,"G",0,fungi, plant, inkwell, notVisible,empty,empty,empty,empty);
        PlayableCard card_16 = new ResourceCard(16,"G",0,manuscript, notVisible, animal, plant,empty,empty,empty,empty);
        PlayableCard card_17 = new ResourceCard(17,"G",1,empty, empty, notVisible, plant,empty,empty,empty,empty);
        PlayableCard card_18 = new ResourceCard(18,"G",1,empty, empty, plant, notVisible,empty,empty,empty,empty);
        PlayableCard card_19 = new ResourceCard(19,"G",1,notVisible, plant, empty, empty,empty,empty,empty,empty);
        PlayableCard card_20 = new ResourceCard(20,"B",0,animal, animal, notVisible, empty,empty,empty,empty,empty);
        PlayableCard card_21 = new ResourceCard(21,"B",0,notVisible, empty, animal, animal,empty,empty,empty,empty);
        PlayableCard card_22 = new ResourceCard(22,"B",0,animal, notVisible, empty, animal,empty,empty,empty,empty);
        PlayableCard card_23 = new ResourceCard(23,"B",0,empty, animal, animal, notVisible,empty,empty,empty,empty);
        PlayableCard card_24 = new ResourceCard(24,"B",0,notVisible, insect, animal, inkwell,empty,empty,empty,empty);
        PlayableCard card_25 = new ResourceCard(25,"B",0,plant, animal, manuscript, notVisible,empty,empty,empty,empty);
        PlayableCard card_26 = new ResourceCard(26,"B",0,quill, notVisible,fungi, animal,empty,empty,empty,empty);
        PlayableCard card_27 = new ResourceCard(27,"B",1,notVisible, empty, empty, plant,empty,empty,empty,empty);
        PlayableCard card_28 = new ResourceCard(28,"B",1,empty, notVisible, animal, empty,empty,empty,empty,empty);
        PlayableCard card_29 = new ResourceCard(29,"B",1,empty, animal, notVisible, empty,empty,empty,empty,empty);
        PlayableCard card_30 = new ResourceCard(30,"P",0,insect, insect, notVisible, empty,empty,empty,empty,empty);
        PlayableCard card_31 = new ResourceCard(31,"P",0,notVisible, empty, insect, insect,empty,empty,empty,empty);
        PlayableCard card_32 = new ResourceCard(32,"P",0,insect, notVisible, empty, insect,empty,empty,empty,empty);
        PlayableCard card_33 = new ResourceCard(33,"P",0,empty, insect, insect, empty,empty,empty,empty,empty);
        PlayableCard card_34 = new ResourceCard(34,"P",0,notVisible, quill, insect, animal,empty,empty,empty,empty);
        PlayableCard card_35 = new ResourceCard(35,"P",0,manuscript, insect, fungi, notVisible,empty,empty,empty,empty);
        PlayableCard card_36 = new ResourceCard(36,"P",0,insect, plant, notVisible, inkwell,empty,empty,empty,empty);
        PlayableCard card_37 = new ResourceCard(37,"P",1,insect, notVisible, empty, empty,empty,empty,empty,empty);
        PlayableCard card_38 = new ResourceCard(38,"P",1,empty, empty, insect, notVisible,empty,empty,empty,empty);
        PlayableCard card_39 = new ResourceCard(39,"P",1,notVisible, insect, empty, empty,empty,empty,empty,empty);

        cards.add(card_0);
        cards.add(card_1);
        cards.add(card_2);
        cards.add(card_3);
        cards.add(card_4);
        cards.add(card_5);
        cards.add(card_6);
        cards.add(card_7);
        cards.add(card_8);
        cards.add(card_9);
        cards.add(card_10);
        cards.add(card_11);
        cards.add(card_12);
        cards.add(card_13);
        cards.add(card_14);
        cards.add(card_15);
        cards.add(card_16);
        cards.add(card_17);
        cards.add(card_18);
        cards.add(card_19);
        cards.add(card_20);
        cards.add(card_21);
        cards.add(card_22);
        cards.add(card_23);
        cards.add(card_24);
        cards.add(card_25);
        cards.add(card_26);
        cards.add(card_27);
        cards.add(card_28);
        cards.add(card_29);
        cards.add(card_30);
        cards.add(card_31);
        cards.add(card_32);
        cards.add(card_33);
        cards.add(card_34);
        cards.add(card_35);
        cards.add(card_36);
        cards.add(card_37);
        cards.add(card_38);
        cards.add(card_39);

    }
    /**
     * This method allows us to shuffle a deck of cards.
     */
    @Override
    public void shuffle() {

        Collections.shuffle(this.cards, new Random());
    }

    /**
     * This method allows drawing a card from one of the decks.
     * @return the card at the top of the deck.
     */
    @Override
    public PlayableCard drawCard() {
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

    public ArrayList<PlayableCard> getCards(){
        return cards;
    }

    public void setEmpty(){
        cards.clear();
    }
}
