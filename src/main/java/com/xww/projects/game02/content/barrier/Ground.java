package com.xww.projects.game02.content.barrier;

import com.xww.Engine.core.Barrier.BaseGround;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.setting.FrameSetting;

import java.awt.*;

public class Ground extends BaseGround {

    public Ground(Vector worldPosition) {
        super(worldPosition, Vector.build(FrameSetting.DEFAULT_WIDTH, 0), true);
    }

    @Override
    protected void onRender(Graphics g) {
        g.setColor(BaseGround.DebugColor);
        Vector drawPosition = this.getDrawPosition();
        g.drawLine(drawPosition.getX(), drawPosition.getY(), drawPosition.getX() + this.getSize().getX(), drawPosition.getY());
    }
}
