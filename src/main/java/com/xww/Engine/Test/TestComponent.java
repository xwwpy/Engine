package com.xww.Engine.Test;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Event.Message.Impl.KeyBoardMessageHandler;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TestComponent extends FreeComponent {
    public static Random random = new Random();

    int countR = 1;
    int countG = 1;
    int countB = 1;
    public TestComponent(Vector worldPosition, Vector size, AnchorMode anchorMode, Vector velocity, Vector acceleration, int order, int collisionRegion) {
        super(worldPosition, GameFrame.PositionType.World, size, anchorMode, velocity, acceleration, order, collisionRegion, 10, true, true);
        Timer timer = new Timer(50, (obj)->{
            TestComponent testComponent = ((TestComponent)obj);
            testComponent.countG = random.nextInt(255);
            testComponent.countB = random.nextInt(235);
            testComponent.countR = random.nextInt(225);
        }, this);
        timer.setRun_times(Timer.INFINITE_TIMES);
        this.addTimer(timer);
        this.addCollider(new RectCollider(Vector.Zero(), this, size));
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
