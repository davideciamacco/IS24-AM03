package it.polimi.ingsw.is24am03.server.model.exceptions;
/**
 * Exception thrown when a player tries to place a card specifying a side different from 'FRONT' or 'BACK'.
 * Exception thrown when a player tries to place a card specifying a card number minor that 1 and greater than 3.
 */
public class ArgumentException extends Exception{
    /**
     * Constructor of a ArgumentException
     * @param m details of the exception
     */
    public ArgumentException(String m){super(m);}
}
