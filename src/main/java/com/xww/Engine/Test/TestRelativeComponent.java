package com.xww.Engine.Test;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Component.RelativeComponent;
import com.xww.Engine.core.Vector.Vector;

import java.awt.*;

public class TestRelativeComponent extends RelativeComponent {
    public TestRelativeComponent(Component parent, Vector size, AnchorMode anchorMode, Vector velocity, Vector acceleration, Vector relative_position, boolean WhetherPinned, int order, int CollisionRegion) {
        super(parent, size, anchorMode, velocity, acceleration, relative_position, WhetherPinned, order, CollisionRegion, 10, true, true);
    }

    @Override
    public void on_update(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(
                this.getDrawPosition().getX(),
                this.getDrawPosition().getY(),
                this.size.getX(),
                this.size.getY()
        );
        super.on_update(g);
    }
}
