package com.xww.projects.game01.object.Zombie;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game01.object.BaseObject;
import com.xww.projects.game01.object.ObjectType;

public abstract class BaseZombie extends BaseObject {
    public BaseZombie(Vector worldPosition,
                      Vector size,
                      int mass,
                      int life,
                      int atk_interval,
                      ObjectType objectType) {
        super(worldPosition, size, mass, life, atk_interval, objectType);
    }
}
