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
 * ViewInterface defines methods for updating the client-side view
 * in response to game events. This includes drawing various game elements,
 * displaying notifications, and handling chat messages.
 */
public interface ViewInterface {

    /**
     * Draws the specified scene.
     *
     * @param sceneType the type of scene to draw.
     */
    void drawScene(SceneType sceneType);

    /**
     * Draws the game board with the provided playable cards.
     *
     * @param playableCards a 2D array representing the playable cards on the board.
     */
    void drawBoard(PlayableCard[][] playableCards);

    /**
     * Draws the player's hand with the provided resource cards.
     *
     * @param hand an ArrayList of ResourceCard representing the player's hand.
     */
    void drawHand(ArrayList<ResourceCard> hand);

    /**
     * Draws the objective card.
     *
     * @param o the ObjectiveCard to draw.
     */
    void drawObjective(ObjectiveCard o);

    /**
     * Draws the starting card.
     *
     * @param startingCard the StartingCard to draw.
     */
    void drawStarting(StartingCard startingCard);

    /**
     * Draws the game table with the provided player points and resource cards.
     *
     * @param playerPoints a map of player names to their points.
     * @param resourceDeck the top card of the resource deck.
     * @param goldDeck the top card of the gold deck.
     * @param card0 the first card on the table.
     * @param card1 the second card on the table.
     * @param card2 the third card on the table.
     * @param card3 the fourth card on the table.
     */
    void drawTable(Map<String, Integer> playerPoints, ResourceCard resourceDeck, ResourceCard goldDeck, ResourceCard card0, ResourceCard card1, ResourceCard card2, ResourceCard card3);

    /**
     * Draws the available colors for player selection.
     *
     * @param colors an ArrayList of available colors.
     */
    void drawAvailableColors(ArrayList<Color> colors);

    /**
     * Notifies that a player has joined the game.
     *
     * @param joinedPlayer the nickname of the joined player.
     */
    public void notifyJoinedPlayer(String joinedPlayer);

    /**
     * Notifies the winners of the game.
     *
     * @param winners an ArrayList of the winners' nicknames.
     */
    public void notifyWinners(ArrayList<String> winners);

    /**
     * Notifies the turn order of the players.
     *
     * @param order an ArrayList of the players' nicknames in turn order.
     * @param player the nickname of the current player.
     */
    public void notifyTurnOrder(ArrayList<String> order, String player);

    /**
     * Notifies the current player and updates the game state.
     *
     * @param current the nickname of the current player.
     * @param boards a map of player names to their board states.
     * @param player the nickname of the player.
     * @param hand an ArrayList of the player's hand cards.
     * @param gamestate the current game state.
     */
    public void notifyCurrentPlayer(String current, Map<String, PlayableCard[][]> boards, String player, ArrayList<ResourceCard> hand, State gamestate);

    /**
     * Notifies that a player has crashed.
     *
     * @param username the nickname of the crashed player.
     */
    public void notifyCrashedPlayer(String username);

    /**
     * Notifies a change in the game state.
     *
     * @param gameState the new game state.
     */
    public void notifyChangeState(State gameState);

    /**
     * Notifies that a player has rejoined the game.
     *
     * @param rejoinedPlayer the nickname of the rejoined player.
     */
    public void notifyRejoinedPlayer(String rejoinedPlayer);

    /**
     * Notifies a change in a player's board state.
     *
     * @param player the nickname of the player whose board changed.
     * @param nickname the nickname of the player.
     * @param boards a map of player names to their board states.
     */
    public void notifyChangePlayerBoard(String player, String nickname, Map<String, PlayableCard[][]> boards);

    /**
     * Receives an update on a player's points.
     *
     * @param player the nickname of the player.
     * @param points the updated points of the player.
     */
    public void ReceiveUpdateOnPoints(String player, int points);

    /**
     * Notifies a change in the player's personal cards.
     *
     * @param p an ArrayList of the player's personal cards.
     */
    public void NotifyChangePersonalCards(ArrayList<ResourceCard> p);

    /**
     * Notifies the choice of an objective card.
     *
     * @param o the chosen ObjectiveCard.
     */
    public void notifyChoiceObjective(ObjectiveCard o);

    /**
     * Notifies the player's first hand.
     *
     * @param hand an ArrayList of ResourceCard representing the player's hand.
     * @param startingCard the player's StartingCard.
     * @param o1 the first ObjectiveCard.
     * @param o2 the second ObjectiveCard.
     */
    public void notifyFirstHand(ArrayList<ResourceCard> hand, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2);

    /**
     * Notifies the common objective cards.
     *
     * @param objectiveCard1 the first common ObjectiveCard.
     * @param objectiveCard2 the second common ObjectiveCard.
     */
    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2);

    /**
     * Updates the common table with player points and resource cards.
     *
     * @param points a map of player names to their points.
     * @param topResDeck the top card of the resource deck.
     * @param topGoldDeck the top card of the gold deck.
     * @param tableCard1 the first card on the table.
     * @param tableCard2 the second card on the table.
     * @param tableCard3 the third card on the table.
     * @param tableCard4 the fourth card on the table.
     */
    public void updateCommonTable(Map<String, Integer> points, ResourceCard topResDeck, ResourceCard topGoldDeck, ResourceCard tableCard1, ResourceCard tableCard2, ResourceCard tableCard3, ResourceCard tableCard4);

    /**
     * Notifies that the required number of players has been reached.
     */
    public void NotifyNumbersOfPlayersReached();

    /**
     * Notifies that the last round has started.
     */
    public void NotifyLastRound();

    /**
     * Notifies the available colors for player selection.
     *
     * @param colors an ArrayList of available colors.
     */
    public void notifyAvailableColors(ArrayList<Color> colors);

    /**
     * Notifies the final colors chosen by the players.
     *
     * @param colors a map of player names to their chosen colors.
     * @param players an ArrayList of the players' nicknames.
     */
    public void notifyFinalColors(Map<String, Color> colors, ArrayList<String> players);

    /**
     * Draws the final colors chosen by the players.
     *
     * @param colors a map of player names to their chosen colors.
     * @param players an ArrayList of the players' nicknames.
     */
    public void drawFinalColors(Map<String, Color> colors, ArrayList<String> players);

    /**
     * Updates the state of a crashed player.
     *
     * @param nickname the nickname of the crashed player.
     * @param player the nickname of the current player.
     * @param gameState the current game state.
     * @param hand an ArrayList of the player's hand cards.
     * @param objectiveCard the player's objective card.
     * @param boards a map of player names to their board states.
     * @param points a map of player names to their points.
     * @param players an ArrayList of the players' nicknames.
     * @param objectiveCards an ArrayList of the player's objective cards.
     * @param color the player's color.
     * @param table an ArrayList of the resource cards on the table.
     */
    public void UpdateCrashedPlayer(String nickname, String player, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table);

    /**
     * Updates the initial state of the game.
     *
     * @param points a map of player names to their points.
     * @param commons an ArrayList of common resource cards.
     */
    public void UpdateFirst(Map<String, Integer> points, ArrayList<ResourceCard> commons);

    /**
     * Adds a group chat message.
     *
     * @param chat an ArrayList of Text messages in the chat.
     * @param player the nickname of the player sending the message.
     */
    public void addGroupText(ArrayList<Text> chat, String player);

    /**
     * Draws the chat with the provided messages.
     *
     * @param chat an ArrayList of Text messages in the chat.
     * @param player the nickname of the player viewing the chat.
     */
    public void drawChat(ArrayList<Text> chat, String player);

    /**
     * Draws an error message.
     *
     * @param message the error message to draw.
     */
    public void drawError(String message);

    /**
     * Restores the chat with the provided messages.
     *
     * @param chat an ArrayList of Text messages in the chat.
     * @param player the nickname of the player viewing the chat.
     */
    public void restoreChat(ArrayList<Text> chat, String player);

    /**
     * Confirms that a player has joined the game.
     */
    public void confirmJoin();

    /**
     * Confirms that a game has been created.
     */
    public void confirmCreate();

    /**
     * Prints notifications with the provided message.
     *
     * @param message the message to print.
     */
    public void printNotifications(String message);

    /**
     * Prints the updated points of players.
     *
     * @param updatedPoints a map of player names to their updated points.
     */
    public void printUpdatedPoints(Map<String, Integer> updatedPoints);
}
