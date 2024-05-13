package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;

import java.io.Serial;

public class PlayerBoardMessage extends Message{

    @Serial
    private static final long serialVersionUID= 7441040663307013718L;

    public String getPlayer() {
        return player;
    }

    public PlayableCard getPlayableCard() {
        return playableCard;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    private String player;
    private PlayableCard playableCard;
    private int i;
    private int j;

    public PlayerBoardMessage(String player, PlayableCard playableCard, int i, int j) {
        super(MessageType.UPDATE_PLAYER_BOARD);
        this.player = player;
        this.playableCard = playableCard;
        this.i = i;
        this.j = j;
    }
}
