
package it.polimi.ingsw.is24am03.server.model.enums;

import java.io.Serializable;

/**
 * This enumeration lists the possible objects and kingdoms/resources that appear in a card's corner.
 * The empty value stands for an empty corner.
 */
public enum CornerItem implements Serializable {

    ANIMAL,
    FUNGI,
    INSECT,
    PLANT,
    INKWELL,
    MANUSCRIPT,
    QUILL,
    EMPTY,
}
