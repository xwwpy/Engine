package com.xww.Engine.core.Collision;

import com.xww.Engine.core.Component.Component;

import java.util.HashSet;
import java.util.Set;

public class CollisionHandler {
    public static Set<BaseCollider> colliders = new HashSet<>();

    public static ActionAfterCollision.CollisionInfo checkCollision(Component component) {
        Set<BaseCollider> colliderSet = component.getColliders();
        outer: for (BaseCollider other: colliders) {
            for (BaseCollider collider: colliderSet) {
                if (other.owner.getCollisionRegion() == -1 || collider.owner.getCollisionRegion() == -1 || other.owner.getCollisionRegion() == collider.owner.getCollisionRegion()) {
                    continue outer;
                } else {
                    ActionAfterCollision.CollisionInfo collisionInfo = collider.checkCollision(other);
                    if (collisionInfo.isWhetherCollider()) {
                        ActionAfterCollision.CollisionInfo collisionInfo1 = new ActionAfterCollision.CollisionInfo(true, ActionAfterCollision.collisionDirection.RectLeft, collider.owner, other, collider);
                        other.owner.receiveCollision(collisionInfo1);
                        return collisionInfo;
                    }
                }
            }
        }
        return ActionAfterCollision.CollisionInfo.NoCollisionInfo();
    }

}
