package it.polimi.ingsw.is24am03.server.model.game;

import it.polimi.ingsw.is24am03.Subscribers.*;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RemoteGameController interface provides methods to manage a remote game session.
 * It allows clients to create and join games, draw resources, place cards, communicate
 * through chat, and manage game observers.
 * This interface extends the Remote interface for RMI communication.
 */
public interface RemoteGameController extends Remote {

    /**
     * Creates a new game session.
     *
     * @param numPlayers the number of players for the game.
     * @param nickname the nickname of the player creating the game.
     * @param ConnectionType the type of connection (e.g., RMI, Socket).
     * @throws RemoteException if a remote communication error occurs.
     * @throws GameAlreadyCreatedException if a game is already created.
     */
    public void createGame(int numPlayers, String nickname, String ConnectionType) throws RemoteException, GameAlreadyCreatedException;

    /**
     * Allows a player to draw resources.
     *
     * @param player the nickname of the player.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void drawResources(String player) throws RemoteException;

    /**
     * Allows a player to draw gold.
     *
     * @param player the nickname of the player.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void drawGold(String player) throws RemoteException;

    /**
     * Allows a player to draw a card from the table.
     *
     * @param player the nickname of the player.
     * @param choice the card choice index.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void drawTable(String player, int choice) throws RemoteException;

    /**
     * Adds a player to the game.
     *
     * @param player the nickname of the player.
     * @param ConnectionType the type of connection (e.g., RMI, Socket).
     * @throws RemoteException if a remote communication error occurs.
     * @throws FullLobbyException if the lobby is full.
     * @throws NicknameAlreadyUsedException if the nickname is already used.
     * @throws GameNotExistingException if the game does not exist.
     */
    public void addPlayer(String player, String ConnectionType) throws FullLobbyException, NicknameAlreadyUsedException, RemoteException, GameNotExistingException;

    /**
     * Places a card on the board.
     *
     * @param player the nickname of the player.
     * @param choice the card choice index.
     * @param i the row index on the board.
     * @param j the column index on the board.
     * @param face the face of the card.
     * @throws RemoteException if a remote communication error occurs.
     * @throws UnknownPlayerException if the player is unknown.
     * @throws PlayerNotInTurnException if the player is not in turn.
     * @throws InvalidStateException if the game state is invalid.
     * @throws GameNotExistingException if the game does not exist.
     * @throws CoordinatesOutOfBoundsException if the coordinates are out of bounds.
     * @throws NoCardsAvailableException if there are no cards available.
     * @throws RequirementsNotMetException if the requirements are not met.
     * @throws ArgumentException if there is an argument error.
     */
    public void placeCard(String player, int choice, int i, int j, String face) throws UnknownPlayerException, PlayerNotInTurnException, InvalidStateException, RemoteException, GameNotExistingException, CoordinatesOutOfBoundsException, NoCardsAvailableException, RequirementsNotMetException, ArgumentException;

    /**
     * Sets an objective card for the player.
     *
     * @param player the nickname of the player.
     * @param choice the objective card choice index.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void setObjectiveCard(String player, int choice) throws RemoteException;

    /**
     * Rejoins a player to the game as the chief.
     *
     * @param player the nickname of the player.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void rejoinedChief(String player) throws RemoteException;

    /**
     * Allows a player to select a starting face.
     *
     * @param player the nickname of the player.
     * @param face the face of the player.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void selectStartingFace(String player, String face) throws RemoteException;

    /**
     * Checks if a player can select a starting face.
     *
     * @param player the nickname of the player.
     * @param face the face of the player.
     * @throws RemoteException if a remote communication error occurs.
     * @throws PlayerNotInTurnException if the player is not in turn.
     * @throws InvalidStateException if the game state is invalid.
     * @throws GameNotExistingException if the game does not exist.
     * @throws ArgumentException if there is an argument error.
     * @throws UnknownPlayerException if the player is unknown.
     */
    public void canSelectStartingFace(String player, String face) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, RemoteException, ArgumentException, UnknownPlayerException;

    /**
     * Allows a player to pick a color.
     *
     * @param nickname the nickname of the player.
     * @param color the color chosen by the player.
     * @throws RemoteException if a remote communication error occurs.
     */
    void pickColor(String nickname, String color) throws RemoteException;

    /**
     * Checks if a player can pick a color.
     *
     * @param nickname the nickname of the player.
     * @param color the color chosen by the player.
     * @throws RemoteException if a remote communication error occurs.
     * @throws PlayerNotInTurnException if the player is not in turn.
     * @throws InvalidStateException if the game state is invalid.
     * @throws ColorAlreadyPickedException if the color is already picked.
     * @throws GameNotExistingException if the game does not exist.
     * @throws UnknownPlayerException if the player is unknown.
     * @throws IllegalArgumentException if there is an argument error.
     */
    public void canPickColor(String nickname, String color) throws PlayerNotInTurnException, InvalidStateException, ColorAlreadyPickedException, RemoteException, GameNotExistingException, UnknownPlayerException, IllegalArgumentException;

    /**
     * Sends a private text message.
     *
     * @param nickname the sender's nickname.
     * @param receiver the receiver's nickname.
     * @param text the text message.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void sendPrivateText(String nickname, String receiver, String text) throws RemoteException;

    /**
     * Sends a group text message.
     *
     * @param nickname the sender's nickname.
     * @param text the text message.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void sendGroupText(String nickname, String text) throws RemoteException;

    /**
     * Adds a chat subscriber.
     *
     * @param subscriber the chat subscriber.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void addToObserver(ChatSub subscriber) throws RemoteException;

    /**
     * Adds a player subscriber.
     *
     * @param subscriber the player subscriber.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void addToObserver(PlayerSub subscriber) throws RemoteException;

    /**
     * Adds a player board subscriber.
     *
     * @param subscriber the player board subscriber.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void addToObserver(PlayerBoardSub subscriber) throws RemoteException;

    /**
     * Adds a game subscriber.
     *
     * @param subscriber the game subscriber.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void addToObserver(GameSub subscriber) throws RemoteException;

    /**
     * Removes a chat subscriber.
     *
     * @param subscriber the chat subscriber.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void removeSub(ChatSub subscriber) throws RemoteException;

    /**
     * Removes a player subscriber.
     *
     * @param subscriber the player subscriber.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void removeSub(PlayerSub subscriber) throws RemoteException;

    /**
     * Removes a player board subscriber.
     *
     * @param subscriber the player board subscriber.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void removeSub(PlayerBoardSub subscriber) throws RemoteException;

    /**
     * Removes a game subscriber.
     *
     * @param subscriber the game subscriber.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void removeSub(GameSub subscriber) throws RemoteException;

    /**
     * Checks if the game can start.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    public void canStart() throws RemoteException;

    /**
     * Checks if a player can set an objective card.
     *
     * @param player the nickname of the player.
     * @param choice the objective card choice index.
     * @throws RemoteException if a remote communication error occurs.
     * @throws PlayerNotInTurnException if the player is not in turn.
     * @throws GameNotExistingException if the game does not exist.
     * @throws InvalidStateException if the game state is invalid.
     * @throws UnknownPlayerException if the player is unknown.
     */
    public void canSetObjectiveCard(String player, int choice) throws PlayerNotInTurnException, GameNotExistingException, RemoteException, InvalidStateException, UnknownPlayerException;

    /**
     * Checks if a player can draw resources.
     *
     * @param player the nickname of the player.
     * @throws RemoteException if a remote communication error occurs.
     * @throws PlayerNotInTurnException if the player is not in turn.
     * @throws InvalidStateException if the game state is invalid.
     * @throws GameNotExistingException if the game does not exist.
     * @throws EmptyDeckException if the deck is empty.
     * @throws UnknownPlayerException if the player is unknown.
     */
    public void canDrawResources(String player) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, RemoteException, EmptyDeckException, UnknownPlayerException;

    /**
     * Checks if a player can draw gold.
     *
     * @param player the nickname of the player.
     * @throws RemoteException if a remote communication error occurs.
     * @throws PlayerNotInTurnException if the player is not in turn.
     * @throws InvalidStateException if the game state is invalid.
     * @throws GameNotExistingException if the game does not exist.
     * @throws EmptyDeckException if the deck is empty.
     * @throws UnknownPlayerException if the player is unknown.
     */
    public void canDrawGold(String player) throws PlayerNotInTurnException, UnknownPlayerException, InvalidStateException, GameNotExistingException, EmptyDeckException, RemoteException;

    /**
     * Checks if a player can draw a card from the table.
     *
     * @param player the nickname of the player.
     * @param choice the card choice index.
     * @throws RemoteException if a remote communication error occurs.
     * @throws PlayerNotInTurnException if the player is not in turn.
     * @throws InvalidStateException if the game state is invalid.
     * @throws GameNotExistingException if the game does not exist.
     * @throws NullCardSelectedException if the selected card is null.
     * @throws UnknownPlayerException if the player is unknown.
     * @throws IllegalArgumentException if there is an argument error.
     */
    public void canDrawTable(String player, int choice) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, RemoteException, NullCardSelectedException, UnknownPlayerException, IllegalArgumentException;

    /**
     * Checks if a player can send a group chat message.
     *
     * @param sender the nickname of the sender.
     * @param text the text message.
     * @throws RemoteException if a remote communication error occurs.
     * @throws BadTextException if the text is inappropriate.
     * @throws InvalidStateException if the game state is invalid.
     * @throws UnknownPlayerException if the sender is unknown.
     * @throws GameNotExistingException if the game does not exist.
     */
    public void canSendGroupChat(String sender, String text) throws BadTextException, InvalidStateException, RemoteException, UnknownPlayerException, GameNotExistingException;

    /**
     * Checks if a player can send a private chat message.
     *
     * @param sender the nickname of the sender.
     * @param receiver the nickname of the receiver.
     * @param text the text message.
     * @throws RemoteException if a remote communication error occurs.
     * @throws UnknownPlayerException if the sender or receiver is unknown.
     * @throws GameNotExistingException if the game does not exist.
     * @throws PlayerAbsentException if the receiver is absent.
     * @throws BadTextException if the text is inappropriate.
     * @throws InvalidStateException if the game state is invalid.
     * @throws ParametersException if there is a parameter error.
     */
    public void canSendPrivateChat(String sender, String receiver, String text) throws UnknownPlayerException, GameNotExistingException, PlayerAbsentException, BadTextException, InvalidStateException, ParametersException, RemoteException;

    /**
     * Allows a player to rejoin the game.
     *
     * @param nickname the nickname of the player.
     * @param ConnectionType the type of connection (e.g., RMI, Socket).
     * @throws RemoteException if a remote communication error occurs.
     * @throws InvalidStateException if the game state is invalid.
     * @throws GameNotExistingException if the game does not exist.
     * @throws UnknownPlayerException if the player is unknown.
     */
    void rejoinGame(String nickname, String ConnectionType) throws RemoteException, InvalidStateException, GameNotExistingException, UnknownPlayerException;

    /**
     * Sets the last heartbeat time for a player.
     *
     * @param nickname the nickname of the player.
     * @throws RemoteException if a remote communication error occurs.
     */
    void setLastHeartBeat(String nickname) throws RemoteException;
}
