package com.xww.NewEngine.core.Actor;

import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Animation.Animation;
import com.xww.NewEngine.core.Component.FreeComponent;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Character extends FreeComponent {

    protected Map<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation;

    public Character(Vector worldPosition,
                     Vector size,
                     Vector velocity,
                     Vector acceleration,
                     int order,
                     int CollisionRegion) {
        super(worldPosition, GameFrame.PositionType.World, size, AnchorMode.LeftTop, velocity, acceleration, order, CollisionRegion);
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
        if (currentAnimation != null) {
            currentAnimation.on_update(g);
        }
    }

    /**
     * 选择动画
     */
    public abstract void selectCurrentAnimation();
}
