
package it.polimi.ingsw.is24am03;

public abstract class Card {
    private int points;
    private int id;

    public Card (int id, int points){
        this.id=id;
        this.points=points;
    }
    public int getPoints(){
        return points;
    }

    public int getId(){
        return id;
    }
}
