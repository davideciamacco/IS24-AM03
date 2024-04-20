package it.polimi.ingsw.is24am03;

public class CoordinatesOutOfBoundsException extends IllegalArgumentException {
    public CoordinatesOutOfBoundsException() {
        super("Coordinates out of board limits");
    }
}
