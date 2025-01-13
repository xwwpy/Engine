package com.xww.projects.game01.Plant;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Vector.Vector;

public class BasePlant extends Character {
    public BasePlant(Vector worldPosition,
                     Vector size,
                     Vector velocity,
                     Vector acceleration,
                     int order,
                     int CollisionRegion,
                     int mass,
                     boolean whetherShowDebugInfo,
                     boolean is_drag_on) {
        super(worldPosition, size, velocity, acceleration, order, CollisionRegion, mass, whetherShowDebugInfo, is_drag_on);
    }

    @Override
    public void selectCurrentAnimation() {

    }
}
