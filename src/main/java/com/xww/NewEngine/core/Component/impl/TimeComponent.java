package com.xww.NewEngine.core.Component.impl;

import com.xww.NewEngine.Utils.StringUtils;
import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Component.FreeComponent;
import com.xww.NewEngine.core.Event.TimeEventManager;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;
import com.xww.NewEngine.setting.FrameSetting;

import java.awt.*;
import java.time.LocalDateTime;

public class TimeComponent extends FreeComponent {
    public TimeComponent() {
        super(Vector.build(FrameSetting.DEFAULT_WIDTH - 200, 30), GameFrame.PositionType.Screen, Vector.Zero(), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 1, -1);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        g.setColor(Color.GREEN);
        g.drawString("运行时间: " + StringUtils.getTargetAccuracy(TimeEventManager.getRunTime(), 3), this.getDrawPosition().getX(), this.getDrawPosition().getY());

    }
}
