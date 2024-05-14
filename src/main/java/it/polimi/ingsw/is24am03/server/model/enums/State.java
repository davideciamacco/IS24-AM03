package it.polimi.ingsw.is24am03.server.model.enums;

import java.io.Serializable;

/**
 * This enumeration lists the potential game's states.
 */

public enum State implements Serializable {
    WAITING,
    STARTING,
    COLOR,
    OBJECTIVE,
    DRAWING,
    PLAYING,
    ENDING
}