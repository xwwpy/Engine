package com.xww.projects.game01.object.Zombie;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game01.object.BaseObject;
import com.xww.projects.game01.object.ObjectType;

public abstract class BaseZombie extends BaseObject {

    public BaseZombie(Vector worldPosition,
                      Vector size,
                      Vector velocity,
                      Vector acceleration,
                      int order,
                      int CollisionRegion,
                      int mass,
                      boolean whetherShowDebugInfo,
                      boolean is_drag_on,
                      int life,
                      int atk,
                      int atk_interval) {
        super(worldPosition, size, velocity, acceleration, order, CollisionRegion, mass, whetherShowDebugInfo, is_drag_on, life, atk, atk_interval, ObjectType.ZOMBIE);
    }

    @Override
    public boolean whetherCheckCollision(Component other) {
        return super.whetherCheckCollision(other);
    }
}
