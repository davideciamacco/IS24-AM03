package it.polimi.ingsw.is24am03.Subscribers;

import it.polimi.ingsw.is24am03.server.model.cards.*;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 * A GameSub is a class which observes a Game class in order to obtain updates from it.
 */
public interface GameSub extends Subscriber {

    /**
     * Notifies a certain sub that a player joined the game.
     * @param player nickname of the player who joined the game
     * @throws RemoteException RMI Exception
     */
    void notifyJoinedPlayer(String player) throws RemoteException;

    /**
     * Notifies a certain sub of the winners of the game
     * @param winners list of winners
     * @throws RemoteException RMI Exception
     */
    void notifyWinners(ArrayList<String> winners) throws RemoteException;

    /**
     * Notifies a certain sub of the turn order in the game.
     * @param order turn order of the game
     * @throws RemoteException RMI Exception
     */
    void notifyTurnOrder(ArrayList<String> order) throws RemoteException;

    /**
     * Notifies a certain sub of the current player.
     * @param current nickname of the current player
     * @throws RemoteException RMI Exception
     */
    void notifyCurrentPlayer(String current) throws RemoteException;

    /**
     * Notifies a certain sub that a player has crashed during the game
     * @param crashedPlayer nickname of the player who crashed
     * @throws RemoteException RMI Exception
     */
    void notifyCrashedPlayer(String crashedPlayer) throws RemoteException;

    /**
     * Notifies a certain sub of the current game's state
     * @param gameState new state of the game
     * @throws RemoteException RMI Exception
     */
    void notifyChangeState(State gameState) throws RemoteException;

    /**
     * Notifies a certain sub that a player rejoined the game after crashing.
     * @param rejoinedPlayer the nickname of the rejoined player
     * @throws RemoteException RMI Exception
     */
    void notifyRejoinedPlayer(String rejoinedPlayer) throws RemoteException;

    /**
     * Provides the nickname associated to the subscriber
     * @return the nickname of the player subscribed to a certain PlayerBoard class
     * @throws RemoteException RMI Exception
     */
    String getSub() throws RemoteException;

    /**
     * Notifies a certain sub of the common objective cards
     * @param objectiveCard1 refers to the first objective card
     * @param objectiveCard2 refers to the second objective card
     * @throws RemoteException RMI Exception
     */
    void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2) throws RemoteException;

    /**
     *Notifies a certain sub that the common table is updated after a player drew a card from it
     * @param resourceCard refers to the new card in the common table
     * @param index identifies which deck has been updated, 0 refers to the Resource Deck, 1 to the Gold Deck and indexes from 2 to 5 refer to the four other cards
     * @throws RemoteException RMI Exception
     */
    void updateCommonTable(ResourceCard resourceCard, int index) throws RemoteException;

    /**
     * Notifies a certain sub in the lobby that the number of players necessary to start the game has been reached
     * @throws RemoteException RMI Exception
     */
    void NotifyNumbersOfPlayersReached() throws RemoteException;

    /**
     * Notifies a certain sub of the starting of the last round, during which drawing from the table won't be allowed
     * @throws RemoteException RMI Exception
     */
    void NotifyLastRound()throws RemoteException;

    /**
     * Notifies a certain sub of the colors chose by the players
     * @param colors each player is mapped to this chosen color
     * @throws RemoteException RMI Exception
     */
    void notifyFinalColors(Map<String, Color> colors) throws RemoteException;

    /**
     * Notifies a sub who have rejoined the game after crashing of the game's changes occurred during his absence
     * @param current the current player
     * @param chat contains all text messages from the group chat and the texts in which the rejoined player was the recipient or the sender
     * @param gameState the current state of the game
     * @param hand contains the personal cards of the player
     * @param objectiveCard the secret objective card chosed by the player
     * @param boards maps each player to his board of placed cards
     * @param points contains the update score of each player
     * @param players contains the turn order of the game
     * @param objectiveCards contains the common objective cards
     * @param color color chosen by the rejoined player
     * @param table contains the cards placed on the common table that can be drawn by a player
     * @param colors contains the colors chosen by the players, ordered by the game's turn order
     * @throws RemoteException RMI Exception
     */
    void UpdateCrashedPlayer(String current, ArrayList<Text> chat, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table, ArrayList<Color> colors) throws RemoteException;

    /**
     *  Notifies a sub at the beginning of the game of the common cards on the table that can be drawn.
     * @param commons contains the common cards, first two indexes refer to the resource and gold deck
     * @throws RemoteException RMI Exception
     */
    void UpdateFirst(ArrayList<ResourceCard> commons) throws RemoteException;

}
