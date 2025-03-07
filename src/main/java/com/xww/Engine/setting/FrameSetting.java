package com.xww.Engine.setting;

import com.xww.Engine.core.Vector.Vector;

public class FrameSetting {
    public static final int MIN_FPS = 1;
    public static final int MAX_FPS = 1440;
    public static int DEFAULT_WIDTH = 1400;
    public static int DEFAULT_HEIGHT = 800;
    public static int DEFAULT_FPS = 120;
    public static String DEFAULT_TITLE = "NewEngineByXww";


    // TODO
    public static int timeSpeed = 2; // 当前的时间倍速 2代表 0.5倍速

    public static boolean whetherInScreen(Vector drawPosition, Vector size) {
        return drawPosition.getX() + size.getX() > 0 && drawPosition.getX() < FrameSetting.DEFAULT_WIDTH &&
                drawPosition.getY() + size.getY() > 0 && drawPosition.getY() < FrameSetting.DEFAULT_HEIGHT;
    }
}
