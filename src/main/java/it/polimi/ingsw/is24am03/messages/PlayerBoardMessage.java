package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;

import java.io.Serial;

/**
 * This message represents the notification to all players connected in game that a player has placed a card
 */
public class PlayerBoardMessage extends Message{

    @Serial
    private static final long serialVersionUID= 7441040663307013718L;
    private String player;
    private PlayableCard playableCard;
    private int i;
    private int j;

    /**
     * Constructor of a PlayerBoardMessage
     * @param player nickname of the player who has placed a card
     * @param playableCard card place by the player
     * @param i the row index of the position in the matrix where the card has been placed
     * @param j the column index of the position in the matrix where the card has been placed
     */
    public PlayerBoardMessage(String player, PlayableCard playableCard, int i, int j) {
        super(MessageType.UPDATE_PLAYER_BOARD);
        this.player = player;
        this.playableCard = playableCard;
        this.i = i;
        this.j = j;
    }

    /**
     *
     * @return the nickname of the player who has placed a card
     */
    public String getPlayer() {
        return player;
    }

    /**
     *
     * @return the card place by the player
     */
    public PlayableCard getPlayableCard() {
        return playableCard;
    }

    /**
     *
     * @return the row index of the position in the matrix where the card has been placed
     */
    public int getI() {
        return i;
    }

    /**
     *
     * @return the column index of the position in the matrix where the card has been placed
     */
    public int getJ() {
        return j;
    }
}
