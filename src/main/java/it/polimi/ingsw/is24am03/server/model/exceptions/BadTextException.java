package it.polimi.ingsw.is24am03.server.model.exceptions;
/**
 * Exception thrown when a player doesn't specify the receiver in a private text, or he tries to send an empty text
 */
public class BadTextException extends Exception{
    /**
     * Constructor of a BadTextException
     * @param message details of the exception
     */
    public BadTextException(String message){ super(message);}
}
