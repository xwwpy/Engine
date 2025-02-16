package com.xww.Engine.Utils;

import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.setting.DebugSetting;

import java.awt.*;

public class DrawUtils {
    public static void drawImage(Image image, Vector position, Graphics g) {
        g.drawImage(image, position.getX(), position.getY(), null);
        if (DebugSetting.IS_DEBUG_ON && DebugSetting.whetherShowImgBoundary){
            g.setColor(DebugSetting.ShowImgBoundaryColor);
            g.drawRect(position.getX(), position.getY(), image.getWidth(null), image.getHeight(null));
        }
    }

    public static void drawImage(Image image, Vector position, Vector size, Graphics g) {
        g.drawImage(image, position.getX(), position.getY(), size.getX(), size.getY(), null);
        if (DebugSetting.IS_DEBUG_ON && DebugSetting.whetherShowImgBoundary){
            g.setColor(DebugSetting.ShowImgBoundaryColor);
            g.drawRect(position.getX(), position.getY(), size.getX(), size.getY());
        }
    }
}
