package com.xww.NewEngine.core.Event.Message;



public class Message {

    public static enum MessageType {
        KeyBoard,
        MousePressed,
        MouseReleased,
        MouseClicked,
        MouseEnter,
        MouseExit, MouseMoved,
    }

    public static enum ColliderType {
        X,
        Y,
        XY,
        NONE
    }

    private final Object message;
    private final MessageType messageType;

    public Message(Object message, MessageType type) {
        this.message = message;
        this.messageType = type;
    }
    public Object getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
