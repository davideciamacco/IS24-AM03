package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a corner with a visibility status, an occupancy status, and an item.
 * The item can be of different types such as ANIMAL, FUNGI, INSECT, PLANT, MANUSCRIPT, INKWELL, QUILL, or EMPTY.
 * This class implements Serializable to allow corner objects to be serialized.
 */
public class Corner implements Serializable {
    private boolean isVisible;
    private boolean isEmpty;
    private CornerItem item;

    /**
     * Constructs a Corner with specified properties based on the provided value.
     * The value determines the visibility, occupancy, and type of item in the corner.
     *
     * @param val1 a string representing the type of item in the corner. It can be one of the following:
     *             "A" for ANIMAL, "F" for FUNGI, "I" for INSECT, "P" for PLANT, "M" for MANUSCRIPT,
     *             "K" for INKWELL, "Q" for QUILL, "E" for an empty visible corner, "X" for an empty invisible corner.
     * @throws IllegalArgumentException if the provided value is not valid.
     */
    public Corner(String val1) {
        switch (val1) {
            case "A":
                this.isVisible = true;
                this.isEmpty = false;
                this.item = CornerItem.ANIMAL;
                break;

            case "F":
                this.isVisible = true;
                this.isEmpty = false;
                this.item = CornerItem.FUNGI;
                break;

            case "I":
                this.isVisible = true;
                this.isEmpty = false;
                this.item = CornerItem.INSECT;
                break;

            case "P":
                this.isVisible = true;
                this.isEmpty = false;
                this.item = CornerItem.PLANT;
                break;

            case "M":
                this.isVisible = true;
                this.isEmpty = false;
                this.item = CornerItem.MANUSCRIPT;
                break;

            case "K":
                this.isVisible = true;
                this.isEmpty = false;
                this.item = CornerItem.INKWELL;
                break;

            case "Q":
                this.isVisible = true;
                this.isEmpty = false;
                this.item = CornerItem.QUILL;
                break;

            case "E":
                this.isVisible = true;
                this.isEmpty = true;
                this.item = CornerItem.EMPTY;
                break;

            case "X":
                this.isVisible = false;
                this.isEmpty = true;
                this.item = CornerItem.EMPTY;
                break;

            default:
                throw new IllegalArgumentException("Opzione non valida: " + val1);
        }
    }

    /**
     * Returns the item in the corner.
     *
     * @return the item in the corner
     */
    public CornerItem getItem() {
        return item;
    }

    /**
     * Returns whether the corner is visible.
     *
     * @return true if the corner is visible, false otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Returns whether the corner is empty.
     *
     * @return true if the corner is empty, false otherwise
     */
    public boolean isEmpty() {
        return isEmpty;
    }
}
