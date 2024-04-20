package it.polimi.ingsw.is24am03;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class employed to manage the deck which contains all Starting cards.
 */
public class StartingDeck extends Deck{

    /**
     * This field contains a list of all Starting Cards
     */
    private final ArrayList<StartingCard> cards;

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

        ArrayList<CornerItem> list = new ArrayList<>();

        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();

        list.add(CornerItem.FUNGI);
        StartingCard card_81= new StartingCard(81,0,plant,animal,insect,fungi,animal,empty,fungi,empty,list);
        list.clear();

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.PLANT);
        StartingCard card_82= new StartingCard(82,0,insect,animal,plant,fungi,empty,empty,empty,empty,list);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.INSECT);
        StartingCard card_83= new StartingCard(83,0,plant,insect,fungi,animal,empty,empty,empty,empty,list);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.INSECT);
        list.add(CornerItem.PLANT);
        StartingCard card_84= new StartingCard(84,0,insect,fungi,animal,plant,empty,empty,empty,empty,list);
        list.clear();

        list.add(CornerItem.ANIMAL);
        list.add(CornerItem.PLANT);
        list.add(CornerItem.FUNGI);
        StartingCard card_85= new StartingCard(85,0,fungi,animal,insect,plant,empty,empty,empty,empty,list);
        list.clear();

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
        Collections.shuffle(this.cards, new Random());
    }
    //il controller può mandare mex al client (solitamente  si fa così),
    //si usano i componenti virtual view, registrsti come osservatori,
    // patterno observer che s i reg al mod e ricevono notifiche di cambiamento di stato, attraverso messaggi
    //gli osservatori trasforma il cmbiamnto di modello in messaggi (inviano matrice o carta) che viaggiano attraverso la rete
    //serial dei mex
    //sequence diagram uml, diagramma fasi importanti di gioco --> protocollo di rete
    //non mandare tutto lo stato del gioco ma solo la differenza !!

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

}
