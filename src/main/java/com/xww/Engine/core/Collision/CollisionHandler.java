package com.xww.Engine.core.Collision;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;

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

    public static ActionAfterCollision.CollisionInfo checkCollisionRectWithCircle(RectCollider otherRect, CircleCollider selfCircle){
        Vector rectSize = otherRect.getSize();
        Vector rectCenter = otherRect.owner.getLeftTopWorldPosition().add(otherRect.relativePosition).add_to_self(otherRect.getSize().divide(2));
        Vector circleCenter = selfCircle.owner.getLeftTopWorldPosition().add(selfCircle.relativePosition).add_to_self(Vector.build(selfCircle.getRadius(), selfCircle.getRadius()));

        double rectHalfWidth = rectSize.getFullX() / 2;
        double rectHalfHeight = rectSize.getFullY() / 2;

        // 2. 找到圆心在矩形坐标系中的最近点
        double closestX = Math.max(rectCenter.getFullX() - rectHalfWidth,
                Math.min(circleCenter.getFullX(), rectCenter.getFullX() + rectHalfWidth));
        double closestY = Math.max(rectCenter.getFullY() - rectHalfHeight,
                Math.min(circleCenter.getFullY(), rectCenter.getFullY() + rectHalfHeight));

        // 3. 计算圆心到最近点的距离
        double distanceX = circleCenter.getFullX() - closestX;
        double distanceY = circleCenter.getFullY() - closestY;

        // 4. 判断是否发生碰撞
        return new ActionAfterCollision.CollisionInfo((distanceX * distanceX + distanceY * distanceY) < (selfCircle.getRadius() * selfCircle.getRadius()), ActionAfterCollision.collisionDirection.RectLeft, otherRect.owner, selfCircle, otherRect);
    }


    public static ActionAfterCollision.CollisionInfo checkCollisionRectWithCircle(CircleCollider otherCircle, RectCollider selfRect){
        Vector rectSize = selfRect.getSize();
        Vector rectCenter = selfRect.owner.getLeftTopWorldPosition().add(selfRect.relativePosition).add_to_self(selfRect.getSize().divide(2));
        Vector circleCenter = otherCircle.owner.getLeftTopWorldPosition().add(otherCircle.relativePosition).add_to_self(Vector.build(otherCircle.getRadius(), otherCircle.getRadius()));

        double rectHalfWidth = rectSize.getFullX() / 2;
        double rectHalfHeight = rectSize.getFullY() / 2;

        // 2. 找到圆心在矩形坐标系中的最近点
        double closestX = Math.max(rectCenter.getFullX() - rectHalfWidth,
                Math.min(circleCenter.getFullX(), rectCenter.getFullX() + rectHalfWidth));
        double closestY = Math.max(rectCenter.getFullY() - rectHalfHeight,
                Math.min(circleCenter.getFullY(), rectCenter.getFullY() + rectHalfHeight));

        // 3. 计算圆心到最近点的距离
        double distanceX = circleCenter.getFullX() - closestX;
        double distanceY = circleCenter.getFullY() - closestY;

        // 4. 判断是否发生碰撞
        return new ActionAfterCollision.CollisionInfo((distanceX * distanceX + distanceY * distanceY) < (otherCircle.getRadius() * otherCircle.getRadius()), ActionAfterCollision.collisionDirection.RectLeft, otherCircle.owner, selfRect, otherCircle);
    }
}
