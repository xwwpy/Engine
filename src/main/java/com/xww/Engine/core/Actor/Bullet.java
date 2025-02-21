package com.xww.Engine.core.Actor;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

// TODO
public abstract class Bullet extends FreeComponent {
    protected Component owner; // 子弹的拥有者
    protected int damage; // 子弹的伤害

    protected int damageInterval;
    protected Component lastDamageTarget;
    protected boolean whetherCanPenetrate; // 子弹是否可以穿透
    public Bullet(Component owner,
                  Vector worldPosition,
                  Vector size,
                  Vector velocity,
                  Vector acceleration,
                  int activeCollisionZone,
                  int mass,
                  int damage) {
        super(worldPosition,
                GameFrame.PositionType.World,
                size,
                AnchorMode.LeftTop,
                velocity,
                acceleration,
                Integer.MAX_VALUE - 1,
                activeCollisionZone,
                CollisionDefaultConstValue.noCollisionChecking,
                mass,
                true,
                false);
        this.owner = owner;
    }

    public Component getOwner() {
        return owner;
    }

    public void setOwner(Component owner) {
        this.owner = owner;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamageInterval() {
        return damageInterval;
    }

    public void setDamageInterval(int damageInterval) {
        this.damageInterval = damageInterval;
    }

    public Component getLastDamageTarget() {
        return lastDamageTarget;
    }

    public void setLastDamageTarget(Component lastDamageTarget) {
        this.lastDamageTarget = lastDamageTarget;
    }

    public boolean isWhetherCanPenetrate() {
        return whetherCanPenetrate;
    }

    public void setWhetherCanPenetrate(boolean whetherCanPenetrate) {
        this.whetherCanPenetrate = whetherCanPenetrate;
    }
}
