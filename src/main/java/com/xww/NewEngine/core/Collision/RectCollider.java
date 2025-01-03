package com.xww.NewEngine.core.Collision;

import com.xww.NewEngine.core.Component.Component;
import com.xww.NewEngine.core.Vector.Vector;

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
    }

    @Override
    public boolean checkCollision(BaseCollider other) {
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
            return !(right_x_1 < left_x_2 || left_x_1 > right_x_2 || down_y_1 < up_y_2 || up_y_1 > down_y_2);
        } else if (other instanceof  CircleCollider){
            return other.checkCollision(this);
        } else {
            System.out.println("Rect 碰撞体不支持此碰撞检测");
            return false;
        }
    }

    public Vector getSize() {
        return size;
    }
}
