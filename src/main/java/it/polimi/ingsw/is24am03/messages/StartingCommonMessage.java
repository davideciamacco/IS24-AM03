package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;

import java.io.Serial;
import java.util.ArrayList;

public class StartingCommonMessage extends Message{
    @Serial
    private final static long serialVersionUID= 1213842072151949402L;
    private ArrayList<ResourceCard> commons;
    public StartingCommonMessage(ArrayList<ResourceCard> commons){
        super(MessageType.FIRST_COMMON);
        this.commons=commons;
    }
    public ArrayList<ResourceCard> getCommons() {
        return commons;
    }


}
