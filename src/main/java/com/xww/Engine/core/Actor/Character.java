package com.xww.Engine.core.Actor;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

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
                     int CollisionRegion,
                     int mass,
                     boolean whetherShowDebugInfo) {
        super(worldPosition, GameFrame.PositionType.World, size, AnchorMode.LeftTop, velocity, acceleration, order, CollisionRegion, mass, whetherShowDebugInfo);
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
}
