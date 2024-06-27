package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.enums.Color;

import java.io.Serial;
import java.util.Map;

/**
 * This message is used to update all the players about the color of each other
 */
public class FinalColorsMessage extends Message{

    @Serial
    private final static long serialVersionUID= 4272961920308559455L;
    private Map<String, Color> colors;

    /**
     * Constructor of a FinalColorMessage
     * @param colors map associating each player's nickname with their color
     */
    public FinalColorsMessage(Map<String,Color> colors){
        super(MessageType.FINAL_COLORS);
        this.colors=colors;
    }

    /**
     *
     * @return the map associating each player's nickname with their color
     */
    public Map<String, Color> getColors() {
        return colors;
    }

}
