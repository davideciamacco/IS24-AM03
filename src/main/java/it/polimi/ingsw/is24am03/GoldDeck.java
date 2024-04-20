package it.polimi.ingsw.is24am03;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Class employed to manage the deck which contains all Gold cards.
 */
public class GoldDeck extends Deck{

    /**
     * This field contains a list of all Gold Cards
     */
    private final ArrayList<PlayableCard> cards;

    /**
     * Class' constructor.
     */
    public GoldDeck() {
        this.cards = new ArrayList<>();

        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");

        ArrayList<CornerItem> list = new ArrayList<>();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_40 = new GoldCard(40,"R",1,notVisible,empty,quill,empty,empty,empty,empty,empty,list,0,CornerItem.QUILL);
        list.clear();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_41 = new GoldCard(41,"R",1,empty,inkwell,empty,notVisible,empty,empty,empty,empty,list,0,CornerItem.INKWELL);
        list.clear();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.INSECT);
        PlayableCard card_42 = new GoldCard(42,"R",1,manuscript,empty,notVisible,empty,empty,empty,empty,empty,list,0,CornerItem.MANUSCRIPT);
        list.clear();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_43 = new GoldCard(43,"R",2,empty,empty,empty,notVisible,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.PLANT);
        PlayableCard card_44 = new GoldCard(44,"R",2,empty,empty,notVisible,empty,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.INSECT);
        PlayableCard card_45 = new GoldCard(45,"R",2,empty,notVisible,empty,empty,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        PlayableCard card_46 = new GoldCard(46,"R",3,empty,notVisible,notVisible,inkwell,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        PlayableCard card_47 = new GoldCard(47,"R",3,quill,empty,notVisible,notVisible,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        PlayableCard card_48 = new GoldCard(48,"R",3,notVisible,manuscript,empty,notVisible,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        PlayableCard card_49 = new GoldCard(49,"R",5,empty,notVisible,notVisible,empty,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.INSECT);
        PlayableCard card_50 = new GoldCard(50,"G",1,quill,empty,notVisible,empty,empty,empty,empty,empty,list,0,CornerItem.QUILL);
        list.clear();

        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.FUNGI);
        PlayableCard card_51 = new GoldCard(51,"G",1,empty,manuscript,empty,notVisible,empty,empty,empty,empty,list,0,CornerItem.MANUSCRIPT);
        list.clear();

        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_52 = new GoldCard(52,"G",1,empty,notVisible,empty,inkwell,empty,empty,empty,empty,list,0,CornerItem.INKWELL);
        list.clear();

        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.INSECT);
        PlayableCard card_53 = new GoldCard(53,"G",2,notVisible,empty,empty,empty,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_54 = new GoldCard(54,"G",2,empty,empty,notVisible,empty,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.FUNGI);
        PlayableCard card_55 = new GoldCard(55,"G",2,empty,notVisible,empty,empty,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        PlayableCard card_56 = new GoldCard(56,"G",3,empty,notVisible,notVisible,quill,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        PlayableCard card_57 = new GoldCard(57,"G",3,manuscript,empty,notVisible,notVisible,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        PlayableCard card_58 = new GoldCard(58,"G",3,notVisible,inkwell,empty,notVisible,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.PLANT);
        PlayableCard card_59 = new GoldCard(59,"G",5,empty,empty,notVisible,notVisible,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.INSECT);
        PlayableCard card_60 = new GoldCard(60,"B",1,inkwell,empty,notVisible,empty,empty,empty,empty,empty,list,0,CornerItem.INKWELL);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.PLANT);
        PlayableCard card_61 = new GoldCard(61,"B",1,notVisible,empty,manuscript,empty,empty,empty,empty,empty,list,0,CornerItem.MANUSCRIPT);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.FUNGI);
        PlayableCard card_62 = new GoldCard(62,"B",1,empty,notVisible,empty,quill,empty,empty,empty,empty,list,0,CornerItem.QUILL);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.INSECT);
        PlayableCard card_63 = new GoldCard(63,"B",2,empty,empty,empty,notVisible,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.FUNGI);
        PlayableCard card_64 = new GoldCard(64,"B",2,empty,notVisible,empty,empty,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.PLANT);
        PlayableCard card_65 = new GoldCard(65,"B",2,notVisible,empty,empty,empty,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_66 = new GoldCard(66,"B",3,empty,notVisible,notVisible,manuscript,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_67 = new GoldCard(67,"B",3,empty,inkwell,notVisible,notVisible,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_68 = new GoldCard(68,"B",3,notVisible,empty,quill,notVisible,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_69 = new GoldCard(69,"B",5,notVisible,empty,empty,notVisible,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.PLANT);
        PlayableCard card_70 = new GoldCard(70,"P",1,empty,quill,empty,notVisible,empty,empty,empty,empty,list,0,CornerItem.QUILL);
        list.clear();

        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_71 = new GoldCard(71,"P",1,empty,notVisible,empty,manuscript,empty,empty,empty,empty,list,0,CornerItem.MANUSCRIPT);
        list.clear();

        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.FUNGI);
        PlayableCard card_72 = new GoldCard(72,"P",1,notVisible,empty,inkwell,empty,empty,empty,empty,empty,list,0,CornerItem.QUILL);
        list.clear();

        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.ANIMAL);
        PlayableCard card_73 = new GoldCard(73,"P",2,empty,empty,empty,notVisible,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.PLANT);
        PlayableCard card_74 = new GoldCard(74,"P",2,empty,empty,notVisible,empty,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.FUNGI);
        PlayableCard card_75 = new GoldCard(75,"P",2,empty,notVisible,empty,empty,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        PlayableCard card_76 = new GoldCard(76,"P",3,inkwell,notVisible,notVisible,empty,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        PlayableCard card_77 = new GoldCard(77,"P",3,empty,manuscript,notVisible,notVisible,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        PlayableCard card_78 = new GoldCard(78,"P",3,notVisible,notVisible,empty,quill,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.INSECT);
        PlayableCard card_79 = new GoldCard(79,"P",5,empty,empty,notVisible,notVisible,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();

        cards.add(card_40);
        cards.add(card_41);
        cards.add(card_42);
        cards.add(card_43);
        cards.add(card_44);
        cards.add(card_45);
        cards.add(card_46);
        cards.add(card_47);
        cards.add(card_48);
        cards.add(card_49);
        cards.add(card_50);
        cards.add(card_51);
        cards.add(card_52);
        cards.add(card_53);
        cards.add(card_54);
        cards.add(card_55);
        cards.add(card_56);
        cards.add(card_57);
        cards.add(card_58);
        cards.add(card_59);
        cards.add(card_60);
        cards.add(card_61);
        cards.add(card_62);
        cards.add(card_63);
        cards.add(card_64);
        cards.add(card_65);
        cards.add(card_66);
        cards.add(card_67);
        cards.add(card_68);
        cards.add(card_69);
        cards.add(card_70);
        cards.add(card_71);
        cards.add(card_72);
        cards.add(card_73);
        cards.add(card_74);
        cards.add(card_75);
        cards.add(card_76);
        cards.add(card_77);
        cards.add(card_78);
        cards.add(card_79);

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
        return cards.remove(0);
    }
    /**
     * This method checks whether the deck is empty.
     * @return true is the deck is empty, otherwise false.
     */

    @Override
    public boolean isEmpty(){
        return cards.isEmpty();
    }
}