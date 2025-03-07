package com.xww.Engine.core.Component.impl;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.FrameSetting;

import java.awt.*;

public class GameSpeedComponent extends FreeComponent {
    public GameSpeedComponent() {
        super(Vector.build(750, 20), GameFrame.PositionType.Screen, Vector.Zero(), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE, CollisionDefaultConstValue.noCollisionChecking, CollisionDefaultConstValue.noCollisionChecking, Integer.MAX_VALUE, true, false);
    }

    @Override
    protected void showDebugInfo(Graphics g) {
        g.setColor(Color.green);
        g.drawString("current gameSpeed: " + FrameSetting.timeSpeed, this.getDrawPosition().getX(), this.getDrawPosition().getY());
    }
}
