package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.enums.Color;

import java.io.Serial;
import java.util.ArrayList;

public class AvailableColorMessage extends Message{

    @Serial

    private final static long serialVersionUID= -7673225354427727767L;

    private ArrayList<Color> colors;



    public AvailableColorMessage(ArrayList<Color> colors){
        super(MessageType.AVAILABLE_COLORS);
        this.colors=colors;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

}
