package com.xww.Engine.core.Actor;

import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;

public class DefaultBullet extends Bullet{
    public DefaultBullet(Component owner, int damage) {
        super(owner, Vector.Zero(), Vector.Zero(), Vector.Zero(), Vector.Zero(), 0, false, CollisionDefaultConstValue.noCollisionChecking, damage);
    }

    @Override
    public void crash() {

    }
}
