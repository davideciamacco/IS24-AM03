package it.polimi.ingsw.is24am03;
import java.util.ArrayList;
import java.util.Collections;

public class Game implements GameInterface {
    int gameId;
    ArrayList<Player> players;
    int numPlayers;
    GoldDeck goldDeck;
    ResourceDeck resourceDeck;
    StartingDeck startingDeck;
    ObjectiveDeck objectiveDeck;
    ArrayList<PlayableCard> tableCards;
    ArrayList<ObjectiveCard> commonObjective;
    ArrayList<Color> availableColors;
    GestoreRound round;
    State gameState;

    public Game(int gId, int nPlayers) {
        this.gameId = gId;
        this.gameState = State.PREPARATION;
        this.numPlayers = nPlayers;
        this.tableCards = null;
        this.resourceDeck = null;
        this.goldDeck = null;
        this.startingDeck = null;
        this.objectiveDeck = null;
        this.players = null;
        this.round = new RoundManager();
        this.availableColors = new ArrayList<Color>(List.of(Color.RED, "GREEN", Color.BLUE, Color.YELLOW));
        this.commonObjective = null;
    }

    public void startGame() {
        setOrder();
        setCommonObjective();
        startingDeck.shuffle();
        objectiveDeck.shuffle();
        goldDeck.shuffle();
        resourceDeck.shuffle();
        initializeTable();
        distributeCards();
    }

    public void endGame() {
        gameState = State.ENDING;
        giveObjectivePoints();
        checkWinner();
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public ArrayList<Color> getAvailableColors() {
        return availableColors;
    }

    public void setCommonObjective() {
        commonObjective.add(objectiveDeck.draw());
        commonObjective.add(objectiveDeck.draw());
    }

    public void distributeCards() {
        for (Player p : players) {
            p.addCard(resourceDeck.draw());
            p.addCard(resourceDeck.draw());
            p.addCard(goldDeck.draw());
            p.setStartingCard(startingDeck.draw());
        }
    }

    public void initializeTable() {
        tableCards.add(resourceDeck.draw());
        tableCards.add(resourceDeck.draw());
        tableCards.add(goldDeck.draw());
        tableCards.add(goldDeck.draw());
    }


    public ArrayList<Player> checkWinner() {
        int maxPoints = 0;
        int maxObjective = 0;
        ArrayList<Player> winners = new ArrayList<Player>();
        for (Player p : players) {
            if (p.getPoints() > maxPoints)
                maxPoints = p.getPoints();
        }
        for (Player p : players) {
            if (p.getPoints() == maxPoints) {
                winners.add(p);
                if (p.getNumObj() > maxObjective)
                    maxObjective = p.getNumObj();
            }
        }
        for (Player w : winners) {
            if (w.getNumObj() < maxObjective)
                winners.remove(w);
            else
                w.setWinner();
        }
        return winners;
    }


    public void addPlayer(Player P) {
        players.add(P);
    }

    /*public void removePlayer(Player P){
        players.remove(P);
    }*/

    public void giveObjectivePoints() {
        int flag;
        for (Player p : players) {
            flag = p.getPlayerBoard().checkObjective(p.getObjectiveCard());
            p.increaseNumObjective(flag);
            for (ObjectiveCard oc : commonObjective) {
                flag = p.getPlayerBoard().checkObjective(oc);
                p.increaseNumObjective(flag);
            }
        }
    }

    public void drawResources(Player P, ResourcesDeck RD) {
        P.addCard(RD.draw());
        //return???
    }

    public void drawGold(Player P, GoldDeck GD) {
        P.addCard(GD.draw());
        //return???
    }

    public void drawTable(Player P, int choice) {
        if (tableCards.get(choice) == null)
            return;
        switch (choice) {
            case 0:
                P.addCard(tableCards.get(0));
                if (!resourceDeck.isEmpty())
                    tableCards.set(0, resourceDeck.draw());
                else if (!goldDeck.isEmpty())
                    tableCards.set(0, goldDeck.draw());
                else
                    tableCards.set(0, null);
                break;

            case 1:
                P.addCard(tableCards.get(1));
                if (!resourceDeck.isEmpty())
                    tableCards.set(1, resourceDeck.draw());
                else if (!goldDeck.isEmpty())
                    tableCards.set(1, goldDeck.draw());
                else
                    tableCards.set(1, null);
                break;

            case 2:
                P.addCard(tableCards.get(2));
                if (!goldDeck.isEmpty())
                    tableCards.set(2, goldDeck.draw());
                else if (!resourceDeck.isEmpty())
                    tableCards.set(2, resourceDeck.draw());
                else
                    tableCards.set(2, null);
                break;

            case 3:
                P.addCard(tableCards.get(3));
                if (!resourceDeck.isEmpty())
                    tableCards.set(3, resourceDeck.draw());
                else if (!goldDeck.isEmpty())
                    tableCards.set(3, goldDeck.draw());
                else
                    tableCards.set(3, null);
                break;
        }

    }

    public void placeCard(Player P, PlayableCard c, int i, int j, boolean face) {
        P.getPlayerBoard().placeCard(c, i, j, face);
    }

    public void sendMessage(Player sender, Player receiver, String message) {

    }

    public void sendMessageAll(Player sender, String message) {

    }

    public void setOrder() {
        Collections.shuffle(players);
    }
}
