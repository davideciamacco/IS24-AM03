package it.polimi.ingsw.is24am03.server.model.exceptions;
/**
 * Exception thrown when a player tries to send a private message to a receiver that isn't part of the game
 */
public class PlayerAbsentException extends Exception{
    /**
     * Constructor of a PlayerAbsentException
     * @param message details of the exception
     */
    public PlayerAbsentException(String message){
        super(message);
    }
}
