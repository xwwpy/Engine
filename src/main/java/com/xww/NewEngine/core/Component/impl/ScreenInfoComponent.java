package com.xww.NewEngine.core.Component.impl;

import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Component.FreeComponent;
import com.xww.NewEngine.core.Timer.Timer;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;
import com.xww.NewEngine.setting.DebugSetting;

import java.awt.*;

public class ScreenInfoComponent extends FreeComponent {
    public static Vector screen_position = Vector.Zero();
    public ScreenInfoComponent() {
        super(Vector.build(30, 40), GameFrame.PositionType.Screen, Vector.Zero(), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE, -1);
        Timer timer = new Timer(0, (obj) ->{
            // 更新屏幕坐标
            Point screen_position = GameFrame.context.getLocationOnScreen();
            ScreenInfoComponent.screen_position = new Vector(screen_position.x, screen_position.y);
        }, this);
        timer.setRun_times(Timer.INFINITE_TIMES);
        this.addTimer(timer);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        g.setColor(DebugSetting.DebugInfoColor);
        Vector drawPosition = this.getDrawPosition();
        g.drawString("screen position: " + screen_position.toString(), drawPosition.getX(), drawPosition.getY());
    }
}
