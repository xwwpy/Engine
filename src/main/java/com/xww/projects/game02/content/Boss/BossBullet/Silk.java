package com.xww.projects.game02.content.Boss.BossBullet;

import com.xww.Engine.core.Actor.Bullet;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game02.content.Boss.Boss;

import java.awt.*;

public class Silk extends Bullet {
    private Vector relativePosition;

    private final Animation currentAnimation;
    public Silk(Component owner,
                Vector worldPosition) {
        super(owner,
                worldPosition,
                Vector.build(400, 400),
                Vector.Zero(),
                Vector.Zero(),
                200,
                true,
                Boss.atkZone,
                10);
        Animation silkLeftAnimation = new Animation(this, 100);
        silkLeftAnimation.add_frame(ResourceManager.getInstance().findAtlas("enemy_silk"));
        silkLeftAnimation.setIs_loop(true);
        Animation silkRightAnimation = new Animation(this, 100);
        silkRightAnimation.add_frame(ResourceManager.getInstance().findAtlas("enemy_silk_right"));
        silkRightAnimation.setIs_loop(true);
        this.relativePosition = Vector.build(-125, -125);
        this.currentAnimation = ((Boss)owner).isWhetherFacingLeft() ? silkLeftAnimation : silkRightAnimation;
        this.addCollider(new RectCollider(Vector.Zero(), this, this.size));
        Component.addComponent(this);
    }

    @Override
    public void on_update(Graphics g) {
        this.worldPosition = this.getOwner().getWorldPosition().add(relativePosition);
        currentAnimation.on_update(g);
        super.on_update(g);
    }

    @Override
    public void crash() {

    }
}
