package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.enums.Color;

import java.io.Serial;
import java.util.Map;

public class FinalColorsMessage extends Message{

    @Serial
    private final static long serialVersionUID= 4272961920308559455L;

    public Map<String, Color> getColors() {
        return colors;
    }

    private Map<String, Color> colors;

    public FinalColorsMessage(Map<String,Color> colors){
        super(MessageType.FINAL_COLORS);
        this.colors=colors;
    }

}
