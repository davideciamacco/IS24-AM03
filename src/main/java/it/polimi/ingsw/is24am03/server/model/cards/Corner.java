
package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;

import java.io.Serial;
import java.io.Serializable;

public class Corner implements Serializable {
    private boolean isVisible;
    private boolean isEmpty;
    private CornerItem item;
    public Corner (String val1){
        switch (val1){
            case "A":
                this.isVisible = true;
                this.isEmpty = false;
                this.item=CornerItem.ANIMAL;
                break;

            case "F":
                this.isVisible = true;
                this.isEmpty = false;
                this.item=CornerItem.FUNGI;
                break;

            case "I":
                this.isVisible = true;
                this.isEmpty = false;
                this.item=CornerItem.INSECT;
                break;

            case "P":
                this.isVisible = true;
                this.isEmpty = false;
                this.item=CornerItem.PLANT;
                break;

            case "M":
                this.isVisible = true;
                this.isEmpty = false;
                this.item=CornerItem.MANUSCRIPT;
                break;

            case "K":
                this.isVisible = true;
                this.isEmpty = false;
                this.item=CornerItem.INKWELL;
                break;

            case "Q":
                this.isVisible = true;
                this.isEmpty = false;
                this.item=CornerItem.QUILL;
                break;

            case "E":
                this.isVisible = true;
                this.isEmpty = true;
                this.item=CornerItem.EMPTY;
                break;

            case "X":
                this.isVisible = false;
                this.isEmpty = true;
                this.item=CornerItem.EMPTY;
                break;

            default:
                throw new IllegalArgumentException("Opzione non valida: " + val1);
        }

    }

    public CornerItem getItem(){
        return item;
    }
    public boolean isVisible() {
        return isVisible;
    }
    public boolean isEmpty() {
        return isEmpty;
    }
}
