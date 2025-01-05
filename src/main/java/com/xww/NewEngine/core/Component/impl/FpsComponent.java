package com.xww.NewEngine.core.Component.impl;

import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Component.FreeComponent;
import com.xww.NewEngine.core.Timer.Timer;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;

import java.awt.*;

public class FpsComponent extends FreeComponent {
    private int current_fps = GameFrame.context.current_fps;
    public FpsComponent() {
        super(Vector.build(10, 10), GameFrame.PositionType.Screen, Vector.build(0, 0), AnchorMode.LeftTop, Vector.Zero(), Vector.build(0, 0), Integer.MAX_VALUE, -1);
        Timer fpsTimer = new Timer(1000, (obj)->{
            ((FpsComponent) obj).current_fps = GameFrame.context.current_fps;
        }, this);
        fpsTimer.setRun_times(Timer.INFINITE_TIMES);
        this.addTimer(fpsTimer);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        g.setColor(Color.GREEN);
        g.drawString("FPS: " + current_fps, this.getDrawPosition().getX(), this.getDrawPosition().getY());
    }
}
