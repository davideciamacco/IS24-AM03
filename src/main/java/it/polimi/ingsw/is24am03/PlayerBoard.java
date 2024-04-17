package it.polimi.ingsw.is24am03;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
/**
 * Represents the board of a player in the game.
 ok
 */

public class PlayerBoard {
    private static final int MAX_ROWS = 81;
    private static final int MAX_COLS = 81;
    private Player player;
    private PlayableCard[][] board;
    private Map<CornerItem, Integer> availableItems;
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
            if (board[x][y].getFace()==true) {
                if (board[x][y].getFrontCorner(corner).isVisible()==false) {
                    throw new IllegalArgumentException("Corner not visible");

                }
            } else {
                if (board[x][y].getBackCorner(corner).isVisible()==false) {
                    throw new IllegalArgumentException("Corner not visible");

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
        if(card.getFace()==true){
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
        if(board[i+1][j+1]!=null){
            if(board[i+1][j+1].getFace()==true){
                if(!board[i+1][j+1].getFrontCorner(0).isEmpty()){
                    CornerItem item = board[i+1][j+1].getFrontCorner(0).getItem();
                    availableItems.put(item, availableItems.get(item) -1);
                }
            }
        }
        if(board[i-1][j-1]!=null){
            if(board[i-1][j-1].getFace()==true){
                if(!board[i-1][j-1].getFrontCorner(2).isEmpty()){
                    CornerItem item = board[i-1][j-1].getFrontCorner(2).getItem();
                    availableItems.put(item, availableItems.get(item) -1);
                }
            }
        }
        if(board[i-1][j+1]!=null){
            if(board[i-1][j+1].getFace()==true){
                if(!board[i-1][j+1].getFrontCorner(3).isEmpty()){
                    CornerItem item = board[i-1][j+1].getFrontCorner(3).getItem();
                    availableItems.put(item, availableItems.get(item) -1);
                }
            }
        }
        if(board[i+1][j-1]!=null){
            if(board[i+1][j-1].getFace()==true){
                if(!board[i+1][j-1].getFrontCorner(1).isEmpty()){
                    CornerItem item = board[i+1][j-1].getFrontCorner(1).getItem();
                    availableItems.put(item, availableItems.get(item) -1);
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
    public boolean checkRequirements(ArrayList<Resources> cardRequirements)
    {
        Map<Resources, Integer> itemRequired = new HashMap<Resources, Integer>();
        for(Resources item: Resources.values())
            itemRequired.put(item, 0);
        for(Resources item: cardRequirements)
            itemRequired.put(item, itemRequired.get(item) + 1);
        for(Resources item: itemRequired.keySet())
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
        int type,scorringType,n=0;
        type= c.getType();
        if(type==1){
            //resource card
            player.addPoints(c);
        }else{
            scorringType=c.getScoringType();
            if(scorringType==0){
                n=availableItems.get(c.getObject());
                for(i=0; i<n; i++)
                    player.addPoints(c);
            }else{
                if(board[i-1][j-1]!=null)
                    n++;
                if(board[i+1][j+1]!=null)
                    n++;
                if(board[i-1][j+1]!=null)
                    n++;
                if(board[i+1][j-1]!=null)
                    n++;
                for(i=0; i<n; i++)
                    player.addPoints(c);
            }
        }
    }
    /**
     * Checks how many times the specified objective has been completed on the player's board.
     *
     * @param ob The objective card to check.
     * @return The number of times the objective has been completed.
     */
    public int checkObjective(ObjctiveCard ob){
        int n;
        int[][] verify = new int[MAX_ROWS][MAX_COLS];
        int i,j,z,nvoltecompletato=0,counter=1, Iiniz,Jiniz;
        if(ob.getType().equals(ObjectiveType.ITEM)){
                if(ob.getTypeList()==1){
                    //GET REQUIREMENT VA MESSO A OBJECTIVE CARD INSIEME AL ATTRIBUTO requirements
                    n=availableItems.get(ob.getRequirement())/2;
                    return n;
                }else if(ob.getTypeList()==2){
                    n=availableItems.get(CornerItem.QUILL);
                    if(availableItems.get(CornerItem.INKWELL)<n){
                        n=availableItems.get(CornerItem.INKWELL);
                    }
                    if(availableItems.get(CornerItem.MANUSCRIPT)<n) {
                        n=availableItems.get(CornerItem.MANUSCRIPT);
                    }
                    return n;
                }else{
                    //caso di carta obbiettivo che richiede una risorsa 3 volte
                    n=availableItems.get(ob.getRequirement())/3;
                    return n;
                }
        }
        else if(ob.getType().equals(ObjectiveType.PATTERNDIAGONAL)){
                for(i=0;i<MAX_ROWS;i++){
                    for(j=0;j<MAX_COLS;j++){
                        verify[i][j]=0;
                    }
                }
                if(ob.getDirection()==true){
                    for(i=0;i<MAX_ROWS;i++){
                        for(j=0;j<MAX_COLS;j++){
                            Iiniz=i;
                            Jiniz=j;
                            if(board[i][j].getKingdomsType().equals(ob.getKingdomsType()) && verify[i][j]!=1){
                                counter=1;
                                for(z=1;z<=2;z++){
                                    if(i-z>=0 && j+z<MAX_COLS){
                                        if(board[i-z][j+z].getKingdomsType().equals(ob.getKingdomsType()) && verify[i-z][j+z]!=1){
                                            counter++;
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
                }else{
                    for(i=0;i<MAX_ROWS;i++){
                        for(j=0;j<MAX_COLS;j++){
                            Iiniz=i;
                            Jiniz=j;
                            if(board[i][j].getKingdomsType().equals(ob.getKingdomsType()) && verify[i][j]!=1){
                                counter=1;
                                for(z=1;z<=2;z++){
                                    if(i+z<MAX_ROWS && j+z<MAX_COLS){
                                        if(board[i+z][j+z].getKingdomsType().equals(ob.getKingdomsType()) && verify[i+z][j+z]!=1){
                                            counter++;
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
                return nvoltecompletato;

        }else{
                for(i=0;i<MAX_ROWS;i++){
                    for(j=0;j<MAX_COLS;j++){
                        verify[i][j]=0;
                    }
                }

                if(ob.getDirection()==true){
                    for(i=0;i<MAX_ROWS;i++){
                        for(j=0;j<MAX_COLS;j++){
                            Iiniz=i;
                            Jiniz=j;
                            if(board[i][j].getKingdomsType().equals(ob.getKingdomsType()) && verify[i][j]!=1){
                                counter=1;
                                for(z=1;z<=2;z++){
                                    if(i-z>=0 && j+z<MAX_COLS){
                                        if(board[i-z][j+z].getKingdomsType().equals(ob.getKingdomsType()) && verify[i-z][j+z]!=1){
                                            counter++;
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
                }else{
                    for(i=0;i<MAX_ROWS;i++){
                        for(j=0;j<MAX_COLS;j++){
                            Iiniz=i;
                            Jiniz=j;
                            if(board[i][j].getKingdomsType().equals(ob.getKingdomsType()) && verify[i][j]!=1){
                                counter=1;
                                for(z=1;z<=2;z++){
                                    if(i+z<MAX_ROWS && j+z<MAX_COLS){
                                        if(board[i+z][j+z].getKingdomsType().equals(ob.getKingdomsType()) && verify[i+z][j+z]!=1){
                                            counter++;
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
                return nvoltecompletato;
        }
    }
    /**
     * Places a card on the board.
     *
     * @param c    The card to be placed.
     * @param i    The row index where the card will be placed.
     * @param j    The column index where the card will be placed.
     * @param face The orientation of the card (true if face up, false if face down).
     * @return 0 if the operation is successful.
     * @throws IllegalArgumentException If the coordinates are out of bounds, the position is already occupied,
     *                                  there are no cards to attach to, or if the card requirements are not met.
     */
    public int placeCard(PlayableCard c, int i, int j, boolean face) {
        if (i < 0 || i >= MAX_ROWS || j < 0 || j >= MAX_COLS) {
            throw new IllegalArgumentException("Coordinates out of board limits");

        }
        if(board[i][j] != null ||board[i+1][j]!=null || board[i-1][j]!=null || board[i][j+1]!=null || board[i][j-1]!=null){
            throw new IllegalArgumentException("Position already occupied/unavailable");

        }
        //almeno una carta ad angolo deve essere presente
        if(board[i+1][j+1]==null && board[i+1][j-1]==null && board[i-1][j+1]==null && board[i-1][j-1]==null ){
            throw new IllegalArgumentException("There are no cards available to attach to");

        }
        checkCornerVisibility(i + 1, j + 1,0);
        checkCornerVisibility(i + 1, j - 1,1);
        checkCornerVisibility(i - 1, j + 1,3);
        checkCornerVisibility(i - 1, j - 1,2);

        if(!checkRequirements(c.getRequirements()))
            throw new IllegalArgumentException("Requirements not met");

        if (face==false) {
            c.rotate();
        }
        board[i][j] = c;
        increaseItemCount(c);
        updateItemCount(i, j);
        giveCardPoints(c, i, j);
        return 0; // return 0 if the operation has been done
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

