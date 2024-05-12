package it.polimi.ingsw.is24am03.server.model.decks;



import it.polimi.ingsw.is24am03.server.model.cards.GoldCard;
import it.polimi.ingsw.is24am03.server.model.cards.Corner;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Class employed to manage the deck which contains all Gold cards.
 */
public class GoldDeck extends Deck {

    /**
     * This field contains a list of all Gold Cards
     */
    private final ArrayList<ResourceCard> cards;

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
        ResourceCard card_40 = new GoldCard(40,"R",1,notVisible,empty,quill,empty,empty,empty,empty,empty,list,0,CornerItem.QUILL);


        ArrayList<CornerItem> list1 = new ArrayList<>();

        list1.add(CornerItem.FUNGI);
        list1.add(CornerItem.FUNGI);
        list1.add(CornerItem.ANIMAL);
        ResourceCard card_41 = new GoldCard(41,"R",1,empty,inkwell,empty,notVisible,empty,empty,empty,empty,list1,0,CornerItem.INKWELL);


        ArrayList<CornerItem> list2 = new ArrayList<>();
        list2.add(CornerItem.FUNGI);
        list2.add(CornerItem.FUNGI);
        list2.add(CornerItem.INSECT);
        ResourceCard card_42 = new GoldCard(42,"R",1,manuscript,empty,notVisible,empty,empty,empty,empty,empty,list2,0,CornerItem.MANUSCRIPT);


        ArrayList<CornerItem> list3 = new ArrayList<>();

        list3.add(CornerItem.FUNGI);
        list3.add(CornerItem.FUNGI);
        list3.add(CornerItem.FUNGI);
        list3.add(CornerItem.ANIMAL);
        ResourceCard card_43 = new GoldCard(43,"R",2,empty,empty,empty,notVisible,empty,empty,empty,empty,list3,1,CornerItem.EMPTY);



        ArrayList<CornerItem> list4 = new ArrayList<>();
        list4.add(CornerItem.FUNGI);
        list4.add(CornerItem.FUNGI);
        list4.add(CornerItem.FUNGI);
        list4.add(CornerItem.PLANT);
        ResourceCard card_44 = new GoldCard(44,"R",2,empty,empty,notVisible,empty,empty,empty,empty,empty,list4,1,CornerItem.EMPTY);

        ArrayList<CornerItem> list5 = new ArrayList<>();

        list5.add(CornerItem.FUNGI);
        list5.add(CornerItem.FUNGI);
        list5.add(CornerItem.FUNGI);
        list5.add(CornerItem.INSECT);
        ResourceCard card_45 = new GoldCard(45,"R",2,empty,notVisible,empty,empty,empty,empty,empty,empty,list5,1,CornerItem.EMPTY);


        ArrayList<CornerItem> list6 = new ArrayList<>();

        list6.add(CornerItem.FUNGI);
        list6.add(CornerItem.FUNGI);
        list6.add(CornerItem.FUNGI);
        ResourceCard card_46 = new GoldCard(46,"R",3,empty,notVisible,notVisible,inkwell,empty,empty,empty,empty,list6,2,CornerItem.EMPTY);


        ArrayList<CornerItem> list7 = new ArrayList<>();

        list7.add(CornerItem.FUNGI);
        list7.add(CornerItem.FUNGI);
        list7.add(CornerItem.FUNGI);
        ResourceCard card_47 = new GoldCard(47,"R",3,quill,empty,notVisible,notVisible,empty,empty,empty,empty,list7,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list8 = new ArrayList<>();
        list8.add(CornerItem.FUNGI);
        list8.add(CornerItem.FUNGI);
        list8.add(CornerItem.FUNGI);
        ResourceCard card_48 = new GoldCard(48,"R",3,notVisible,manuscript,empty,notVisible,empty,empty,empty,empty,list8,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list9 = new ArrayList<>();
        list9.add(CornerItem.FUNGI);
        list9.add(CornerItem.FUNGI);
        list9.add(CornerItem.FUNGI);
        list9.add(CornerItem.FUNGI);
        list9.add(CornerItem.FUNGI);
        ResourceCard card_49 = new GoldCard(49,"R",5,empty,notVisible,notVisible,empty,empty,empty,empty,empty,list9,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list10 = new ArrayList<>();
        list10.add(CornerItem.PLANT);
        list10.add(CornerItem.PLANT);
        list10.add(CornerItem.INSECT);
        ResourceCard card_50 = new GoldCard(50,"G",1,quill,empty,notVisible,empty,empty,empty,empty,empty,list10,0,CornerItem.QUILL);

        ArrayList<CornerItem> list11 = new ArrayList<>();

        list11.add(CornerItem.PLANT);
        list11.add(CornerItem.PLANT);
        list11.add(CornerItem.FUNGI);
        ResourceCard card_51 = new GoldCard(51,"G",1,empty,manuscript,empty,notVisible,empty,empty,empty,empty,list11,0,CornerItem.MANUSCRIPT);

        ArrayList<CornerItem> list12 = new ArrayList<>();
        list12.add(CornerItem.PLANT);
        list12.add(CornerItem.PLANT);
        list12.add(CornerItem.ANIMAL);
        ResourceCard card_52 = new GoldCard(52,"G",1,empty,notVisible,empty,inkwell,empty,empty,empty,empty,list12,0,CornerItem.INKWELL);

        ArrayList<CornerItem> list13 = new ArrayList<>();
        list13.add(CornerItem.PLANT);
        list13.add(CornerItem.PLANT);
        list13.add(CornerItem.PLANT);
        list13.add(CornerItem.INSECT);
        ResourceCard card_53 = new GoldCard(53,"G",2,notVisible,empty,empty,empty,empty,empty,empty,empty,list13,1,CornerItem.EMPTY);

        ArrayList<CornerItem> list14 = new ArrayList<>();

        list14.add(CornerItem.PLANT);
        list14.add(CornerItem.PLANT);
        list14.add(CornerItem.PLANT);
        list14.add(CornerItem.ANIMAL);
        ResourceCard card_54 = new GoldCard(54,"G",2,empty,empty,notVisible,empty,empty,empty,empty,empty,list14,1,CornerItem.EMPTY);


        ArrayList<CornerItem> list15 = new ArrayList<>();
        list15.add(CornerItem.PLANT);
        list15.add(CornerItem.PLANT);
        list15.add(CornerItem.PLANT);
        list15.add(CornerItem.FUNGI);
        ResourceCard card_55 = new GoldCard(55,"G",2,empty,notVisible,empty,empty,empty,empty,empty,empty,list15,1,CornerItem.EMPTY);

        ArrayList<CornerItem> list16 = new ArrayList<>();
        list16.add(CornerItem.PLANT);
        list16.add(CornerItem.PLANT);
        list16.add(CornerItem.PLANT);
        ResourceCard card_56 = new GoldCard(56,"G",3,empty,notVisible,notVisible,quill,empty,empty,empty,empty,list16,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list17 = new ArrayList<>();
        list17.add(CornerItem.PLANT);
        list17.add(CornerItem.PLANT);
        list17.add(CornerItem.PLANT);
        ResourceCard card_57 = new GoldCard(57,"G",3,manuscript,empty,notVisible,notVisible,empty,empty,empty,empty,list17,2,CornerItem.EMPTY);
        ArrayList<CornerItem> list18 = new ArrayList<>();
        list18.add(CornerItem.PLANT);
        list18.add(CornerItem.PLANT);
        list18.add(CornerItem.PLANT);

        ResourceCard card_58 = new GoldCard(58,"G",3,notVisible,inkwell,empty,notVisible,empty,empty,empty,empty,list18,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list19 = new ArrayList<>();
        list19.add(CornerItem.PLANT);
        list19.add(CornerItem.PLANT);
        list19.add(CornerItem.PLANT);
        list19.add(CornerItem.PLANT);
        list19.add(CornerItem.PLANT);
        ResourceCard card_59 = new GoldCard(59,"G",5,empty,empty,notVisible,notVisible,empty,empty,empty,empty,list19,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list20 = new ArrayList<>();
        list20.add(CornerItem.ANIMAL);
        list20.add(CornerItem.ANIMAL);
        list20.add(CornerItem.INSECT);
        ResourceCard card_60 = new GoldCard(60,"B",1,inkwell,empty,notVisible,empty,empty,empty,empty,empty,list20,0,CornerItem.INKWELL);

        ArrayList<CornerItem> list21 = new ArrayList<>();
        list21.add(CornerItem.ANIMAL);
        list21.add(CornerItem.ANIMAL);
        list21.add(CornerItem.PLANT);
        ResourceCard card_61 = new GoldCard(61,"B",1,notVisible,empty,manuscript,empty,empty,empty,empty,empty,list21,0,CornerItem.MANUSCRIPT);

        ArrayList<CornerItem> list22 = new ArrayList<>();
        list22.add(CornerItem.ANIMAL);
        list22.add(CornerItem.ANIMAL);
        list22.add(CornerItem.FUNGI);
        ResourceCard card_62 = new GoldCard(62,"B",1,empty,notVisible,empty,quill,empty,empty,empty,empty,list22,0,CornerItem.QUILL);

        ArrayList<CornerItem> list23 = new ArrayList<>();
        list23.add(CornerItem.ANIMAL);
        list23.add(CornerItem.ANIMAL);
        list23.add(CornerItem.ANIMAL);
        list23.add(CornerItem.INSECT);
        ResourceCard card_63 = new GoldCard(63,"B",2,empty,empty,empty,notVisible,empty,empty,empty,empty,list23,1,CornerItem.EMPTY);

        ArrayList<CornerItem> list24 = new ArrayList<>();
        list24.add(CornerItem.ANIMAL);
        list24.add(CornerItem.ANIMAL);
        list24.add(CornerItem.ANIMAL);
        list24.add(CornerItem.FUNGI);
        ResourceCard card_64 = new GoldCard(64,"B",2,empty,notVisible,empty,empty,empty,empty,empty,empty,list24,1,CornerItem.EMPTY);

        ArrayList<CornerItem> list25 = new ArrayList<>();
        list25.add(CornerItem.ANIMAL);
        list25.add(CornerItem.ANIMAL);
        list25.add(CornerItem.ANIMAL);
        list25.add(CornerItem.PLANT);
        ResourceCard card_65 = new GoldCard(65,"B",2,notVisible,empty,empty,empty,empty,empty,empty,empty,list25,1,CornerItem.EMPTY);

        ArrayList<CornerItem> list26 = new ArrayList<>();
        list26.add(CornerItem.ANIMAL);
        list26.add(CornerItem.ANIMAL);
        list26.add(CornerItem.ANIMAL);
        ResourceCard card_66 = new GoldCard(66,"B",3,empty,notVisible,notVisible,manuscript,empty,empty,empty,empty,list26,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list67= new ArrayList<>();
        list67.add(CornerItem.ANIMAL);
        list67.add(CornerItem.ANIMAL);
        list67.add(CornerItem.ANIMAL);
        ResourceCard card_67 = new GoldCard(67,"B",3,empty,inkwell,notVisible,notVisible,empty,empty,empty,empty,list67,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list68= new ArrayList<>();
        list68.add(CornerItem.ANIMAL);
        list68.add(CornerItem.ANIMAL);
        list68.add(CornerItem.ANIMAL);
        ResourceCard card_68 = new GoldCard(68,"B",3,notVisible,empty,quill,notVisible,empty,empty,empty,empty,list68,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list69= new ArrayList<>();
        list69.add(CornerItem.ANIMAL);
        list69.add(CornerItem.ANIMAL);
        list69.add(CornerItem.ANIMAL);
        list69.add(CornerItem.ANIMAL);
        list69.add(CornerItem.ANIMAL);
        ResourceCard card_69 = new GoldCard(69,"B",5,notVisible,empty,empty,notVisible,empty,empty,empty,empty,list69,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list70= new ArrayList<>();
        list70.add(CornerItem.INSECT);
        list70.add(CornerItem.INSECT);
        list70.add(CornerItem.PLANT);
        ResourceCard card_70 = new GoldCard(70,"P",1,empty,quill,empty,notVisible,empty,empty,empty,empty,list70,0,CornerItem.QUILL);

        ArrayList<CornerItem> list71= new ArrayList<>();
        list71.add(CornerItem.INSECT);
        list71.add(CornerItem.INSECT);
        list71.add(CornerItem.ANIMAL);
        ResourceCard card_71 = new GoldCard(71,"P",1,empty,notVisible,empty,manuscript,empty,empty,empty,empty,list71,0,CornerItem.MANUSCRIPT);

        ArrayList<CornerItem> list72= new ArrayList<>();
        list72.add(CornerItem.INSECT);
        list72.add(CornerItem.INSECT);
        list72.add(CornerItem.FUNGI);
        ResourceCard card_72 = new GoldCard(72,"P",1,notVisible,empty,inkwell,empty,empty,empty,empty,empty,list72,0,CornerItem.QUILL);

        ArrayList<CornerItem> list73 = new ArrayList<>();
        list73.add(CornerItem.INSECT);
        list73.add(CornerItem.INSECT);
        list73.add(CornerItem.INSECT);
        list73.add(CornerItem.ANIMAL);
        ResourceCard card_73 = new GoldCard(73,"P",2,empty,empty,empty,notVisible,empty,empty,empty,empty,list73,1,CornerItem.EMPTY);

        ArrayList<CornerItem> list74 = new ArrayList<>();
        list74.add(CornerItem.INSECT);
        list74.add(CornerItem.INSECT);
        list74.add(CornerItem.INSECT);
        list74.add(CornerItem.PLANT);
        ResourceCard card_74 = new GoldCard(74,"P",2,empty,empty,notVisible,empty,empty,empty,empty,empty,list74,1,CornerItem.EMPTY);

        ArrayList<CornerItem> list75 = new ArrayList<>();
        list75.add(CornerItem.INSECT);
        list75.add(CornerItem.INSECT);
        list75.add(CornerItem.FUNGI);
        ResourceCard card_75 = new GoldCard(75,"P",2,empty,notVisible,empty,empty,empty,empty,empty,empty,list75,1,CornerItem.EMPTY);

        ArrayList<CornerItem> list76 = new ArrayList<>();
        list76.add(CornerItem.INSECT);
        list76.add(CornerItem.INSECT);
        list76.add(CornerItem.INSECT);
        ResourceCard card_76 = new GoldCard(76,"P",3,inkwell,notVisible,notVisible,empty,empty,empty,empty,empty,list76,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list77 = new ArrayList<>();
        list77.add(CornerItem.INSECT);
        list77.add(CornerItem.INSECT);
        list77.add(CornerItem.INSECT);
        ResourceCard card_77 = new GoldCard(77,"P",3,empty,manuscript,notVisible,notVisible,empty,empty,empty,empty,list77,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list78 = new ArrayList<>();
        list78.add(CornerItem.INSECT);
        list78.add(CornerItem.INSECT);
        list78.add(CornerItem.INSECT);
        ResourceCard card_78 = new GoldCard(78,"P",3,notVisible,notVisible,empty,quill,empty,empty,empty,empty,list78,2,CornerItem.EMPTY);

        ArrayList<CornerItem> list79 = new ArrayList<>();
        list79.add(CornerItem.INSECT);
        list79.add(CornerItem.INSECT);
        list79.add(CornerItem.INSECT);
        list79.add(CornerItem.INSECT);
        list79.add(CornerItem.INSECT);

        ResourceCard card_79 = new GoldCard(79,"P",5,empty,empty,notVisible,notVisible,empty,empty,empty,empty,list79,2,CornerItem.EMPTY);

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
    public ResourceCard drawCard() {
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

    public void setEmpty(){
        cards.clear();
    }

    public ArrayList<ResourceCard> getCards(){
        return cards;
    }
}