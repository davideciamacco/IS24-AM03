package it.polimi.ingsw.is24am03.messages;
/**
 * Enum representing all the type of messages sent over the network
 */
public enum MessageType {

    CREATE_GAME,
    CONFIRM_GAME,

    JOIN_GAME,
    CONFIRM_JOIN,
    NOTIFY_JOINED_PLAYER, //Simone
    PICK_COLOR,  //Andrea
    CONFIRM_PICK,  //Andrea
    NOTIFY_NUM_PLAYERS_REACHED,  //Davide
    CHOOSE_STARTING_CARD_SIDE, //Simone
    CONFIRM_CHOOSE_SIDE, //Simone
    CHOOSE_OBJECTIVE, //Davide
    CONFIRM_CHOOSE_OBJECTIVE, //Davide
    UPDATE_COMMON_BOARD, //Andrea
    UPDATE_PERSONAL_OBJECTIVE, //Simone
    PLACE_CARD, //Andrea
    CONFIRM_PLACE, //Andrea
    NOTIFY_CURRENT_PLAYER, //Davide
    DRAW_TABLE, //Simone
    CONFIRM_DRAW_TABLE, //Simone
    DRAW_GOLD, //Davide
    DRAW_RESOURCE, //Davide
    CONFIRM_DRAW, //Davide
    NOTIFY_WINNERS, //Andrea
    CLOSE_CONNECTION, //Andrea
    NOTIFY_ADDITIONAL_ROUND, //Simone
    SEND_MESSAGE, //Andrea
    CONFIRM_MESSAGE, //Andrea
    NOTIFY_MESSAGE, //Andrea
    REJOIN_GAME, //Davide
    CONFIRM_REJOIN, //Davide
    NOTIFY_CRASHED_PLAYER, //Simone
    NOTIFY_REJOINED_PLAYER, //Simone
}