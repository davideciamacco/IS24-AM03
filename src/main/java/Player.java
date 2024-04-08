public class Player {
    private String nickname;
    private Color pawncolor;
    private ArrayList<PlayableCard> hand;
    private ObjectiveCard objective;
    private StartingCard startingCard;
    private boolean winner,firstPlayer;
    private int points,numObj;
    private playerBoard PlayerBoard;
    // in startgame verranno pescate le carte da aggiungere alla mano, cosi come per altri attributi che inzialmente risultano null
    public Player(String nickname, Color pawncolor,boolean firstPlayer) {
        this.nickname = nickname;
        this.pawncolor = pawncolor;
        this.hand = null;
        this.objective =null;
        this.startingCard = null;
        this.winner = false;
        this.firstPlayer = firstPlayer;
        this.points = 0;
        this.numObj = 0;
        this.playerBoard = null;
    }
    public void setObjective(ObjectiveCard objective) {
        this.objective = objective;
    }
    public void setStartingCard(StartingCard startingCard) {

    }
    public void setWinner(boolean winner) {
        this.winner = winner;
    }
    //inizialmente playerboard è null, quando viene istanzianata nella rispettiva classe viene assegnata al player attraverso metodo set
    public void setPlayerBoard(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }
    // Metodo per aggiungere una carta alla mano del giocatore
    public void addCard(PlayableCard c) {
        hand.add(c);
    }
    // Aggiunge punti al giocatore basandosi su una carta giocabile
    public void addPoints(PlayableCard card) {
        int pointsToAdd = card.getPoints();
        points=points+pointsToAdd;
    }
    // Getter per hand
    public ArrayList<PlayableCard> getHand() {
        return hand;
    }
    public int getPoints() {
        return points;
    }
}
