package com.xww.NewEngine.Test;

import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Base;
import com.xww.NewEngine.core.Component.Component;
import com.xww.NewEngine.core.Component.RelativeComponent;
import com.xww.NewEngine.core.Timer.Timer;
import com.xww.NewEngine.core.Vector.Vector;

import java.awt.*;

public class TestRelativeComponent extends RelativeComponent {
    public TestRelativeComponent(Component parent, Vector size, AnchorMode anchorMode, Vector velocity, Vector acceleration, Vector relative_position, boolean WhetherPinned, int order) {
        super(parent, size, anchorMode, velocity, acceleration, relative_position, WhetherPinned, order);
        Timer timer = new Timer(3456, new Timer.TimerCallBack() {
            public void run(Base obj) {
                ((TestRelativeComponent) obj).isAlive = false;
            }
        }, this);
        timer.setRun_times(1);
        this.addTimer(timer);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        g.setColor(Color.YELLOW);
        g.fillRect(
                this.worldPosition.getX(),
                this.worldPosition.getY(),
                this.size.getX(),
                this.size.getY()
        );
    }
}
