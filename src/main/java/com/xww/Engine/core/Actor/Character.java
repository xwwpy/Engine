package com.xww.Engine.core.Actor;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public abstract class Character extends FreeComponent {
    protected int life;
    protected int currentLife;
    protected int atk;
    protected int atk_interval; // 攻击间隔或者其它特殊行为的间隔
    protected boolean whetherCanAtk = true;
    protected Map<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation;

    public Character(Vector worldPosition,
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
        super(worldPosition, GameFrame.PositionType.World, size, AnchorMode.LeftTop, velocity, acceleration, order, CollisionRegion, mass, whetherShowDebugInfo, is_drag_on);
        this.life = life;
        this.currentLife = life;
        this.atk = atk;
        this.atk_interval = atk_interval;
    }

    public void addAnimation(String name, Animation animation) {
        animations.put(name, animation);
    }

    public void setAnimation(String name) {
        currentAnimation = animations.get(name);
    }
    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        // 选择动画
        selectCurrentAnimation();
        if (currentAnimation != null) {
            currentAnimation.on_update(g);
        } else {
            System.out.println("currentAnimation is null");
        }
    }

    /**
     * 选择动画
     */
    public abstract void selectCurrentAnimation();

    /**
     * 尝试进行攻击
     * @return 是否攻击成功
     */
    protected abstract boolean tryAtk();

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getCurrentLife() {
        return currentLife;
    }

    public void setCurrentLife(int currentLife) {
        this.currentLife = currentLife;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getAtk_interval() {
        return atk_interval;
    }

    public void setAtk_interval(int atk_interval) {
        this.atk_interval = atk_interval;
    }

    public boolean isWhetherCanAtk() {
        return whetherCanAtk;
    }

    public void setWhetherCanAtk(boolean whetherCanAtk) {
        this.whetherCanAtk = whetherCanAtk;
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }
}
