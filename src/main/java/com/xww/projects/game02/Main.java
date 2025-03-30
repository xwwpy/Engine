package com.xww.projects.game02;

import com.xww.Engine.core.Animation.Atlas;
import com.xww.Engine.core.Barrier.BaseGround;
import com.xww.Engine.core.Component.Component;

import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Scene.SceneManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.FrameSetting;
import com.xww.projects.game02.content.BackGroundComponent;
import com.xww.projects.game02.content.Boss.Boss;
import com.xww.projects.game02.content.GameScene.MenuScene;
import com.xww.projects.game02.content.GameScene.PlayerFailureScene;
import com.xww.projects.game02.content.GameScene.PlayerScene;
import com.xww.projects.game02.content.GameScene.PlayerWinScene;
import com.xww.projects.game02.content.Player.Player;
import com.xww.Engine.core.Barrier.impl.Ground;
import com.xww.Engine.core.Barrier.impl.Wall;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 加载资源
        GameFrame.init(()->{
            loadResources();
            SceneManager.sceneManagerIns.addScene("game", new PlayerScene());
            SceneManager.sceneManagerIns.addScene("menu", new MenuScene());
            SceneManager.sceneManagerIns.addScene("playerFailure", new PlayerFailureScene());
            SceneManager.sceneManagerIns.addScene("playerWin", new PlayerWinScene());
            SceneManager.sceneManagerIns.setCurrentScene("menu");
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
        MP3Player.getInstance().setBGMPath(ResourceManager.getInstance().findAudioPath("bgm"));
        MP3Player.getInstance().startBGM();
        Component.addComponent(new BackGroundComponent());
        Player player = new Player(Vector.build(100, 400));
        Ground ground = new Ground(Vector.build(0, 680));
        ground.setWhetherCanDown(false);
        BaseGround.registerBarrier(ground);
        new Wall(Vector.build(-10, 0), Vector.build(10, FrameSetting.DEFAULT_HEIGHT));
        new Wall(Vector.build(FrameSetting.DEFAULT_WIDTH, 0), Vector.build(10, FrameSetting.DEFAULT_HEIGHT));
        new Boss(Vector.build(300, 300), player);
        new Boss(Vector.build(900, 300), player);
    }
}
