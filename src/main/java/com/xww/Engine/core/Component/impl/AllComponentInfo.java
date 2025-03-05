package com.xww.Engine.core.Component.impl;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;


public class AllComponentInfo extends FreeComponent {
    public AllComponentInfo() {
        super(Vector.build(600, 20), GameFrame.PositionType.Screen, Vector.Zero(), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE, CollisionDefaultConstValue.noCollisionChecking, CollisionDefaultConstValue.noCollisionChecking, Integer.MAX_VALUE, true, false);
    }

    @Override
    protected void showDebugInfo(Graphics g) {
        g.setColor(Color.green);
        g.drawString("Component Size: " + Component.components.size(), this.getDrawPosition().getX(), this.getDrawPosition().getY());
        g.drawString("AllComponent Size: " + Component.allComponents.size(), this.getDrawPosition().getX(), this.getDrawPosition().getY() + 20);
    }
}
