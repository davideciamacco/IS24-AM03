
package it.polimi.ingsw.is24am03.server.model.cards;

import java.io.Serializable;

public abstract class Card implements Serializable {
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
