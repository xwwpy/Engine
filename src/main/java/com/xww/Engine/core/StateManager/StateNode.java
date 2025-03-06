package com.xww.Engine.core.StateManager;

import java.awt.*;

public abstract class StateNode {
    public abstract void on_enter();

    public abstract void on_update(Graphics g, Component owner);

    public abstract void on_exit();
}
