package com.xww.Engine.core.Collision;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;

import java.awt.*;

public abstract class BaseCollider {
    public static final Color boundaryColor = Color.MAGENTA;
    public static final Color collisionColor = Color.WHITE;
    protected Vector relativePosition; // 碰撞体左上角相对拥有者左上角的相对位置

    protected Component owner;

    protected boolean enable = true;
    protected boolean isAlive = true;
    /**
     * 上次发生碰撞的方向
     * 当它与其它的所有碰撞体检测碰撞后 没有发生碰撞的话 将其设置为null
     */

    protected ActionAfterCollision.collisionDirection lastCollisionDirection = null;

    public BaseCollider(Vector relativePosition, Component owner) {
        this.relativePosition = relativePosition;
        this.owner = owner;
    }

    public abstract void draw(Graphics g);


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

    public abstract ActionAfterCollision.CollisionInfo checkCollision(BaseCollider other);

    public void setOwner(Component component) {
        this.owner = component;
    }

    public Component getOwner() {
        return owner;
    }

    public ActionAfterCollision.collisionDirection getLastCollisionDirection() {
        return lastCollisionDirection;
    }

    public void setLastCollisionDirection(ActionAfterCollision.collisionDirection lastCollisionDirection) {
        this.lastCollisionDirection = lastCollisionDirection;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
