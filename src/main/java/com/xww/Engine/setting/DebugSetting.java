package com.xww.Engine.setting;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;

import java.awt.*;

public class DebugSetting {
    public static boolean IS_DEBUG_ON = true;
    public static Color DebugInfoColor = Color.RED;

    public static Color ShowImgBoundaryColor = Color.WHITE;

    public static boolean whetherShowImgBoundary = true; // 只有当此为true以及开启了DEBUG模式时时，才会显示图片的边界

    public static void ShowDebugInfo(Component component, Graphics g){
        if (IS_DEBUG_ON) {
            Vector drawPosition = component.getDrawPosition();
            g.setColor(DebugInfoColor);
            g.drawString("size: " + component.getSize().toString(), drawPosition.getX(), drawPosition.getY() + 10);
            g.drawString("position: " + component.getWorldPosition().toString(), drawPosition.getX(), drawPosition.getY() + 20);
            g.drawString("velocity: " + component.getVelocity().toString(), drawPosition.getX(), drawPosition.getY() + 30);
            g.drawString("acceleration: " + component.getAcceleration().toString(), drawPosition.getX(), drawPosition.getY() + 40);
            g.drawString("order: " + component.getOrder(), drawPosition.getX(), drawPosition.getY() + 50);
            g.drawString("whetherCheckCollision: " + component.isWhetherCheckCollision(), drawPosition.getX(), drawPosition.getY() + 60);
            g.drawString("collisionRegion: " + component.getCollisionRegion(), drawPosition.getX(), drawPosition.getY() + 70);
            g.drawString("collisionShape num: " + component.getColliders().size(), drawPosition.getX(), drawPosition.getY() + 80);
            g.drawString("alive: " + component.isAlive(), drawPosition.getX(), drawPosition.getY() + 90);
            g.drawString("mass: " + component.getMass(), drawPosition.getX(), drawPosition.getY() + 100);
        }
    }
}
