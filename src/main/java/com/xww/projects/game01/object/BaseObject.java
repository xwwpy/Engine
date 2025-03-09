package com.xww.projects.game01.object;

import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.core.Actor.Character;

public abstract class BaseObject extends Character {

    protected ObjectType objectType; // 对象所属的类型
    public BaseObject(Vector worldPosition,
                      Vector size,
                      int mass,
                      int life,
                      int atk_interval,
                      ObjectType objectType) {
        super(worldPosition,
                size,
                size,
                Vector.Zero(),
                mass,
                life,
                atk_interval,
                100,
                0,
                0,
                false,
                2,
                500,
                200,
                200,
                CharacterType.Player);
        this.objectType = objectType;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }
}
