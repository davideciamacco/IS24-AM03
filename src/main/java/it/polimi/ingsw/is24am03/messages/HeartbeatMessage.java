package it.polimi.ingsw.is24am03.messages;

public class HeartbeatMessage extends Message {
    public HeartbeatMessage(String heartbeat) {
        super(MessageType.HEARTBEAT);
    }
}
