package com.xww.projects.game01.object;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Vector.Vector;

public abstract class BaseObject extends Character {

    protected ObjectType objectType; // 对象所属的类型
    public BaseObject(Vector worldPosition,
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
                      int atk_interval,
                      ObjectType objectType) {
        super(worldPosition, size, velocity, acceleration, order, CollisionRegion, mass, whetherShowDebugInfo, is_drag_on, life, atk, atk_interval);
        this.objectType = objectType;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }
}
