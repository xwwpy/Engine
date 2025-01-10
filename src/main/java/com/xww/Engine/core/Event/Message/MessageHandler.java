package com.xww.Engine.core.Event.Message;

public interface MessageHandler {

    public abstract void handle(Message message);

    /**
     *
     * @return 当返回false时，表示消息处理完毕，可以移除该消息处理器
     */
    public abstract boolean checkValid();
}
