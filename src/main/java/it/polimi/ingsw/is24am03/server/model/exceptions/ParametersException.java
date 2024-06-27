package it.polimi.ingsw.is24am03.server.model.exceptions;
/**
 * Exception thrown when a player tries to send a message to himself
 */
public class ParametersException extends Exception{
    /**
     * Constructor of a ParametersException
     * @param message details of the exception
     */
    public ParametersException(String message){
        super(message);
    }
}
