package com.xww.NewEngine.core.Actor;

import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Component.FreeComponent;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;

public class Character extends FreeComponent {

    public Character(Vector worldPosition,
                     Vector size,
                     Vector velocity,
                     Vector acceleration,
                     int order,
                     int CollisionRegion) {
        super(worldPosition, GameFrame.PositionType.World, size, AnchorMode.LeftTop, velocity, acceleration, order, CollisionRegion);
    }
}
