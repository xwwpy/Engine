package com.xww.NewEngine.core.Component.impl;

import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Component.FreeComponent;
import com.xww.NewEngine.core.Timer.Timer;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;
import com.xww.NewEngine.setting.DebugSetting;

import java.awt.*;

public class CursorComponent extends FreeComponent {
    public CursorComponent() {
        super(Vector.Zero(), GameFrame.PositionType.Screen, Vector.Zero(), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE, -1);
        Timer updatePositionTimer = new Timer(0, (obj)->{
            Point location = MouseInfo.getPointerInfo().getLocation();
            this.worldPosition = Vector.build(location.x, location.y - 29).sub_to_self(ScreenInfoComponent.screen_position);
        }, this);
        updatePositionTimer.setRun_times(Timer.INFINITE_TIMES);
        this.addTimer(updatePositionTimer);
    }

    @Override
    public void on_update(Graphics g) {
        Vector drawPosition = this.getDrawPosition();
        g.setColor(DebugSetting.DebugInfoColor);
        g.drawString(drawPosition.toString(), drawPosition.getX(), drawPosition.getY());
        super.on_update(g);
    }
}
