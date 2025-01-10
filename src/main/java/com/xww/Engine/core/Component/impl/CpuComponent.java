package com.xww.Engine.core.Component.impl;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.DebugSetting;

import java.awt.*;

@Deprecated
public class CpuComponent extends FreeComponent {
    public CpuComponent() {
        super(Vector.build(400, 10), GameFrame.PositionType.Screen, Vector.Zero(), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE, -1);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        g.setColor(DebugSetting.DebugInfoColor);
        Vector drawPosition = this.getDrawPosition();
        g.drawString("memory: " + (int) (Runtime.getRuntime().totalMemory() / 1024 / 1024) + "M/" + (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024) + "M", drawPosition.getX(), drawPosition.getY() + 30);

    }
}
