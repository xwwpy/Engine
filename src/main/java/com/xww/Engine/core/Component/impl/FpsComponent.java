package com.xww.Engine.core.Component.impl;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;

public class FpsComponent extends FreeComponent {
    private long current_fps = GameFrame.context.current_fps;
    public FpsComponent() {
        super(Vector.build(10, 10), GameFrame.PositionType.Screen, Vector.build(0, 0), AnchorMode.LeftTop, Vector.Zero(), Vector.build(0, 0), Integer.MAX_VALUE, -1, Integer.MAX_VALUE, true);
        Timer fpsTimer = new Timer(1000, (obj)->{
            ((FpsComponent) obj).current_fps = GameFrame.context.current_fps;
        }, this);
        fpsTimer.setRun_times(Timer.INFINITE_TIMES);
        this.addTimer(fpsTimer);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
    }

    @Override
    protected void showDebugInfo(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawString("FPS: " + current_fps, this.getDrawPosition().getX(), this.getDrawPosition().getY());
    }
}
