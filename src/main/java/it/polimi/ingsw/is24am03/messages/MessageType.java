package it.polimi.ingsw.is24am03.messages;
/**
 * Enum representing all the type of messages sent over the network
 */
public enum MessageType {

    //create game, confirm game created
    CREATE_GAME,
    CONFIRM_GAME,

    //join game, confirm join, notify joined player
    JOIN_GAME,
    CONFIRM_JOIN,
    JOINED_PLAYER,

    //pick color, confirm pick
    PICK_COLOR,  //Andrea
    CONFIRM_PICK,  //Andrea
    AVAILABLE_COLORS,
    FINAL_COLORS,

    //notify num players reached
    NOTIFY_NUM_PLAYERS_REACHED,  //Davide


    //choose starting card, confirm, notify choice is notify player board message
    CHOOSE_STARTING_CARD_SIDE, //Simone
    CONFIRM_CHOOSE_SIDE, //Simone
    UPDATE_PLAYER_BOARD,


    //choose objective, comfirm choice, notify choice
    CHOOSE_OBJECTIVE, //Davide
    CHOICE_OBJECTIVE,
    CONFIRM_CHOOSE_OBJECTIVE, //Davide
    UPDATE_PERSONAL_OBJECTIVE, //Simone


    //place card, confirm place + update player board
    PLACE_CARD, //Andrea
    CONFIRM_PLACE, //Andrea


    //notify current player, notify turn order, notify winners
    NOTIFY_CURRENT_PLAYER, //Davide
    NOTIFY_WINNERS, //Andrea
    NOTIFY_ADDITIONAL_ROUND, //Simone
    TURN_ORDER,
    GAME_STATE,


    //draw table, confirm draw table, draw from decks, confirm draw from decks
    DRAW_TABLE, //Simone
    CONFIRM_DRAW_TABLE, //Simone
    DRAW_GOLD, //Davide
    DRAW_RESOURCE, //Davide
    CONFIRM_DRAW, //Davide

    //close connection
    CLOSE_CONNECTION, //Andrea
    CONFIRM_CLOSE_CONNECTION,


    //messaggi per la gestione di rejoin dopo il crash
    REJOIN_GAME, //Davide
    CONFIRM_REJOIN, //Davide
    UPDATE_CRASHED_PLAYER,
    NOTIFY_CRASHED_PLAYER, //Simone
    NOTIFY_REJOINED_PLAYER,

    //messaggi per la chat
    GROUP_CHAT,
    PRIVATE_CHAT,
    CONFIRM_CHAT,

    //update common table + COMMON OBJECTIVES
    UPDATE_COMMON_TABLE,
    COMMON_OBJECTIVE,

    //UPDATE POINTS AND PERSONAL CARDS + FIRST HAND MESSAGE
    UPDATE_POINTS,
    UPDATE_PERSONAL_CARDS,
    FIRST_HAND,









}