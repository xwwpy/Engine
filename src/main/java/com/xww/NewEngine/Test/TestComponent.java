package com.xww.NewEngine.Test;

import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Component.FreeComponent;
import com.xww.NewEngine.core.Timer.Timer;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;

import java.awt.*;
import java.util.Random;

public class TestComponent extends FreeComponent {
    public static Random random = new Random();

    int countR = 1;
    int countG = 1;
    int countB = 1;
    public TestComponent(Vector worldPosition, Vector size, AnchorMode anchorMode, Vector velocity, Vector acceleration, int order) {
        super(worldPosition, GameFrame.PositionType.World, size, anchorMode, velocity, acceleration, order);
        Timer timer = new Timer(500, (obj)->{
            TestComponent testComponent = ((TestComponent)obj);
            testComponent.countG = random.nextInt(255);
            testComponent.countB = random.nextInt(255);
            testComponent.countR = random.nextInt(255);
        }, this);
        timer.setRun_times(Timer.INFINITE_TIMES);
        this.addTimer(timer);
    }

    @Override
    public void on_update(Graphics graphics) {

        graphics.setColor(new Color(countR, countG, countB));
        graphics.fillRect(
                 this.getDrawPosition().getX(),
                 this.getDrawPosition().getY(),
                 this.size.getX(),
                 this.size.getY()
        );
        super.on_update(graphics);
    }
}
