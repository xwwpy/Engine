package com.xww.projects.game02.content.barrier;

import com.xww.Engine.core.Barrier.BaseBarrier;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.setting.FrameSetting;

import java.awt.*;

public class Ground extends BaseBarrier {

    public Ground(Vector worldPosition) {
        super(worldPosition, Vector.build(FrameSetting.DEFAULT_WIDTH, 0));
    }

    @Override
    protected void onRender(Graphics g) {
        g.setColor(BaseBarrier.DebugColor);
        Vector drawPosition = this.getDrawPosition();
        g.drawLine(drawPosition.getX(), drawPosition.getY(), drawPosition.getX() + this.getSize().getX(), drawPosition.getY());
    }
}
