package it.polimi.ingsw.is24am03.messages;

/**
 * This message represents the heartbeat used to check the TCP connection between client and server
 */
public class HeartbeatMessage extends Message {

    /**
     * Constructor of a HeartBeatMessage
     */
        public HeartbeatMessage(String heartbeat) {
            super(MessageType.HEARTBEAT);
        }
    }

