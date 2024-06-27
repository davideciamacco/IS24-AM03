package it.polimi.ingsw.is24am03.server.model.exceptions;
/**
 * Exception thrown when a player tries to draw a card from an empty deck
 */
public class EmptyDeckException extends Exception{
    /**
     * Constructor of a EmptyDeckException
     * @param message details of the exception
     */
    public EmptyDeckException(String message) {
        super(message);
    }
}
