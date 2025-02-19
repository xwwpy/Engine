package com.xww.projects.game02.content;

import com.xww.Engine.Utils.DrawUtils;
import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.FrameSetting;

import java.awt.*;

public class BackGroundComponent extends FreeComponent {

    public static final Image background = ResourceManager.getInstance().findImage("background");

    public BackGroundComponent() {
        super(Vector.Zero(), GameFrame.PositionType.World, Vector.build(1695, 725), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), -Integer.MAX_VALUE, CollisionDefaultConstValue.noCollisionChecking, CollisionDefaultConstValue.noCollisionChecking, -1, false, false);
    }

    @Override
    public void on_update(Graphics g) {
        DrawUtils.drawImage(background, this.getDrawPosition(), Vector.build(FrameSetting.DEFAULT_WIDTH, FrameSetting.DEFAULT_HEIGHT), g);
    }
}
