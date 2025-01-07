package com.xww.NewEngine.core.Scene;

import java.awt.*;

public interface BaseScene {
    public abstract void on_enter();
    public abstract void on_update(Graphics g);
    public abstract void reset();
    public abstract void on_exit();
}
