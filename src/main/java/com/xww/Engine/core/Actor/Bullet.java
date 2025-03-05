package com.xww.Engine.core.Actor;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

// TODO
public abstract class Bullet extends FreeComponent {
    protected Component owner; // 子弹的拥有者
    protected int damage; // 子弹的伤害

    protected int damageInterval; // 子弹攻击同一个人可以造成伤害的的间隔 单位毫秒
    protected Component lastDamageTarget;
    protected boolean whetherCanPenetrate; // 子弹是否可以穿透

    public Bullet(Component owner,
                  Vector worldPosition,
                  Vector size,
                  Vector velocity,
                  Vector acceleration,
                  int damageInterval,
                  boolean whetherCanPenetrate,
                  int activeCollisionZone,
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
                -1,
                true,
                false);
        this.owner = owner;
        this.damage = damage;
        this.damageInterval = damageInterval;
        this.whetherCanPenetrate = whetherCanPenetrate;
    }


    @Override
    protected void checkMove() {
        this.velocity = this.velocity.add_to_self(GameFrame.getFrameVelocity(this.acceleration));
        // 预移动
        pre_move();
        // 检查碰撞
        // 当组件需要检测碰撞时才进行检测 并且需要主动碰撞
        if (whetherCheckCollision && activeCollisionZone != CollisionDefaultConstValue.noCollisionChecking) {
            ActionAfterCollision.CollisionInfo collisionInfo = checkCollision();
            // 如果发生碰撞执行下面的逻辑
            if (collisionInfo.isWhetherCollider()) {
                Component component = collisionInfo.getOtherCollider().getOwner(); // 碰撞到的组件
                if (component instanceof Character character){
                    if (character.lastBeAttackedBullet == this && character.getLastBeAttackedTime() < damageInterval){
                        return;
                    }
                    character.attackByBullet(this);
                } else {
                    System.out.println("暂未实现子弹对象与该种类型--" + component + "--碰撞后的行为");
                }
                if (!whetherCanPenetrate) {
                    this.crash();
                }
            }
        }
    }

    /**
     * 子弹销毁
     */
    public abstract void crash();

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
