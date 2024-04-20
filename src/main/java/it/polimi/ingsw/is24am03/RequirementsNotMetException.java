package it.polimi.ingsw.is24am03;

public class RequirementsNotMetException extends IllegalArgumentException {
    public RequirementsNotMetException() {
        super("Requirements not met");
    }
}
