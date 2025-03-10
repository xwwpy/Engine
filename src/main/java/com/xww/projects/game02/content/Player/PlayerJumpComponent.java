package com.xww.projects.game02.content.Player;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;

public class PlayerJumpComponent extends FreeComponent {

    public Animation animation;
    public PlayerJumpComponent(Vector worldPosition) {
        super(worldPosition,
                GameFrame.PositionType.World,
                Vector.build(100, 100),
                AnchorMode.LeftTop,
                Vector.Zero(),
                Vector.Zero(),
                100,
                CollisionDefaultConstValue.noCollisionChecking,
                CollisionDefaultConstValue.noCollisionChecking,
                Component.infiniteMass,
                false,
                false);
        animation = new Animation(this, 100);
        animation.add_frame(ResourceManager.getInstance().findImage("player_vfx_jump"), 5, size);
        animation.setIs_loop(false);
        animation.setOn_complete((owner) -> {
            owner.setAlive(false);
        });
    }

    @Override
    public void on_update(Graphics g) {
        animation.on_update(g);
        super.on_update(g);
    }
}
