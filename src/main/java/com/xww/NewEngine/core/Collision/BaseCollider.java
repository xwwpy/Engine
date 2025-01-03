package com.xww.NewEngine.core.Collision;

import com.xww.NewEngine.core.Component.Component;
import com.xww.NewEngine.core.Vector.Vector;

public abstract class BaseCollider {
    protected Vector relativePosition; // 相对拥有者的位置

    protected Component owner;

    protected boolean isAlive = false;

    public BaseCollider(Vector relativePosition, Component owner) {
        this.relativePosition = relativePosition;
        this.owner = owner;
    }

    public static boolean whetherCollider(BaseCollider collider1, BaseCollider collider2){
        return false;
    }

    public Vector getWorldPosition(){
        return owner.getWorldPosition().add(relativePosition);
    }

    public Vector getDrawPosition(){
        return owner.getDrawPosition().add(relativePosition);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Vector getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Vector relativePosition) {
        this.relativePosition = relativePosition;
    }
}
