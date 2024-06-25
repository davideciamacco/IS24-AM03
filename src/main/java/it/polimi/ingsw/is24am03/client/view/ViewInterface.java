package it.polimi.ingsw.is24am03.client.view;

import it.polimi.ingsw.is24am03.SceneType;
import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 */
public interface ViewInterface {

    /**
     *
     * @param sceneType
     */
    void drawScene(SceneType sceneType);
    //void notify(String s);

    /**
     *
     * @param playableCards
     */
    void drawBoard(PlayableCard[][] playableCards);

    /**
     *
     * @param hand
     */
    void drawHand(ArrayList<ResourceCard> hand);

    /**
     *
     * @param o
     */
    void drawObjective(ObjectiveCard o);

    /**
     *
     * @param startingCard
     */
    void drawStarting(StartingCard startingCard);
    void drawTable(Map<String, Integer> playerPoints, ResourceCard resourceDeck, ResourceCard goldDeck, ResourceCard card0, ResourceCard card1, ResourceCard card2, ResourceCard card3);
    void drawAvailableColors(ArrayList<Color> colors);
    public  void  notifyJoinedPlayer(String joinedPlayer);
    public void notifyWinners(ArrayList<String> winners);
    public void  notifyTurnOrder(ArrayList<String> order, String player);
    public void notifyCurrentPlayer(String current, Map<String,PlayableCard[][]> boards, String player, ArrayList<ResourceCard> hand, State gamestate);
    public void notifyCrashedPlayer(String username);
    public void notifyChangeState(State gameState);
    public void notifyRejoinedPlayer(String rejoinedPlayer);
    public void notifyChangePlayerBoard(String player, String nickname, Map<String,PlayableCard[][]>boards);
    public void ReceiveUpdateOnPoints(String player, int points);
    public  void NotifyChangePersonalCards(ArrayList<ResourceCard> p);
    public void notifyChoiceObjective(ObjectiveCard o);
    public void notifyFirstHand(ArrayList<ResourceCard> hand, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2);
    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2);
    public void updateCommonTable(Map<String, Integer> points, ResourceCard topResDeck, ResourceCard topGoldDeck, ResourceCard tableCard1, ResourceCard tableCard2, ResourceCard tableCard3, ResourceCard tableCard4);
    public void NotifyNumbersOfPlayersReached();
    public void NotifyLastRound();
    public void notifyAvailableColors(ArrayList<Color> colors);
    public void notifyFinalColors(Map<String, Color> colors, ArrayList<String> players);
    public void drawFinalColors(Map<String,Color> colors, ArrayList<String> players);
    public void UpdateCrashedPlayer(String nickname, String player, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table);
    public void UpdateFirst(Map<String,Integer> points, ArrayList<ResourceCard> commons);
    public void addGroupText(ArrayList<Text> chat, String player);
    public void drawChat(ArrayList<Text> chat, String player);
    public void drawError(String message);
    public void restoreChat(ArrayList<Text> chat, String player);
    public void confirmJoin();
    public void confirmCreate();
    public void printNotifications(String message);
}
