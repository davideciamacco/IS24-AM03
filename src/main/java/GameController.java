package it.polimi.ingsw.is24am03;

import java.util.ArrayList;

/**
 * the game controller ensures the communication through client controller and server model
 */
public class GameController implements GameInterface{
    /**
     * locks used for thread synchronization
     */
    private final Object gameLock;
    /**
     * Using the GameInterface type for gameModel allows flexibility.
     */
    private final GameInterface gameModel;
    /**
     * Constructs a GameController with the specified game.
     *
     * @param game the game model to be associated with this controller
     */
    public GameController(Game game){
        gameModel = game;
        gameLock= new Object();
    }
/*
    public void startGame()
    {
        synchronized (gameLock)
        {
            if(!gameModel.getState().equals(State.PREPARATION)) throw new InvalidStateException();
            gameModel.startGame();
        }
    }*/

    /**
     * Draws resources for the player from the resource deck.
     *
     * @param P        the player who is drawing resources
     * @param RD  the resource deck from which resources are drawn
     * @throws PlayerNotInTurnException if the player is not currently in turn
     * @throws FullHandException        if the player's hand is already full
     * @throws InvalidStateException   if the game state is not suitable for drawing resources
     */

    public void drawResources(Player P, ResourceDeck RD) throws PlayerNotInTurnException
    {
        synchronized (gameLock)
        {
            if(!P.equals(gameModel.getCurrentPlayer())) throw new PlayerNotInTurnException();
            if(P.getHand().size()==3) throw new FullHandException();
            if(!gameModel.getState().equals(State.DRAWING)) throw new InvalidStateException();
            gameModel.drawResources(P, RD);
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
     * @param player   the player who is drawing gold
     * @param goldDeck the gold deck from which gold is drawn
     * @throws PlayerNotInTurnException if the player is not currently in turn
     * @throws FullHandException        if the player's hand is already full
     * @throws InvalidStateException   if the game state is not suitable for drawing gold
     */
    public void drawGold(Player P, GoldDeck GD)
    {
        synchronized (gameLock)
        {
            if(!P.equals(gameModel.getCurrentPlayer())) throw new PlayerNotInTurnException();
            if(P.getHand().size()==3) throw new FullHandException();
            if(!gameModel.getState().equals(State.DRAWING)) throw new InvalidStateException();
        }
    }
    /**
     * Draws a table card for the player based on the choice provided.
     *
     * @param P the player who is drawing the table card
     * @param choice the choice indicating which table card to draw
     * @throws PlayerNotInTurnException if the player is not currently in turn
     * @throws FullHandException        if the player's hand is already full
     * @throws InvalidStateException   if the game state is not suitable for drawing table cards
     */
    public void drawTable(Player P, int choice)
    {
        synchronized (gameLock)
        {
            if(!P.equals(gameModel.getCurrentPlayer())) throw new PlayerNotInTurnException();
            if(P.getHand().size()==3) throw new FullHandException();
            if(!gameModel.getState().equals(State.DRAWING)) throw new InvalidStateException();
            gameModel.drawTable(P,choice);
        }
    }
    /**
     * Adds a player to the game.
     *
     * @param p the player to be added
     * @throws FullLobbyException if the lobby is already full
     * @throws NicknameAlreadyUsedException if the provided nickname is already used by another player
     */
    public void addPlayer(Player p){
        synchronized (gameLock)
        {
            if(gameModel.getPlayers().size()==gameModel.getNumPlayers()) throw new FullLobbyException();
            for(Player player: gameModel.getPlayers())
                if(player.getNickname().equals(p.getNickname())) throw new NicknameAlreadyUsedException();
            gameModel.addPlayer(p);
        }
    }
    /**
     *
     */
    public int getAvailableColors(){
        synchronized (gameLock)
        {
            return gameModel.getAvailableColors();
        }
    }
    /**
     * Places a card on the table for the player.
     *
     * @param p the player placing the card
     * @param c  the card to be placed
     * @param i      the row index for placement
     * @param j      the column index for placement
     * @param face   whether the card should be placed face up or not
     * @throws PlayerNotInTurnException if the player is not currently in turn
     * @throws InvalidStateException   if the game state is not suitable for placing cards
     */
    public int placeCard(Player p,PlayableCard c,int i, int j, boolean face){
        synchronized (gameLock)
        {
            if(!P.equals(gameModel.getCurrentPlayer())) throw new PlayerNotInTurnException();
    //      if(P.getHand().size()==0) throw new EmptyHandException();
            if(!gameModel.getState().equals(State.PLAYING)) throw new InvalidStateException();
            gameModel.placeCard(p,c,i,j,face);
        }
    }
}
