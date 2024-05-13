package it.polimi.ingsw.is24am03.server.controller;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerBoardSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.game.Game;
import it.polimi.ingsw.is24am03.server.model.game.RemoteGameController;
import it.polimi.ingsw.is24am03.server.model.player.Player;
import org.controlsfx.control.spreadsheet.SpreadsheetCellEditor;

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
    private final Object chatLock;

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
        chatLock=new Object();
    }


    public void createGame(int numPlayers, String nickname) throws GameAlreadyCreatedException {
        synchronized (gameLock) {
            if(nickname.isBlank() || numPlayers<2 || numPlayers>4) throw new IllegalArgumentException();
            if(gameModel!=null) throw new GameAlreadyCreatedException();
            gameModel = new Game(numPlayers, nickname);
        }
    }

    public void selectStartingFace(String player, String face) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException {
        boolean faceBoolean;
        synchronized(gameLock) {
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(gameModel == null)
                throw new GameNotExistingException();

            if (!gameModel.getGameState().equals(State.STARTING))
                throw new InvalidStateException("Action not allowed in this state");
            if(face.equals("FRONT"))
                faceBoolean=true;
            else if (face.equals("BACK")) {
                faceBoolean=false;
            }
            else{
                throw new IllegalArgumentException();
            }
            gameModel.selectStartingFace(player, faceBoolean);
        }
    }

    public void setObjectiveCard(String player, int choice) throws PlayerNotInTurnException, GameNotExistingException {
        if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
            throw new PlayerNotInTurnException();
        }
        if(gameModel == null)
            throw new GameNotExistingException();
        if(choice<1 || choice>2) throw new IllegalArgumentException();
        gameModel.setObjectiveCard(player, choice);
    }


    /**
     * Draws resources for the player from the resource deck.
     *
     * @param player represents the nickname of the player who is drawing resources
    //* @param RD  the resource deck from which resources are drawn
     * @throws PlayerNotInTurnException if the player is not currently in turn
    //* @throws FullHandException        if the player's hand is already full
     * @throws InvalidStateException   if the game state is not suitable for drawing resources
     */

    public void drawResources(String player) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException {
        synchronized (gameLock) {
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player))
                throw new PlayerNotInTurnException();
            if(gameModel == null)
                throw new GameNotExistingException();
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
    public void drawGold(String player) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException {
        synchronized (gameLock)
        {
            synchronized (gameLock) {
                if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                    throw new PlayerNotInTurnException();
                }
                if(gameModel == null)
                    throw new GameNotExistingException();
                if (!gameModel.getGameState().equals(State.DRAWING))
                    throw new InvalidStateException("Action not allowed in this state");
                try {
                    gameModel.drawGold(player);
                }
                catch (EmptyDeckException e)
                {

                }
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
    public void drawTable(String player, int choice) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException {
        synchronized (gameLock)
        {
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(gameModel == null) throw new GameNotExistingException();
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
    public void addPlayer(String player) throws FullLobbyException, NicknameAlreadyUsedException, IllegalArgumentException, GameNotExistingException {
        synchronized (gameLock)
        {
            if(gameModel == null) throw new GameNotExistingException();
            if(gameModel.getPlayers().size()==gameModel.getNumPlayers()) throw new FullLobbyException();
            if(player.isBlank() || player.equals("ALL")) throw new IllegalArgumentException();
            for(Player p: gameModel.getPlayers())
                if(p.getNickname().equals(player)) throw new NicknameAlreadyUsedException();
            gameModel.addPlayer(player);
        }
    }

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
    public void placeCard(String player, int choice, int i, int j, String face) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException {
        synchronized (gameLock)
        {
            boolean faceBoolean;
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(gameModel == null) throw new GameNotExistingException();
            //      if(P.getHand().size()==0) throw new EmptyHandException();
            if(!gameModel.getGameState().equals(State.PLAYING)) throw new InvalidStateException("Action not allowed in this state");
            if(choice<1 || choice>3) throw new IllegalArgumentException();

            if(face.equals("FRONT"))
                faceBoolean = true;
            else if(face.equals("BACK"))
                faceBoolean = false;
            else
                throw new IllegalArgumentException();
            gameModel.placeCard(player, choice, i, j, faceBoolean);
        }
    }

    public void pickColor(String player, String color) throws PlayerNotInTurnException, InvalidStateException, ColorAlreadyPickedException, GameNotExistingException {
        Color chosenColor;
        boolean flag=false;
        synchronized (gameLock)
        {
            if(gameModel == null) throw new GameNotExistingException();
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(!gameModel.getGameState().equals(State.COLOR)) throw new InvalidStateException("Action not allowed in this state");
            switch(color)
            {
                case "RED":
                    chosenColor= Color.RED;
                    break;
                case "BLUE":
                    chosenColor= Color.BLUE;
                    break;
                case "GREEN":
                    chosenColor= Color.GREEN;
                    break;
                case "YELLOW":
                    chosenColor= Color.YELLOW;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            for(int i=0; i<gameModel.getAvailableColors().size(); i++){
                if(chosenColor.equals(gameModel.getAvailableColors().get(i)))
                    flag=true;
            }
            if(flag)
                gameModel.setColor(chosenColor);
            else
                throw new ColorAlreadyPickedException();
        }
    }


    public Game getGameModel() {
        return gameModel;
    }

    //UPDATE OBSERVERS LOGIC//
    public void addToObserver(GameSub gameSub){ synchronized (gameLock){gameModel.addSub(gameSub);}}
    public void addToObserver(PlayerSub playerSub){  synchronized (gameLock){gameModel.addSub(playerSub);}}
    public void addToObserver(ChatSub chatSub){ synchronized (gameLock){gameModel.addSub(chatSub);}}
    public void addToObserver(PlayerBoardSub playerBoardSub) { synchronized (gameLock){gameModel.addSub(playerBoardSub);}}

    public void removeSub(GameSub gameSub){synchronized (gameLock){gameModel.removeSub(gameSub);}}
    public void removeSub(PlayerSub gameSub){synchronized (gameLock){gameModel.removeSub(gameSub);}}
    public void removeSub(PlayerBoardSub gameSub){synchronized (gameLock){gameModel.removeSub(gameSub);}}
    public void removeSub(ChatSub gameSub){synchronized (gameLock){gameModel.removeSub(gameSub);}}
    public void sendGroupText(String sender, String text) throws BadTextException,InvalidStateException{
        synchronized (chatLock){
            gameModel.sendGroupMessage(sender, text);
        }
    }
    public void sendPrivateText(String sender, String receiver, String text) throws PlayerAbsentException, BadTextException, InvalidStateException, ParametersException{
        synchronized (chatLock){
            gameModel.sendPrivateMessage(sender, receiver,text);
        }
    }
}
