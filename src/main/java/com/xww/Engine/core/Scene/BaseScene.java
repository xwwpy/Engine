package com.xww.Engine.core.Scene;

import com.xww.Engine.core.Event.Message.Message;

import java.awt.*;

public interface BaseScene {
    public abstract void on_enter();
    public abstract void on_update(Graphics g);
    public abstract void processMessage(Message message);
    public abstract void reset();
    public abstract void on_exit();
}
