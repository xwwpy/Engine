package com.xww.Engine.core.Barrier.impl;

import com.xww.Engine.core.Barrier.BaseGround;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.setting.FrameSetting;

import java.awt.*;

public class Ground extends BaseGround {

    public Ground(Vector worldPosition) {
        super(worldPosition, Vector.build(FrameSetting.DEFAULT_WIDTH, 0), true);
    }

    public Ground(Vector worldPosition, Vector size) {
        super(worldPosition, size, true);
    }

    @Override
    protected void onRender(Graphics g) {
        g.setColor(BaseGround.DebugColor);
        Vector drawPosition = this.getDrawPosition();
        g.drawRect(drawPosition.getX(), drawPosition.getY(), this.getSize().getX(), this.getSize().getY());
    }
}
