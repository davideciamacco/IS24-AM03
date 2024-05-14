package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;

import java.util.ArrayList;
import java.util.Map;

public class UpdateCrashedPlayerMessage extends Message{

    //gli do tutto quello che ho in un client model

    //stringa del giocatore da notificare//
    private String nickname;

    //chat//
    private ArrayList<Text> chat;

    //stato del gioco//
    private State gameState;

    //carte personali del giocatore//
    private ArrayList<ResourceCard> hand;

    //carta obiettivo segreta//
    private ObjectiveCard objectiveCard;

    //Tutte le board dei giocatori, compresa la sua//
    private Map<String, PlayableCard[][]> boards;

    //Il punteggio di tutti i giocatori, compreso il suo//
    private Map<String, Integer> points;


    //Una lista dei giocatori, che comprende anche lui stesso
    private ArrayList<String> players;


    private ArrayList<ObjectiveCard> objectiveCards;

    private Map<String, Color> colors;


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

    public Map<String, Color> getColors() {
        return colors;
    }

    public ArrayList<ResourceCard> getTable() {
        return table;
    }

    public UpdateCrashedPlayerMessage(String nickname, ArrayList<Text> chat, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Map<String, Color> colors, ArrayList<ResourceCard> table) {
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
        this.colors = colors;
        this.table = table;
    }


    private ArrayList<ResourceCard> table;


    //lista delle carte comuni del gioco

    //0--> resource deck
    //1--> gold deck
    //2--> carta in posizione 0
    //3--> carta in posizione 1
    //4--> carta in posizione 2
    //5--> carta in posizione 3


}
