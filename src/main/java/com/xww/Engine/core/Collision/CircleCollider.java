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
            return new ActionAfterCollision.CollisionInfo(distance < (this.radius + otherCircle.radius), ActionAfterCollision.collisionDirection.RectLeft, otherCircle.owner, this, otherCircle);
        } else if (other instanceof RectCollider otherRect){
            return CollisionHandler.checkCollisionRectWithCircle(otherRect, this);
        } else {
            System.out.println("Circle碰撞体不支持的碰撞检测");
            return ActionAfterCollision.CollisionInfo.NoCollisionInfo();
        }
    }

    public double getRadius() {
        return radius;
    }
}
