package it.polimi.ingsw.is24am03.server.model.cards;

import java.io.Serializable;

/**
 * Abstract class representing a generic Card with an ID and point value.
 * This class implements Serializable to allow card objects to be serialized.
 * Specific types of cards should extend this class.
 */
public abstract class Card implements Serializable {
    private int points;
    private int id;

    /**
     * Constructs a Card with the specified ID and point value.
     *
     * @param id the unique identifier of the card
     * @param points the point value of the card
     */
    public Card(int id, int points) {
        this.id = id;
        this.points = points;
    }

    /**
     * Returns the point value of the card.
     *
     * @return the points of the card
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns the unique identifier of the card.
     *
     * @return the ID of the card
     */
    public int getId() {
        return id;
    }
}
