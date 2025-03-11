package com.xww.projects.game02.content.Boss.BossBullet;

import com.xww.Engine.core.Actor.Bullet;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game02.content.Boss.Boss;

import java.awt.*;

public class Sword extends Bullet {
    private final Animation currentAnimation;
    public Sword(Component owner,
                 Vector worldPosition) {

        super(owner,
                worldPosition,
                Vector.build(150, 20),
                Vector.Zero(),
                Vector.Zero(),
                1000,
                true,
                Boss.atkZone,
                20);

        Animation left = new Animation(this, 100);
        left.add_frame(ResourceManager.getInstance().findAtlas("enemy_sword"));

        Animation right = new Animation(this, 100);
        right.add_frame(ResourceManager.getInstance().findAtlas("enemy_sword_right"));

        if (owner instanceof Boss boss){
            if (boss.isWhetherFacingLeft()) {
                this.worldPosition = worldPosition.sub(Vector.build(this.size.getFullX() / 2, -75));
                this.velocity = Vector.build(-400, 0);
                this.currentAnimation = left;
            } else {
                this.worldPosition = worldPosition.add(Vector.build(this.size.getFullX() / 2, 75));
                this.currentAnimation = right;
                this.velocity = Vector.build(400, 0);
            }
        } else {
            throw new RuntimeException("owner is not a Boss");
        }
        this.addCollider(new RectCollider(Vector.Zero(), this, this.size));
        this.addTimer(new Timer(5000, (obj) -> {
            ((Component) obj).setAlive(false);
        }, this));
        Component.addComponent(this);
    }

    @Override
    public void on_update(Graphics g) {
        currentAnimation.on_update(g);
        super.on_update(g);
    }

    @Override
    public void crash() {

    }
}
