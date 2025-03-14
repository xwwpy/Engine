package com.xww.Engine.core.Component.impl;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;

public class MusicComponent extends FreeComponent {
    public MusicComponent() {
        super(Vector.build(900, 20), GameFrame.PositionType.Screen, Vector.Zero(), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE, CollisionDefaultConstValue.noCollisionChecking, CollisionDefaultConstValue.noCollisionChecking, Integer.MAX_VALUE, true, false);
    }

    @Override
    protected void showDebugInfo(Graphics g) {
        g.setColor(Color.green);
        g.drawString("音乐: " + (MP3Player.getInstance().isWhetherCloseMusic() ? "关闭" : "开启"), this.getDrawPosition().getX(), this.getDrawPosition().getY());
        g.drawString("音乐队列: " + MP3Player.getInstance().getAudioQueueSize(), this.getDrawPosition().getX(), this.getDrawPosition().getY() + 20);
    }
}
