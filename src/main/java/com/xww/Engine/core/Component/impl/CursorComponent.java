package com.xww.Engine.core.Component.impl;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.DebugSetting;

import java.awt.*;

public class CursorComponent extends FreeComponent {
    public CursorComponent() {
        super(Vector.Zero(), GameFrame.PositionType.Screen, Vector.Zero(), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE, -1, Integer.MAX_VALUE, true);
        Timer updatePositionTimer = new Timer(0, (obj)->{
            Point location = MouseInfo.getPointerInfo().getLocation();
            this.worldPosition = Vector.build(location.x, location.y - 29).sub_to_self(ScreenInfoComponent.screen_position);
        }, this);
        updatePositionTimer.setRun_times(Timer.INFINITE_TIMES);
        this.addTimer(updatePositionTimer);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
    }

    @Override
    protected void showDebugInfo(Graphics g) {
        Vector drawPosition = this.getDrawPosition();
        g.setColor(DebugSetting.DebugInfoColor);
        g.drawString(drawPosition.toString(), drawPosition.getX(), drawPosition.getY());
    }
}
