package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.enums.Color;

import java.io.Serial;
import java.util.ArrayList;


/**
 * The AvailableColorMessage class represents a message used to communicate the available colors which can be selected by a player.
*/
public class AvailableColorMessage extends Message{

    @Serial

    private final static long serialVersionUID= -7673225354427727767L;

    private ArrayList<Color> colors;


    /**
     * Constructor of a AvailableColorMessage
     * @param colors list of the available colors
     */
    public AvailableColorMessage(ArrayList<Color> colors){
        super(MessageType.AVAILABLE_COLORS);
        this.colors=colors;
    }

    /**
     *
     * @return the list of the available colors
     */
    public ArrayList<Color> getColors() {
        return colors;
    }

}
