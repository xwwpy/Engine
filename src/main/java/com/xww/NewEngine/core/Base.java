package com.xww.NewEngine.core;

import java.awt.*;

public interface Base {
    public abstract void on_create();

    public abstract void on_update(Graphics g);
    public abstract void on_destroy();
}
