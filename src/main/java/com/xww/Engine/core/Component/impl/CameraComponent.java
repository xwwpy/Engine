package com.xww.Engine.core.Component.impl;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.Camera;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.DebugSetting;

import java.awt.*;

public class CameraComponent extends FreeComponent {
    public CameraComponent() {
        super(Vector.build(300, 20), GameFrame.PositionType.Screen, Vector.Zero(), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE, -1, Integer.MAX_VALUE, true);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
    }

    @Override
    protected void showDebugInfo(Graphics g) {
        g.setColor(DebugSetting.DebugInfoColor);
        g.drawString("camera position: " + Camera.camera_position, this.getDrawPosition().getX(), this.getDrawPosition().getY());
        g.drawString("camera velocity: " + Camera.velocity, this.getDrawPosition().getX(), this.getDrawPosition().getY() + 20);
        g.drawString("camera acceleration: " + Camera.acceleration, this.getDrawPosition().getX(), this.getDrawPosition().getY() + 40);
    }
}
