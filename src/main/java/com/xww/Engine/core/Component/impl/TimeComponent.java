package com.xww.Engine.core.Component.impl;

import com.xww.Engine.Utils.StringUtils;
import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Event.TimeEventManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.FrameSetting;

import java.awt.*;

public class TimeComponent extends FreeComponent {
    public TimeComponent() {
        super(Vector.build(FrameSetting.DEFAULT_WIDTH - 200, 30), GameFrame.PositionType.Screen, Vector.Zero(), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE, CollisionDefaultConstValue.noCollisionChecking, CollisionDefaultConstValue.noCollisionChecking, 10, true, false);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
    }

    @Override
    protected void showDebugInfo(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawString("运行时间: " + StringUtils.getTargetAccuracy(TimeEventManager.getRunTime(), 3), this.getDrawPosition().getX(), this.getDrawPosition().getY());
    }
}
