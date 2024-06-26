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
    public Color getColor() {
        return color;
    }
    public String getNickname() {
        return nickname;
    }
    public ArrayList<Text> getChat() {
        return chat;
    }
    public State getGameState() {
        return gameState;
    }
    public ArrayList<ResourceCard> getHand() {
        return hand;
    }
    public ObjectiveCard getObjectiveCard() {
        return objectiveCard;
    }
    public Map<String, PlayableCard[][]> getBoards() {
        return boards;
    }
    public Map<String, Integer> getPoints() {
        return points;
    }
    public ArrayList<String> getPlayers() {
        return players;
    }
    public ArrayList<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }
    public ArrayList<Color> getColors() {
        return colors;
    }
    public ArrayList<ResourceCard> getTable() {
        return table;
    }

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
