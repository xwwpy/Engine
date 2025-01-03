package com.xww.NewEngine.core.Collision;

import com.xww.NewEngine.core.Component.Component;
import com.xww.NewEngine.core.Vector.Vector;

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
    public boolean checkCollision(BaseCollider other) {
        if (other instanceof CircleCollider otherCircle){
            Vector circle_point_position1 = this.owner.getLeftTopWorldPosition().add(this.relativePosition).add_to_self(Vector.build(this.radius, this.radius));
            Vector circle_point_position2 = otherCircle.owner.getLeftTopWorldPosition().add(otherCircle.relativePosition).add_to_self(Vector.build(otherCircle.radius, otherCircle.radius));
            double distance = circle_point_position1.distance(circle_point_position2);
            return distance < (this.radius + otherCircle.radius);
        } else if (other instanceof RectCollider otherRect){
            Vector circle_point_position = this.owner.getLeftTopWorldPosition().add(this.relativePosition).add_to_self(Vector.build(this.radius, this.radius));
            Vector rect_center_pointer = otherRect.owner.getLeftTopWorldPosition().add(otherRect.relativePosition).add_to_self(otherRect.getSize().divide(2));
            double distanceX = Math.abs(circle_point_position.getFullX() - rect_center_pointer.getFullX());
            double distanceY = Math.abs(circle_point_position.getFullY() - rect_center_pointer.getFullY());
            return (distanceX <= (otherRect.getSize().getFullX() / 2 + this.radius) && distanceY <= (otherRect.getSize().getFullY() / 2 + this.radius));
//            {
//                System.out.println("center -- rect :" + rect_center_pointer + "--" + "circle: " + circle_point_position);
//                return true;
//            }
//            return false;
        } else {
            System.out.println("Circle碰撞体不支持的碰撞检测");
            return false;
        }
    }
}
