package com.xww.Engine.core.Collision;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;

import java.awt.*;

public class CircleCollider extends BaseCollider{
    private double radius;

    public CircleCollider(Vector relativePosition, Component owner, double radius) {
        super(relativePosition, owner);
        this.radius = radius;
    }

    @Override
    public void draw(Graphics g) {
        Vector drawPosition = this.owner.getDrawPosition().add(this.relativePosition);
        g.setColor(BaseCollider.boundaryColor);
        g.drawOval(drawPosition.getX(), drawPosition.getY(), (int) (radius * 2), (int) (radius * 2));
    }

    @Override
    public ActionAfterCollision.CollisionInfo checkCollision(BaseCollider other) {
        if (other instanceof CircleCollider otherCircle){
            Vector circle_point_position1 = this.owner.getLeftTopWorldPosition().add(this.relativePosition).add_to_self(Vector.build(this.radius, this.radius));
            Vector circle_point_position2 = otherCircle.owner.getLeftTopWorldPosition().add(otherCircle.relativePosition).add_to_self(Vector.build(otherCircle.radius, otherCircle.radius));
            double distance = circle_point_position1.distance(circle_point_position2);
            return new ActionAfterCollision.CollisionInfo(distance < (this.radius + otherCircle.radius), ActionAfterCollision.collisionDirection.RectLeft, other.owner, this, other);
        } else if (other instanceof RectCollider otherRect){
            Vector rectSize = otherRect.getSize();
            Vector rectCenter = otherRect.owner.getLeftTopWorldPosition().add(otherRect.relativePosition).add_to_self(otherRect.getSize().divide(2));
            Vector circleCenter = this.owner.getLeftTopWorldPosition().add(this.relativePosition).add_to_self(Vector.build(this.radius, this.radius));

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
            return new ActionAfterCollision.CollisionInfo((distanceX * distanceX + distanceY * distanceY) <= (radius * radius), ActionAfterCollision.collisionDirection.RectLeft, other.owner, this, other);
//            {
//                System.out.println("center -- rect :" + rect_center_pointer + "--" + "circle: " + circle_point_position);
//                return true;
//            }
//            return false;
        } else {
            System.out.println("Circle碰撞体不支持的碰撞检测");
            return ActionAfterCollision.CollisionInfo.NoCollisionInfo();
        }
    }
}
