package com.xww.Engine.core.Collision;

import com.xww.Engine.core.Component.Component;

import java.util.HashSet;
import java.util.Set;

public class CollisionHandler {
    public static Set<BaseCollider> colliders = new HashSet<>();

    public static boolean checkCollision(Component component) {
        Set<BaseCollider> colliderSet = component.getColliders();
        outer: for (BaseCollider other: colliders) {
            for (BaseCollider collider: colliderSet) {
                if (other.owner.getCollisionRegion() == -1 || collider.owner.getCollisionRegion() == -1 || other.owner.getCollisionRegion() == collider.owner.getCollisionRegion()) {
                    continue outer;
                } else {
                    if (collider.checkCollision(other)) {
                        other.owner.receiveCollision(component);
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
