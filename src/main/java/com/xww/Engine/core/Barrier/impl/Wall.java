package com.xww.Engine.core.Barrier.impl;

import com.xww.Engine.core.Barrier.BaseGround;
import com.xww.Engine.core.Barrier.BaseWall;
import com.xww.Engine.core.Vector.Vector;

import java.awt.*;

public class Wall extends BaseWall {
    public Wall(Vector worldPosition) {
        super(worldPosition, Vector.build(20, 100), true);
        BaseWall.registerWall(this);
        Ground ground = new Ground(this.worldPosition, Vector.build(this.size.getFullX(), 0));
        ground.setWhetherCanDown(false);
        BaseGround.registerBarrier(ground);
    }

    public Wall(Vector worldPosition, Vector size) {
        super(worldPosition, size, true);
        BaseWall.registerWall(this);
        Ground ground = new Ground(this.worldPosition, Vector.build(this.size.getFullX(), 0));
        ground.setWhetherCanDown(false);
        BaseGround.registerBarrier(ground);
    }

    @Override
    protected void onRender(Graphics g) {
        g.setColor(BaseWall.DebugColor);
        g.drawRect(this.getDrawPosition().getX(), this.getDrawPosition().getY(), this.getSize().getX(), this.getSize().getY());
    }
}
