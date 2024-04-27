package it.polimi.ingsw.is24am03.server.model.player;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.ObjectiveType;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Represents the board of a player in the game.
 */

public class PlayerBoard {
    private static final int MAX_ROWS = 81;
    private static final int MAX_COLS = 81;
    private final Player player;
    private final PlayableCard[][] board;
    private final Map<CornerItem, Integer> availableItems;
    /**
     * Constructs a player board with the given player.
     *
     * @param player The player to whom this board belongs.
     */
    public PlayerBoard(Player player) {
        this.player = player;
        this.board =new PlayableCard[MAX_ROWS][MAX_COLS];
        this.availableItems = new HashMap<CornerItem, Integer>();
        for (CornerItem item : CornerItem.values()) {
            availableItems.put(item, 0);
        }
    }
    //
    /**
     * Checks the visibility of corners when placing a card.
     *
     * @param x      The x-coordinate of the card placement.
     * @param y      The y-coordinate of the card placement.
     * @param corner The corner to check visibility.
     * @throws IllegalArgumentException If the corner is not visible.
     */
    private void checkCornerVisibility(int x, int y,int corner) {
        if (board[x][y] != null) {
            if (board[x][y].getFace()) {
                if (!board[x][y].getFrontCorner(corner).isVisible()) {
                    throw new CornerNotVisibleException();

                }
            } else {
                if (!board[x][y].getBackCorner(corner).isVisible()) {
                    throw new CornerNotVisibleException();

                }
            }
        }
    }
    /**
     * Increases the count of each item when placing a card.
     *
     * @param card The card being placed.
     */
    private void increaseItemCount(PlayableCard card) {
        if(card.getFace()){
            for (int i=0;i<=3;i++) {
                CornerItem item = card.getFrontCorner(i).getItem();
                availableItems.put(item, availableItems.get(item) + 1);
            }
        }else{
            CornerItem item = card.getKingdomsType();
            //in playable card getkingdom type
            availableItems.put(item, availableItems.get(item) + 1);
        }
    }
    /**
     * Decreases the count of each item when a card is placed over them.
     *
     * @param i The x-coordinate of the card placement.
     * @param j The y-coordinate of the card placement.
     */
    private void updateItemCount(int i,int j){
        if(i+1<MAX_ROWS && j+1<MAX_COLS){
            if(board[i+1][j+1]!=null){
                if(board[i+1][j+1].getFace()){
                    if(!board[i+1][j+1].getFrontCorner(0).isEmpty()){
                        CornerItem item = board[i+1][j+1].getFrontCorner(0).getItem();
                        availableItems.put(item, availableItems.get(item) -1);
                    }
                }
            }
        }
        if(i-1>=0 && j-1>=0){
            if(board[i-1][j-1]!=null){
                if(board[i-1][j-1].getFace()){
                    if(!board[i-1][j-1].getFrontCorner(2).isEmpty()){
                        CornerItem item = board[i-1][j-1].getFrontCorner(2).getItem();
                        availableItems.put(item, availableItems.get(item) -1);
                    }
                }
            }
        }
        if(i-1>=0 && j+1<MAX_COLS){
            if(board[i-1][j+1]!=null){
                if(board[i-1][j+1].getFace()){
                    if(!board[i-1][j+1].getFrontCorner(3).isEmpty()){
                        CornerItem item = board[i-1][j+1].getFrontCorner(3).getItem();
                        availableItems.put(item, availableItems.get(item) -1);
                    }
                }
            }
        }
        if(i+1<MAX_ROWS && j+1>=0){
            if(board[i+1][j-1]!=null){
                if(board[i+1][j-1].getFace()){
                    if(!board[i+1][j-1].getFrontCorner(1).isEmpty()){
                        CornerItem item = board[i+1][j-1].getFrontCorner(1).getItem();
                        availableItems.put(item, availableItems.get(item) -1);
                    }
                }
            }
        }

    }
    /**
     * Checks if the player meets the requirements to play a card.
     *
     * @param cardRequirements The requirements of the card to be played.
     * @return True if the player meets the requirements, false otherwise.
     */
    public boolean checkRequirements(ArrayList<CornerItem> cardRequirements)
    {
        Map<CornerItem, Integer> itemRequired = new HashMap<CornerItem, Integer>();
        for(CornerItem item: CornerItem.values())
            itemRequired.put(item, 0);
        for(CornerItem item: cardRequirements)
            itemRequired.put(item, itemRequired.get(item) + 1);
        for(CornerItem item: itemRequired.keySet())
        {
            if(availableItems.get(item)<itemRequired.get(item))
                return false;
        }
        return true;
    }
    /**
     * Gives points to the player based on the type of card played.
     *
     * @param c The card being played.
     * @param i The x-coordinate of the card placement.
     * @param j The y-coordinate of the card placement.
     */
    public void giveCardPoints(PlayableCard c, int i, int j){
        int type,scoringType,n=0;
        type= c.getType();
        if(type==1){
            //resource card
            player.addPoints(c.getPoints());
        }else{
            scoringType=c.getScoringType();
            if(scoringType==0){
                n=availableItems.get(c.getObject());
                for(i=0; i<n; i++)
                    player.addPoints(c.getPoints());
            }else if(scoringType==1){
                if(board[i-1][j-1]!=null)
                    n++;
                if(board[i+1][j+1]!=null)
                    n++;
                if(board[i-1][j+1]!=null)
                    n++;
                if(board[i+1][j-1]!=null)
                    n++;
                for(i=0; i<n; i++)
                    player.addPoints(c.getPoints());
            }else if(scoringType==2){
                player.addPoints(c.getPoints());
            }
        }
    }
    /**
     * Checks how many times the specified objective has been completed on the player's board.
     *
     * @param ob The objective card to check.
     * @return The number of times the objective has been completed.
     */
    public int checkObjective(ObjectiveCard ob){
        int n;
        int[][] verify = new int[MAX_ROWS][MAX_COLS];
        int i,j,z,nvoltecompletato=0,counter, Iiniz,Jiniz;
        if(ob.getType().equals(ObjectiveType.ITEM)){
            if(ob.getTypeList()==1){
                n=availableItems.get(ob.getRequirement())/2;
                return n*(ob.getPoints());
            }else if(ob.getTypeList()==2){
                n=availableItems.get(CornerItem.QUILL);
                if(availableItems.get(CornerItem.INKWELL)<n){
                    n=availableItems.get(CornerItem.INKWELL);
                }
                if(availableItems.get(CornerItem.MANUSCRIPT)<n) {
                    n=availableItems.get(CornerItem.MANUSCRIPT);
                }
                return n*(ob.getPoints());
            }else{
                n=availableItems.get(ob.getRequirement())/3;
                return n*(ob.getPoints());
            }
        }
        else if(ob.getType().equals(ObjectiveType.PATTERNDIAGONAL)){
            for(i=0;i<MAX_ROWS;i++){
                for(j=0;j<MAX_COLS;j++){
                    verify[i][j]=0;
                }
            }
            if(ob.getDirection()==0){
                for(i=0;i<MAX_ROWS;i++){
                    for(j=0;j<MAX_COLS;j++){
                        Iiniz=i;
                        Jiniz=j;
                        if(board[i][j]!=null){
                            if(board[i][j].getKingdomsType().equals(ob.getKingdomsType()) && verify[i][j]!=1){
                                counter=1;
                                for(z=1;z<=2;z++){
                                    if(i-z>=0 && j+z<MAX_COLS){
                                        if(board[i-z][j+z]!=null){
                                            if(board[i-z][j+z].getKingdomsType().equals(ob.getKingdomsType()) && verify[i-z][j+z]!=1){
                                                counter++;
                                            }

                                        }
                                    }
                                }
                                if(counter==3){
                                    nvoltecompletato++;
                                    verify[Iiniz][Jiniz]=1;
                                    verify[Iiniz-1][Jiniz+1]=1;
                                    verify[Iiniz-2][Jiniz+2]=1;
                                }
                            }
                        }
                    }
                }
            }else if(ob.getDirection()==1){
                for(i=0;i<MAX_ROWS;i++){
                    for(j=0;j<MAX_COLS;j++){
                        Iiniz=i;
                        Jiniz=j;
                        if(board[i][j]!=null ){
                            if(board[i][j].getKingdomsType().equals(ob.getKingdomsType()) && verify[i][j]!=1){
                                counter=1;
                                for(z=1;z<=2;z++){
                                    if(i+z<MAX_ROWS && j+z<MAX_COLS){
                                        if(board[i+z][j+z]!=null){
                                            if(board[i+z][j+z].getKingdomsType().equals(ob.getKingdomsType()) && verify[i+z][j+z]!=1){
                                                counter++;
                                            }
                                        }
                                    }
                                }
                                if(counter==3){
                                    nvoltecompletato++;
                                    verify[Iiniz][Jiniz]=1;
                                    verify[Iiniz+1][Jiniz+1]=1;
                                    verify[Iiniz+2][Jiniz+2]=1;
                                }
                            }
                        }
                    }
                }
            }
            return nvoltecompletato*(ob.getPoints());

        }else{

            for(i=0;i<MAX_ROWS;i++){
                for(j=0;j<MAX_COLS;j++){
                    verify[i][j]=0;
                }
            }
            for(i=0;i<MAX_ROWS;i++){
                for(j=0;j<MAX_COLS;j++){
                    Iiniz=i;
                    Jiniz=j;
                    if( board[i][j]!=null){
                        if(board[i][j].getKingdomsType().equals(ob.getSecondColor()) && verify[i][j]!=1){
                            counter=1;
                            if(ob.getCorner()==0){
                                for(z=1;z<=3;z++){
                                    if(i-z>=0 && j-1>=0  && z!=2) {
                                        if (board[i-z][j-1]!=null) {
                                            if( board[i - z][j - 1].getKingdomsType().equals(ob.getKingdomsType())){
                                                counter++;
                                            }
                                        }
                                    }
                                }
                                if(counter==3){
                                    nvoltecompletato++;
                                    verify[Iiniz][Jiniz]=1;
                                    verify[Iiniz-1][Jiniz-1]=1;
                                    verify[Iiniz-2][Jiniz-1]=1;
                                }
                            }else if(ob.getCorner()==1){
                                for(z=1;z<=3;z++){
                                    if(i-z>=0 && j+1<MAX_COLS  && z!=2) {
                                        if (board[i-z][j+1]!=null) {
                                            if(board[i - z][j + 1].getKingdomsType().equals(ob.getKingdomsType())){
                                                counter++;
                                            }
                                        }
                                    }
                                }
                                if(counter==3){
                                    nvoltecompletato++;
                                    verify[Iiniz][Jiniz]=1;
                                    verify[Iiniz-1][Jiniz+1]=1;
                                    verify[Iiniz-2][Jiniz+1]=1;
                                }
                            }else if(ob.getCorner()==2){
                                for(z=1;z<=3;z++){
                                    if(i+z<MAX_ROWS && j+1<MAX_COLS && z!=2) {
                                        if ( board[i+z][j+1]!=null) {
                                            if( board[i + z][j + 1].getKingdomsType().equals(ob.getKingdomsType())){
                                                counter++;
                                            }
                                        }
                                    }
                                }
                                if(counter==3){
                                    nvoltecompletato++;
                                    verify[Iiniz][Jiniz]=1;
                                    verify[Iiniz+1][Jiniz+1]=1;
                                    verify[Iiniz+2][Jiniz+1]=1;
                                }
                            }else{
                                for(z=1;z<=3;z++){
                                    if(i+z<MAX_ROWS && j-1>=0  && z!=2) {
                                        if (board[i+z][j-1]!=null) {
                                            if( board[i + z][j - 1].getKingdomsType().equals(ob.getKingdomsType())){
                                                counter++;
                                            }
                                        }
                                    }
                                }
                                if(counter==3){
                                    nvoltecompletato++;
                                    verify[Iiniz][Jiniz]=1;
                                    verify[Iiniz+1][Jiniz-1]=1;
                                    verify[Iiniz+2][Jiniz-1]=1;
                                }
                            }
                        }
                    }
                }
            }

            return nvoltecompletato*(ob.getPoints());
        }
    }
    /**
     * Places a card on the board.
     *
     * @param c    The card to be placed.


     * @param face The orientation of the card (true if face up, false if face down).
     * @return 0 if the operation is successful.
     * @throws IllegalArgumentException If the coordinates are out of bounds, the position is already occupied,
     *                                  there are no cards to attach to, or if the card requirements are not met.
     */
    public void placeStartingCard(StartingCard c, boolean face){
        if (!face) {
            c.rotate();
        }
        board[40][40] = c;
        increaseItemCount(c);

    }
    public void placeCard(PlayableCard c, int i, int j, boolean face) {
        if (i < 0 || i >= MAX_ROWS || j < 0 || j >= MAX_COLS) {
            throw new CoordinatesOutOfBoundsException();

        }
    /*    if(board[i][j] != null ||board[i+1][j]!=null || board[i-1][j]!=null || board[i][j+1]!=null || board[i][j-1]!=null){
            throw new PositionOccupiedException();

        }

     */
        if(board[i][j] != null){
            throw new PositionOccupiedException();
        }
        if(i+1<MAX_ROWS){
            if(board[i+1][j]!=null)
                throw new PositionOccupiedException();
        }
        if(i-1>=0){
            if(board[i-1][j]!=null)
                throw new PositionOccupiedException();
        }
        if(j+1<MAX_COLS){
            if(board[i][j+1]!=null)
                throw new PositionOccupiedException();
        }
        if(j-1<MAX_ROWS){
            if(board[i][j-1]!=null)
                throw new PositionOccupiedException();
        }

        if (!face) {
            c.rotate();
        }
        int check=0;
        //    if((board[i+1][j+1]==null && board[i+1][j-1]==null && board[i-1][j+1]==null && board[i-1][j-1]==null ){
        //        throw new NoCardsAvailableException();
        //    }
        if(i+1<MAX_ROWS && j+1<MAX_COLS){
            checkCornerVisibility(i + 1, j + 1,0);
            if(board[i+1][j+1]!=null){
                check=1;
            }
        }
        if(i+1<MAX_ROWS && j-1>=0){
            checkCornerVisibility(i + 1, j - 1,1);
            if(board[i+1][j-1]!=null){
                check=1;
            }
        }
        if(i-1>=0 && j+1<MAX_COLS){
            checkCornerVisibility(i - 1, j + 1,3);
            if(board[i-1][j+1]!=null){
                check=1;
            }
        }
        if(i-1>=0 && j-1>=0){
            checkCornerVisibility(i - 1, j - 1,2);
            if(board[i-1][j-1]!=null){
                check=1;
            }
        }
        if(check==0){
            throw new NoCardsAvailableException();
        }


        if(!checkRequirements(c.getRequirements()))
            throw new RequirementsNotMetException();


        board[i][j] = c;
        increaseItemCount(c);
        updateItemCount(i, j);
        giveCardPoints(c, i, j);
        player.removeCard(c);
    }
    public Player getPlayer(){
        return player;
    }
    public PlayableCard[][] getBoard() {
        return board;
    }
    public Map<CornerItem, Integer> getAvailableItems() {
        return availableItems;
    }

}