package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Map;

/**
 * This message represents a notification to a rejoined player containing all the information of the game when the player rejoined to update his client model
 */
public class UpdateCrashedPlayerMessage extends Message{
    @Serial
    private static final long serialVersionUID= 4639752391662757240L;
    private String nickname;
    private ArrayList<Color> colors;
    private ArrayList<Text> chat;
    private State gameState;
    private ArrayList<ResourceCard> hand;
    private ObjectiveCard objectiveCard;
    private Map<String, PlayableCard[][]> boards;
    private Map<String, Integer> points;
    private ArrayList<String> players;
    private ArrayList<ObjectiveCard> objectiveCards;
    private Color color;
    private ArrayList<ResourceCard> table;

    /**
     *
     * @return the pawn's color of the rejoined player
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return the nickname of the player who has rejoined
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @return an ArrayList containing all chat messages sent during the game
     */
    public ArrayList<Text> getChat() {
        return chat;
    }

    /**
     *
     * @return the game state
     */
    public State getGameState() {
        return gameState;
    }

    /**
     *
     * @return an ArrayList containing the personal cards of the rejoined player
     */
    public ArrayList<ResourceCard> getHand() {
        return hand;
    }

    /**
     *
     * @return the personal objective card of the rejoined player
     */
    public ObjectiveCard getObjectiveCard() {
        return objectiveCard;
    }

    /**
     *
     * @return the Map associating every player's nickname to their personal board
     */
    public Map<String, PlayableCard[][]> getBoards() {
        return boards;
    }

    /**
     *
     * @return the Map associating every player's nickname to their points
     */
    public Map<String, Integer> getPoints() {
        return points;
    }

    /**
     *
     * @return an ArrayList containing all the players' nicknames ordered according to their turn order
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     *
     * @return the two common objective cards
     */
    public ArrayList<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }

    /**
     *
     * @return an ArrayList containing all player's color ordered according to their turn order
     */
    public ArrayList<Color> getColors() {
        return colors;
    }

    /**
     *
     * @return an Arraylist containing the six cards that can be drawn from the game table
     */
    public ArrayList<ResourceCard> getTable() {
        return table;
    }

    /**
     * Constructor of a UpdateCrashedPlayerMessage
     * @param nickname nickname of the player who has rejoined
     * @param chat ArrayList containing all chat messages sent during the game
     * @param gameState game state
     * @param hand ArrayList containing the personal cards of the rejoined player
     * @param objectiveCard personal objective card of the rejoined player
     * @param boards Map associating every player's nickname to their personal board
     * @param points Map associating every player's nickname to their points
     * @param players ArrayList containing all the players' nicknames ordered according to their turn order
     * @param objectiveCards common objective cards
     * @param color pawn's color of the rejoined player
     * @param table Arraylist containing the six cards that can be drawn from the game table
     * @param colors ArrayList containing all pawn's color ordered according to the players' turn order
     */
    public UpdateCrashedPlayerMessage(String nickname, ArrayList<Text> chat, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color  color, ArrayList<ResourceCard> table, ArrayList<Color> colors) {
        super(MessageType.UPDATE_CRASHED_PLAYER);
        this.nickname = nickname;
        this.chat = chat;
        this.gameState = gameState;
        this.hand = hand;
        this.objectiveCard = objectiveCard;
        this.boards = boards;
        this.points = points;
        this.players = players;
        this.objectiveCards = objectiveCards;
        this.color = color;
        this.table = table;
        this.colors = colors;
    }

}
