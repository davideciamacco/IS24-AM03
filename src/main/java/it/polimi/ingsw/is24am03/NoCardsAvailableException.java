package it.polimi.ingsw.is24am03;

public class NoCardsAvailableException extends IllegalArgumentException {

    public NoCardsAvailableException() {
        super("Position already occupied/unavailable");
    }
}
