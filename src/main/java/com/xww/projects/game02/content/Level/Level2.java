package com.xww.projects.game02.content.Level;

import com.xww.Engine.core.Barrier.BaseGround;
import com.xww.Engine.core.Barrier.impl.Ground;
import com.xww.Engine.core.Barrier.impl.Wall;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.setting.FrameSetting;

public class Level2 {
    public static void initGame() {
        Ground ground = new Ground(Vector.build(0, 680));
        ground.setWhetherCanDown(false);
        BaseGround.registerBarrier(ground);
        new Wall(Vector.build(-10, 0), Vector.build(10, FrameSetting.DEFAULT_HEIGHT));
        new Wall(Vector.build(500, 0), Vector.build(10, (double) FrameSetting.DEFAULT_HEIGHT / 2));
        new Wall(Vector.build(FrameSetting.DEFAULT_WIDTH, 0), Vector.build(10, FrameSetting.DEFAULT_HEIGHT));
    }
}
