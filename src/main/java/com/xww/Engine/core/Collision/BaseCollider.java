package com.xww.Engine.core.Collision;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;

import java.awt.*;

public abstract class BaseCollider {
    public static final Color boundaryColor = Color.MAGENTA;
    protected Vector relativePosition; // 碰撞体左上角相对拥有者左上角的相对位置

    protected Component owner;

    protected boolean isAlive = true;

    public BaseCollider(Vector relativePosition, Component owner) {
        this.relativePosition = relativePosition;
        this.owner = owner;
    }

    public abstract void draw(Graphics g);

//    public static boolean whetherCollider(BaseCollider collider1, BaseCollider collider2){
//        return false;
//    }

    public Vector getLeftTopPosition(){
        return owner.getLeftTopWorldPosition().add(relativePosition);
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

    public abstract boolean checkCollision(BaseCollider other);

    public void setOwner(Component component) {
        this.owner = component;
    }

    public Component getOwner() {
        return owner;
    }
}
