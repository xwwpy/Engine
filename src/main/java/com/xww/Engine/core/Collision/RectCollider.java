package com.xww.Engine.core.Collision;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;

import java.awt.*;

public class RectCollider extends BaseCollider{
    private Vector size;
    public RectCollider(Vector relativePosition, Component owner, Vector size) {
        super(relativePosition, owner);
        this.size = size;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(BaseCollider.boundaryColor);
        Vector drawPosition = this.owner.getDrawPosition().add(this.relativePosition);
        g.drawRect(drawPosition.getX(), drawPosition.getY(), size.getX(), size.getY());
        g.setColor(BaseCollider.collisionColor);
        if(lastCollisionDirection != null) {
            switch (lastCollisionDirection) {
                case RectLeft -> {
                    g.drawLine(drawPosition.getX(), drawPosition.getY(), drawPosition.getX(), drawPosition.getY() + size.getY());
                }
                case RectRight -> {
                    g.drawLine(drawPosition.getX() + size.getX(), drawPosition.getY(), drawPosition.getX() + size.getX(), drawPosition.getY() + size.getY());
                }
                case RectTop -> {
                    g.drawLine(drawPosition.getX(), drawPosition.getY(), drawPosition.getX() + size.getX(), drawPosition.getY());
                }
                case RectBottom -> {
                    g.drawLine(drawPosition.getX(), drawPosition.getY() + size.getY(), drawPosition.getX() + size.getX(), drawPosition.getY() + size.getY());
                }
                default -> {
                  // do nothing
                }
            }
        }
    }

    @Override
    public ActionAfterCollision.CollisionInfo checkCollision(BaseCollider other) {
        if (other instanceof RectCollider otherRect){

            Vector position = this.owner.getLeftTopWorldPosition().add(this.relativePosition);
            double left_x_1 = position.getFullX();
            double right_x_1 = position.getFullX() + this.size.getFullX();
            double up_y_1 = position.getFullY();
            double down_y_1 = position.getFullY() + this.size.getFullY();

            Vector shapePosition = otherRect.owner.getLeftTopWorldPosition().add(otherRect.relativePosition);
            double left_x_2 = shapePosition.getFullX();
            double right_x_2 = shapePosition.getFullX() + otherRect.size.getFullX();
            double up_y_2 = shapePosition.getFullY();
            double down_y_2 = shapePosition.getFullY() + otherRect.size.getFullY();

            // 判断两矩形是否相交
            boolean isColliding = !(right_x_1 < left_x_2 || left_x_1 > right_x_2 || down_y_1 < up_y_2 || up_y_1 > down_y_2);
            if (isColliding) {
                // 计算碰撞方向
                double overlapX = Math.min(right_x_1, right_x_2) - Math.max(left_x_1, left_x_2);
                double overlapY = Math.min(down_y_1, down_y_2) - Math.max(up_y_1, up_y_2);

                ActionAfterCollision.collisionDirection direction;
                if (overlapX < overlapY) {
                    if (left_x_1 < left_x_2) {
                        direction = ActionAfterCollision.collisionDirection.RectRight;
                    } else {
                        direction = ActionAfterCollision.collisionDirection.RectLeft;
                    }
                } else {
                    if (up_y_1 < up_y_2) {
                        direction = ActionAfterCollision.collisionDirection.RectBottom;
                    } else {
                        direction = ActionAfterCollision.collisionDirection.RectTop;
                    }
                }
                return new ActionAfterCollision.CollisionInfo(true, direction, other.owner, this, other);
            } else {
                return ActionAfterCollision.CollisionInfo.NoCollisionInfo();
            }
        } else if (other instanceof CircleCollider otherCircle){
            return CollisionHandler.checkCollisionRectWithCircle(otherCircle, this);
        } else {
            System.out.println("Rect 碰撞体不支持此碰撞检测");
            return ActionAfterCollision.CollisionInfo.NoCollisionInfo();
        }
    }

    public Vector getSize() {
        return size;
    }

    public void setSize(Vector size) {
        this.size = size;
    }
}
