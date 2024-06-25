package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;

import java.io.Serial;
import java.util.ArrayList;

public class PersonalCardsMessage extends Message{
    @Serial
    private final static long serialVersionUID= 2076020660851296694L;

    private String player;
    private ArrayList<ResourceCard> hand;


    public PersonalCardsMessage(String player, ArrayList<ResourceCard> hand){
        super(MessageType.UPDATE_PERSONAL_CARDS);
        this.player=player;
        this.hand=hand;
    }
    public String getPlayer() {
        return player;
    }

    public ArrayList<ResourceCard> getHand() {
        return hand;
    }

}
