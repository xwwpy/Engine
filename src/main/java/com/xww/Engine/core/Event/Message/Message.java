package com.xww.Engine.core.Event.Message;



public class Message {

    public enum MessageType {
        KeyBoard,
        MousePressed,
        MouseReleased,
        MouseClicked,
        MouseEnter,
        MouseExit,
        MouseMoved,
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
