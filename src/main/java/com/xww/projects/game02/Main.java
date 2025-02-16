package com.xww.projects.game02;

import com.xww.Engine.core.Animation.Atlas;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;

import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.projects.game02.content.BackGroundComponent;
import com.xww.projects.game02.content.Player;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 加载资源
        loadResources();
        GameFrame.init();
        initGame();
        GameFrame.start();
    }

    private static void loadResources(){
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
    }

    public static void initGame() {
        Component.addComponent(new BackGroundComponent());
        for (int i = 0; i < 3; i++) {
            Player player = new Player(Vector.build(100, 200));
            if (i % 2 == 0){
                player.setAnimation("roll");
            }
            player.addCollider(new RectCollider(Vector.build(38, 55), player, Vector.build(92, 440 - 366)));
            Component.addComponent(player);
        }
    }
}
