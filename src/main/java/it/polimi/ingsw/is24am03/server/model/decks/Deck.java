package it.polimi.ingsw.is24am03.server.model.decks;

import it.polimi.ingsw.is24am03.server.model.cards.Card;

/**
 *Class employed to manage the decks of the game, which are GoldDeck, ResourceDeck,
 *ObjectiveDeck and StartingDeck.
 */
public abstract class Deck {
    /**
     * This method allows us to shuffle a deck of cards.
     */
    public abstract void shuffle();

    /**
     * This method allows drawing a card from one of the decks.
     * @return the card at the top of the deck.
     */
    public abstract Card drawCard();

    /**
     * This method checks whether the deck is empty.
     * @return true is the deck is empty,otherwise false.
     */
    public abstract boolean isEmpty();


}
