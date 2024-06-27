package it.polimi.ingsw.is24am03.server.model.exceptions;

/**
 * Exception thrown if a user attempts to make a move but has never been in the game
 */
public class UnknownPlayerException extends Exception{

    /**
     * Constructor of a UnknownPlayerException
     * @param message details of the exception
     */
    public UnknownPlayerException(String message){
        super(message);
    }
}
