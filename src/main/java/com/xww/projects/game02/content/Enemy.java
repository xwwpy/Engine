package com.xww.projects.game02.content;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

public class Enemy extends FreeComponent {
    public Enemy(Vector worldPosition, GameFrame.PositionType positionType, Vector size, AnchorMode anchorMode, Vector velocity, Vector acceleration, int order, int activeCollisionZone, int hitCollisionZone, int mass, boolean whetherShowDebugInfo, boolean is_drag_on) {
        super(worldPosition, positionType, size, anchorMode, velocity, acceleration, order, activeCollisionZone, hitCollisionZone, mass, whetherShowDebugInfo, is_drag_on);
    }
}
