package it.polimi.ingsw.is24am03;

public class PositionOccupiedException extends IllegalArgumentException {
    public PositionOccupiedException() {
        super("Position already occupied/unavailable");
    }
}
