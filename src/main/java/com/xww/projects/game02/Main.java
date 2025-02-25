package com.xww.projects.game02;

import com.xww.Engine.Test.TestComponent;
import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Animation.Atlas;
import com.xww.Engine.core.Barrier.BaseBarrier;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;

import com.xww.Engine.core.Event.Message.Impl.KeyBoardMessageHandler;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.FrameSetting;
import com.xww.projects.game02.content.BackGroundComponent;
import com.xww.projects.game02.content.Player;
import com.xww.projects.game02.content.barrier.Ground;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 加载资源
        GameFrame.init(()->{
            loadResources();
            MP3Player.getInstance().setBGMPath(ResourceManager.getInstance().findAudioPath("bgm"));
            MP3Player.getInstance().startBGM();
            initGame();
        });
        GameFrame.start();
        MP3Player.getInstance().shutdown();
    }

    private static void loadResources(){
        long start = System.currentTimeMillis();
        Graphics graphics = GameFrame.context.getGraphics();
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("微软雅黑", Font.PLAIN, 100));
        graphics.drawString("加载资源中...", 150, 400);
        ResourceManager instance = ResourceManager.getInstance();
        instance.loadAllAtlas("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/enemy", "%d", null, ".png");
        Map<String, Atlas> atlasPoolToAdd = new HashMap<>();
        instance.atlasPool.forEach((name, atlas) -> {
            instance.flipAtlas(name, "_", "right", atlasPoolToAdd);
        });
        instance.loadAll("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/player", true, ".png");
        Map<String, BufferedImage> imagePoolToAdd = new HashMap<>();
        instance.imagePool.forEach((name, image) -> {
            instance.flipImage(name, "_", "left", imagePoolToAdd);
        });
        instance.atlasPool.putAll(atlasPoolToAdd);
        instance.imagePool.putAll(imagePoolToAdd);
        instance.loadImage("background", "/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/background.png");
        instance.loadImage("ui_heart", "/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/ui_heart.png");
        instance.loadAllAudio("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/audio", ".mp3");
        long end = System.currentTimeMillis();
        System.out.println("加载资源耗时：" + (end - start) + "ms");
    }

    public static void initGame() {
        Component.addComponent(new BackGroundComponent());
        for (int i = 1; i < 2; i++) {
            Player player = new Player(Vector.build(100, 200));
            if (i % 2 == 0) {
                player.setAnimation("roll_left");
                player.setVelocity(Vector.build(-20, 0));
            }
            player.addCollider(new RectCollider(Vector.build(38, 55), player, Vector.build(92, 440 - 366)));
            Component.addComponent(player);
            KeyBoardMessageHandler.keyBoardMessageHandlerInstance.registerComponent(player);
        }

        BaseBarrier.registerBarrier(new Ground(Vector.build(100, 600)));
        BaseBarrier.registerBarrier(new Ground(Vector.build(300, 500)));

        BaseBarrier.registerBarrier(new Ground(Vector.build(0, 680)));
    }
}
