import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PlayerBoard {
    private static final int MAX_ROWS = 81;
    private static final int MAX_COLS = 81;
    private Player player;
    private PlayableCard[][] board;
    //la mappa serve per accedere in modo comodo al contenuto degli oggetti presenti sul tavolo del giocatore
    private Map<CornerItem, Integer> availableItems;

    // Costruttore con i parametri
    public PlayerBoard(Player player) {
        this.player = player;
        this.board =null;
        this.availableItems = new HashMap<CornerItem, Integer>();
        for (CornerItem item : CornerItem.values()) {
            availableItems.put(item, 0); // Inizializza il conteggio di ogni oggetto a 0
        }
    }
    //metodo che facilità l implementazione di place card controllando se le sovrapposizioni degli angoli sono permesse
    private void checkCornerVisibility(int x, int y,int corner) {
        if (board[x][y] != null) {
            if (board[x][y].getFace()==true) {
                if (board[x][y].getFrontCorners(corner).getisVisible==false) {
                    throw new IllegalArgumentException("Angolo non visibile");
                }
            } else {
                if (board[x][y].getBackCorners(corner).getisVisible==false) {
                    throw new IllegalArgumentException("Angolo non visibile");
                }
            }
        }
    }
    //nel momento in cui vado ad aggiungere una carta sul tavolo da gioco aggiungo alla mappa i nuovi oggetti che compaiono
    private void increaseItemCount(PlayableCard card) {
        // Aggiorna il conteggio di ciascun oggetto negli angoli o sul retro della carta
        if(card.getFace==true){
            for (int i=0;i<=3;i++) {
                CornerItem item = card.getFrontCorners(i).getItem();
                availableItems.put(item, availableItems.get(item) + 1);
            }
        }else{
            Resources item = card.getKingdomType();
            availableItems.put(item, availableItems.get(item) + 1);
        }
    }
// al contrario di increaseItemCount, questo metodo è incaricato di eliminare dalla mappa gli oggetti che vengono coperti in seguito al piazzamento di una card
    private void updateItemCount(int i,int j){
        if(board[i+1][j+1]!=null){
            if(board[i+1][j+1].getFace()==true){
                if(!board[i+1][j+1].getFrontCorners(0).getEmpty()){
                    CornerItem item = board[i+1][j+1].getFrontCorners(0).getItem();
                    availableItems.put(item, availableItems.get(item) -1);
                }
            }
        }
        if(board[i-1][j-1]!=null){
            if(board[i-1][j-1].getFace()==true){
                if(!board[i-1][j-1].getFrontCorners(2).getEmpty()){
                    CornerItem item = board[i-1][j-1].getFrontCorners(2).getItem();
                    availableItems.put(item, availableItems.get(item) -1);
                }
            }
        }
        if(board[i-1][j+1]!=null){
            if(board[i-1][j+1].getFace()==true){
                if(!board[i-1][j+1].getFrontCorners(3).getEmpty()){
                    CornerItem item = board[i-1][j+1].getFrontCorners(3).getItem();
                    availableItems.put(item, availableItems.get(item) -1);
                }
            }
        }
        if(board[i+1][j-1]!=null){
            if(board[i+1][j-1].getFace()==true){
                if(!board[i+1][j-1].getFrontCorners(1).getEmpty()){
                    CornerItem item = board[i+1][j-1].getFrontCorners(1).getItem();
                    availableItems.put(item, availableItems.get(item) -1);
                }
            }
        }
    }
// metodo chiamato in presenza dello schieramento di una goldCard ,atto a verificarne i requisiti
    public boolean checkRequirements(ArrayList<Resources> cardRequirements)
    {
        Map<Resources, Integer> itemRequired = new HashMap<Resources, Integer>();
        for(Resources item: Resources.values())
            itemRequired.put(item, 0);
        for(Resources item: cardRequirements)
            itemRequired.put(item, itemRequired.get(item) + 1);
        for(Resources item: itemRequired)
        {
            if(availableItems.get(item)<itemRequired.get(item))
                return false;
        }
        return true;
    }
//in base al tipo di  carta giocata risorsa/oro , attribuisce in base ai vincoli i punti al giocatore
    public void giveCardPoints(PlayableCard c, int i, int j){
        if(c instanceof ResorceCard)
            player.addPoints(c.getPoints());
        else if(c instanceof GoldCard){
            int n=0;
            c = (GoldCard) c;
            if(c.getScoringType==0)
            {
                player.addPoints(availableItems.get(c.getObject()));
            }
            else
            {
                if(board[i-1][j-1]!=null)
                    n++;
                if(board[i+1][j+1]!=null)
                    n++;
                if(board[i-1][j+1]!=null)
                    n++;
                if(board[i+1][j-1]!=null)
                    n++;
                player.addPoints(n*c.getPoints());
            }
        }
    }
    // Metodo per posizionare una carta sulla board
    public int placeCard(PlayableCard c, int i, int j, boolean face) {
        if (i < 0 || i >= MAX_ROWS || j < 0 || j >= MAX_COLS) {
            throw new IllegalArgumentException("Coordinate fuori dai limiti della board");
        }
        // se siamo arrivati fin qui, la posizione della carta sulla board è ammissibile
        //controlliamo ora che si possa effettivamente giocarla in quella posizione
        if(board[i][j] != null ||board[i+1][j]!=null || board[i-1][j]!=null || board[i][j+1]!=null || board[i][j-1]!=null){
            throw new IllegalArgumentException("Posizione già occupata/non disponibile");
        }
        //almeno una carta ad angolo deve essere presente
        if(board[i+1][j+1]==null && board[i+1][j-1]==null && board[i-1][j+1]==null && board[i-1][j-1]==null ){
            throw new IllegalArgumentException("non è presente alcuna carta su cui agganciarsi");
        }
        //devo controllare che gli angoli posti in diagonale rispetto a dove voglio mettere la carta siano visibili oppure non ci deve essere la carta
        checkCornerVisibility(i + 1, j + 1,0);
        checkCornerVisibility(i + 1, j - 1,1);
        checkCornerVisibility(i - 1, j + 1,3);
        checkCornerVisibility(i - 1, j - 1,2);
        if(c instanceof GoldCard){
            c = (GoldCard) c;
            if(!checkRequirements(c.getRequirements()))
                throw new IllegalArgumentException("requirements non rispettati");
        }
        if (face==false) {
            c.rotate();
        }
        board[i][j] = c;
        increaseItemCount(c);
        updateItemCount(i, j);
        giveCardPoints(c, i, j);
        return 0; // Ritorna 0 se l'operazione ha successo
    }
}

