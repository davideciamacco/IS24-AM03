package it.polimi.ingsw.is24am03.client.connection;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerBoardSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.client.view.ViewInterface;
import it.polimi.ingsw.is24am03.client.clientModel.ClientModel;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.game.RemoteGameController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the Client interface using Remote Method Invocation (RMI) for game interaction.
 * Manages client-server communication, game actions (joining, card selection), and message sending.
 */

public class ClientRMI implements Client{
    Registry registry;
    private RemoteGameController gameController;
    private ViewInterface view;
    private final String ip;
    private final int port;
    private boolean hasJoined;
    private String nickname;
    private ClientModel clientModel;
    private final ScheduledExecutorService heartbeatScheduler = Executors.newScheduledThreadPool(1);

    /**
     * Initializes a new instance of the ClientRMI class.
     * This constructor attempts to connect to a remote game controller via RMI and starts a heartbeat sender.
     *
     * @param hostName the hostname of the remote registry.
     * @param portNumber the port number on which the remote registry is running.
     * @param view the ViewInterface instance used for interacting with the client view.
     */
    public ClientRMI(String hostName, int portNumber, ViewInterface view) {
        boolean connected = false;
        this.hasJoined = false;
        RemoteGameController temp = null;
        this.ip = hostName;
        this.port = portNumber;
        this.view = view;
        while (!connected) {
            try {
                this.registry = LocateRegistry.getRegistry(hostName, portNumber);
                String remoteObjectName = "game_controller";
                temp = (RemoteGameController) registry.lookup(remoteObjectName);
                connected = true;
            } catch (RemoteException e) {
            } catch (NotBoundException e) {
            }
        }
        this.gameController = temp;
        startHeartbeatSender();
    }


    /**
     * Creates a new game with the specified number of players and the given nickname.
     * This method initializes the client model, invokes the remote game controller to create a game, and updates the view accordingly.
     *
     * @param nPlayers the number of players for the game.
     * @param nickname the nickname of the player creating the game.
     */
    public void CreateGame(int nPlayers, String nickname) {
        try {
            clientModel = new ClientModel(nickname, view);
            this.gameController.createGame(nPlayers, nickname, "RMI");
            view.confirmCreate();
            this.nickname = nickname;
            this.subscribeToObservers();
            hasJoined = true;
        } catch (IllegalArgumentException e) {
            view.drawError("Invalid arguments");
        } catch (GameAlreadyCreatedException e) {
            view.drawError("Game already created");
        } catch (RemoteException ignored) {
        }
        System.out.flush();
    }


    /**
     * Allows a player to join an existing game with the specified nickname.
     * This method initializes the client model, invokes the remote game controller to add the player, and updates the view accordingly.
     *
     * @param nickname the nickname of the player joining the game.
     */
    public void JoinGame(String nickname) {
        try {
            if (!hasJoined) {
                clientModel = new ClientModel(nickname, view);
                this.gameController.addPlayer(nickname, "RMI");
                view.confirmJoin();
                hasJoined = true;
                this.nickname = nickname;
                this.subscribeToObservers();
                this.gameController.canStart();
            } else {
                view.drawError("Already joined");
            }
        } catch (IllegalArgumentException e) {
            view.drawError("Nickname not allowed");
        } catch (FullLobbyException e) {
            view.drawError("Lobby is full");
        } catch (NicknameAlreadyUsedException e) {
            view.drawError("Nickname already used");
        } catch (GameNotExistingException e) {
            view.drawError("Game not existing");
        } catch (RemoteException ignored) {
        }
        System.out.flush();
    }

    /**
     * Allows a player to pick a color for their game piece.
     * This method checks if the color can be picked and then assigns the color to the player.
     *
     * @param color the color the player wants to pick.
     */
    public void PickColor(String color) {
        try {
            this.gameController.canPickColor(nickname, color);
            this.gameController.pickColor(nickname, color);
            hasJoined = true;
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e) {
            clientModel.printNotifications("Action not allowed in this state");
        } catch (ColorAlreadyPickedException e) {
            clientModel.printNotifications("Color not available");
        } catch (RemoteException ignored) {

        } catch (GameNotExistingException e) {
            view.drawError("Game not existing");
        } catch (UnknownPlayerException e) {
            view.drawError(e.getMessage());
        }
        System.out.flush();
    }

    /**
     * Allows a player to choose a starting card side.
     * This method checks if the player can select the starting card face and then performs the selection.
     *
     * @param face the side of the starting card the player wants to choose.
     */
    public void ChooseStartingCardSide(String face) {
        try {
            this.gameController.canSelectStartingFace(nickname, face);
            clientModel.printNotifications("Starting card side chosen successfully");
            this.gameController.selectStartingFace(nickname, face);
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e) {
            clientModel.printNotifications("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("Game doesn't exist");
        } catch (ArgumentException e) {
            view.drawError(e.getMessage());
        } catch (RemoteException e) {
        } catch (UnknownPlayerException e) {
            clientModel.printNotifications(e.getMessage());
        }
        System.out.flush();
    }

    /**
     * Sets the graphical user interface (GUI) view for this client.
     *
     * @param view the ViewInterface instance representing the GUI.
     */
    @Override
    public void setGUI(ViewInterface view) {
        this.view = view;
    }

    /**
     * Sets the command-line interface (CLI) view for this client.
     *
     * @param view the ViewInterface instance representing the CLI.
     */
    @Override
    public void setCLI(ViewInterface view) {
        this.view = view;
    }

    /**
     * Places a card on the game board at the specified coordinates and face.
     *
     * @param choice the choice of card to place.
     * @param i the row index of the card placement.
     * @param j the column index of the card placement.
     * @param face the face of the card to place.
     */
    public void PlaceCard(int choice, int i, int j, String face) {
        try {
            this.gameController.placeCard(nickname, choice, i, j, face);
        } catch (ArgumentException e) {
            clientModel.printNotifications("Invalid command");
        } catch (UnknownPlayerException e) {
            view.drawError(e.getMessage());
        } catch (PositionOccupiedException e) {
            clientModel.printNotifications("Position is not empty");
        } catch (CoordinatesOutOfBoundsException e) {
            clientModel.printNotifications("Coordinates out of bound");
        } catch (NoCardsAvailableException e) {
            clientModel.printNotifications("Card can't be placed in these coordinates");
        } catch (RequirementsNotMetException e) {
            clientModel.printNotifications("Gold card requirements not satisfied");
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e) {
            clientModel.printNotifications("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("GameNotExists exception");
        } catch(RemoteException ignored) {}
        System.out.flush();
    }

    /**
     * Allows the player to draw a gold card from the deck.
     * This method checks if the player can draw a gold card and performs the draw operation.
     */
    public void DrawGold() {
        try {
            this.gameController.canDrawGold(nickname);
            clientModel.printNotifications("Gold card drawn successfully");
            this.gameController.drawGold(nickname);
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e) {
            clientModel.printNotifications("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("GameNotExists exception");
        } catch (EmptyDeckException e) {
            clientModel.printNotifications("Empty deck exception");
        } catch (UnknownPlayerException e) {
            view.drawError(e.getMessage());
        } catch (RemoteException ignored) {}
        System.out.flush();
    }

    /**
     * Allows the player to draw resource cards from the deck.
     * This method checks if the player can draw resource cards and performs the draw operation.
     */
    public void DrawResource() {
        try {
            this.gameController.canDrawResources(nickname);
            clientModel.printNotifications("Resource card drawn successfully");
            this.gameController.drawResources(nickname);
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e) {
            clientModel.printNotifications("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("GameNotExists exception");
        } catch (EmptyDeckException e) {
            clientModel.printNotifications("Empty deck exception");
        } catch (UnknownPlayerException e) {
            view.drawError(e.getMessage());
        } catch (RemoteException e) {
        }
        System.out.flush();
    }

    /**
     * Allows the player to draw a card from the table.
     *
     * @param choice the choice of the card to draw.
     */
    public void DrawTable(int choice) {
        try {
            this.gameController.canDrawTable(nickname, choice);
            clientModel.printNotifications("Card drawn successfully");
            this.gameController.drawTable(nickname, choice);
        } catch (UnknownPlayerException e) {
            view.drawError(e.getMessage());
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e) {
            clientModel.printNotifications("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("Game not existing exception");
        } catch (NullCardSelectedException e) {
            clientModel.printNotifications("NullCardSelectedException exception");
        } catch (RemoteException ignored) {
        }
        System.out.flush();
    }


    /**
     * Allows the player to choose an objective card.
     *
     * @param choice the choice of the objective card.
     */
    public void ChooseObjectiveCard(int choice) {
        try {
            this.gameController.canSetObjectiveCard(nickname, choice);
            clientModel.printNotifications("Objective card selected successfully");
            this.gameController.setObjectiveCard(nickname, choice);
        } catch (GameNotExistingException e) {
            view.drawError("Game not existing");
        } catch (UnknownPlayerException e) {
            view.drawError(e.getMessage());
        } catch (PlayerNotInTurnException e) {
            clientModel.printNotifications("Not your turn");
        } catch (InvalidStateException e) {
            clientModel.printNotifications("Action not allowed in this state");
        } catch (IllegalArgumentException e) {
            clientModel.printNotifications("Invalid arguments");
        } catch (RemoteException e) {
        }
    }
    /**
     * Allows a player to rejoin an existing game.
     *
     * @param nickname the nickname of the player rejoining the game.
     */
    public void RejoinGame(String nickname) {
        try {
            if (!hasJoined) {
                this.gameController.rejoinGame(nickname, "RMI");
                clientModel = new ClientModel(nickname, view);
                hasJoined = true;
                this.nickname = nickname;
                this.subscribeToObservers();
                this.gameController.rejoinedChief(nickname);
            } else {
                clientModel.printNotifications("Already joined");
            }
        } catch (UnknownPlayerException e) {
            view.drawError(e.getMessage());
        } catch (RemoteException ignored) {
        } catch (InvalidStateException e) {
            view.drawError("Action not allowed in this state");
        } catch (GameNotExistingException e) {
            view.drawError("Game doesn't exist");
        }
    }
    /**
     * Sends a text message to all players in the game.
     *
     * @param text the text message to send.
     */
    public void sendGroupText(String text) {
        try {
            this.gameController.canSendGroupChat(this.nickname, text);
            this.gameController.sendGroupText(this.nickname, text);
        } catch (BadTextException | InvalidStateException e) {
            clientModel.printNotifications(e.getMessage());
        } catch (RemoteException e) {
        } catch (UnknownPlayerException e) {
            view.drawError(e.getMessage());
        } catch (GameNotExistingException e) {
            view.drawError("Game doesn't exist");
        }
    }

    /**
     * Sends a private text message to a specific player.
     *
     * @param receiver the nickname of the recipient player.
     * @param text     the text message to send.
     */
    public void sendPrivateText(String receiver, String text) {
        try {
            this.gameController.canSendPrivateChat(this.nickname, receiver, text);
            this.gameController.sendPrivateText(this.nickname, receiver, text);
        } catch (BadTextException | InvalidStateException | PlayerAbsentException | ParametersException e) {
            clientModel.printNotifications(e.getMessage());
        } catch (RemoteException e) {
        } catch (UnknownPlayerException e) {
            view.drawError(e.getMessage());
        } catch (GameNotExistingException e) {
            view.drawError("Game doesn't exist");
        }
    }
    /**
     * Subscribes the client model to game-related observers.
     * These observers include GameSub, ChatSub, PlayerSub, and PlayerBoardSub.
     */
    private void subscribeToObservers() {
        try {
            gameController.addToObserver((GameSub) clientModel);
            gameController.addToObserver((ChatSub) clientModel);
            gameController.addToObserver((PlayerSub) clientModel);
            gameController.addToObserver((PlayerBoardSub) clientModel);
        } catch (RemoteException ignored) {
        }
    }

    /**
     * Starts a heartbeat sender that periodically sends a heartbeat signal to the server.
     * This ensures the client remains connected and active.
     */
    private void startHeartbeatSender() {
        long heartbeatInterval = 5000;
        heartbeatScheduler.scheduleAtFixedRate(this::sendHeartbeat, 0, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * Sends a heartbeat signal to the game controller to indicate the client is still active.
     * If a RemoteException occurs during this process, the client assumes the server is disconnected and terminates.
     */
    private void sendHeartbeat() {
        try {
            gameController.setLastHeartBeat(nickname);
        } catch (RemoteException e) {
            System.out.println("Server disconnected. Closing client...");
            System.exit(0);
        }
    }

}


