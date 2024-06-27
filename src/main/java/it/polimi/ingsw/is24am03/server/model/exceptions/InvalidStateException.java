package it.polimi.ingsw.is24am03.server.model.exceptions;
/**
 * Exception thrown when a player tries to make a move during a game state that doesn't allow it
 */
public class InvalidStateException extends Exception{
    /**
     * Constructor of a InvalidStateException
     * @param message details of the exception
     */
    public InvalidStateException(String message)
    {
        super(message);
    }
}
