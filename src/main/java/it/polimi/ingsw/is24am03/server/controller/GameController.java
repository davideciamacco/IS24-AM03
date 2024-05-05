package it.polimi.ingsw.is24am03.server.controller;

import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.game.Game;
import it.polimi.ingsw.is24am03.server.model.game.RemoteGameController;
import it.polimi.ingsw.is24am03.server.model.player.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * the game controller ensures the communication through client controller and server model
 */
public class GameController extends UnicastRemoteObject implements RemoteGameController {
    /**
     * locks used for thread synchronization
     */
    private final Object gameLock;

    /**
     * Using the GameInterface type for gameModel allows flexibility.
     */
    private Game gameModel = null;

    /**
     * Constructs a GameController with the specified game.
     *
     //* @param game the game model to be associated with this controller
     */
    public GameController() throws RemoteException {
        gameLock= new Object();
    }


    public void createGame(int numPlayers, String nickname){
        synchronized (gameLock) {
            if(nickname.isBlank() || numPlayers<2 || numPlayers>4) throw new IllegalArgumentException();
            if(gameModel!=null) throw new RuntimeException();
            gameModel = new Game(numPlayers, nickname);
        }
    }

    public void selectStartingFace(String player, boolean face) throws PlayerNotInTurnException, InvalidStateException {
        synchronized(gameLock) {
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if (!gameModel.getGameState().equals(State.PREPARATION_1))
                throw new InvalidStateException("Action not allowed in this state");
            gameModel.selectStartingFace(player, face);
        }
    }

    public void setObjectiveCard(String player, int choice) throws PlayerNotInTurnException {
        if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
            throw new PlayerNotInTurnException();
        }
        if(choice<1 || choice>2) throw new IllegalArgumentException();
        gameModel.setObjectiveCard(player, choice);
    }

    /*
    public void startGame() {
        gameModel.startGame();
        /*ArrayList<ObjectiveCard> ObjectiveOptions = new ArrayList<ObjectiveCard>();
        for(int i=0; i<gameModel.getNumPlayers(); i++){
            ObjectiveOptions.add(drawObjectiveOptions());
        }
    }*/

    /*
    private ObjectiveCard drawObjectiveOptions(){
        return gameModel.drawObjectiveOptions();
    }*/

    /**
     * Draws resources for the player from the resource deck.
     *
     * @param player represents the nickname of the player who is drawing resources
    //* @param RD  the resource deck from which resources are drawn
     * @throws PlayerNotInTurnException if the player is not currently in turn
    //* @throws FullHandException        if the player's hand is already full
     * @throws InvalidStateException   if the game state is not suitable for drawing resources
     */

    public void drawResources(String player) throws PlayerNotInTurnException, InvalidStateException {
        synchronized (gameLock) {
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            //if(P.getHand().size()==3) throw new FullHandException();
            if (!gameModel.getGameState().equals(State.DRAWING))
                throw new InvalidStateException("Action not allowed in this state");
            try {
                gameModel.drawResources(player);
            } catch (EmptyDeckException e)
            {}
        }
    }

    /*public ArrayList<Player> checkWinner()
    {
        synchronized (gameLock)
        {
            return gameModel.checkWinner();
        }
    }*/

    /**
     * Draws gold for the player from the gold deck.
     *
     //* @param player   the player who is drawing gold
     //* @param goldDeck the gold deck from which gold is drawn
     * @throws PlayerNotInTurnException if the player is not currently in turn
    //* @throws FullHandException        if the player's hand is already full
     * @throws InvalidStateException   if the game state is not suitable for drawing gold
     */
    public void drawGold(String player) throws PlayerNotInTurnException, InvalidStateException {
        synchronized (gameLock)
        {
            synchronized (gameLock) {
                if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                    throw new PlayerNotInTurnException();
                }
                //if(P.getHand().size()==3) throw new FullHandException();
                if (!gameModel.getGameState().equals(State.DRAWING))
                    throw new InvalidStateException("Action not allowed in this state");
                try {
                    gameModel.drawGold(player);
                } catch (EmptyDeckException e)
                {}
            }
        }
    }

    /**
     * Draws a table card for the player based on the choice provided.
     *
     * @param player the player who is drawing the table card
     * @param choice the choice indicating which table card to draw
     * @throws PlayerNotInTurnException if the player is not currently in turn
    //* @throws FullHandException        if the player's hand is already full
     * @throws InvalidStateException   if the game state is not suitable for drawing table cards
     */
    public void drawTable(String player, int choice) throws PlayerNotInTurnException, InvalidStateException {
        synchronized (gameLock)
        {
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(choice<1 || choice>4) throw new IllegalArgumentException();
            //if(P.getHand().size()==3) throw new FullHandException();
            if(!gameModel.getGameState().equals(State.DRAWING)) throw new InvalidStateException("");
            try {
                gameModel.drawTable(player, choice);
            }
            catch(NullCardSelectedException e){}
        }
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to be added
     * @throws FullLobbyException if the lobby is already full
     * @throws NicknameAlreadyUsedException if the provided nickname is already used by another player
     */
    public void addPlayer(String player) throws FullLobbyException, NicknameAlreadyUsedException {
        synchronized (gameLock)
        {
            if(gameModel.getPlayers().size()==gameModel.getNumPlayers()) throw new FullLobbyException();
            for(Player p: gameModel.getPlayers())
                if(p.getNickname().equals(player)) throw new NicknameAlreadyUsedException();
            gameModel.addPlayer(player);
        }
    }
    /**
     *
     */

    /*
    public int getAvailableColors(){
        synchronized (gameLock)
        {
            return gameModel.getAvailableColors();
        }*/

    /**
     * Places a card on the table for the player.
     *
     * @param player the player placing the card
     * @param choice  the card to be placed
     * @param i      the row index for placement
     * @param j      the column index for placement
     * @param face   whether the card should be placed face up or not
     * @throws PlayerNotInTurnException if the player is not currently in turn
     * @throws InvalidStateException   if the game state is not suitable for placing cards
     */
    public void placeCard(String player, int choice, int i, int j, boolean face) throws PlayerNotInTurnException, InvalidStateException {
        synchronized (gameLock)
        {
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            //      if(P.getHand().size()==0) throw new EmptyHandException();
            if(!gameModel.getGameState().equals(State.PLAYING)) throw new InvalidStateException("Action not allowed in this state");
            if(choice<1 || choice>3) throw new IllegalArgumentException();
            //try {
            gameModel.placeCard(player, choice, i, j, face);
            //}
            //catch(){};
        }
    }

    public Game getGameModel() {
        return gameModel;
    }
}
