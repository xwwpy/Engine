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
                      int activeCollisionZone,
                      int hitCollisionZone,
                      int mass,
                      boolean whetherShowDebugInfo,
                      boolean is_drag_on,
                      int life,
                      int atk,
                      int atk_interval) {
        super(worldPosition, size, velocity, acceleration, order, activeCollisionZone, hitCollisionZone, mass, whetherShowDebugInfo, is_drag_on, life, atk, atk_interval, ObjectType.ZOMBIE);
    }
}
