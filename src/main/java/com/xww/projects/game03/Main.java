package com.xww.projects.game03;

import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Scene.SceneManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.gui.GameFrame;
import com.xww.projects.game03.Scenes.CurrentGameScene;

public class Main {
    public static void main(String[] args) {
        GameFrame.init(Main::initGame);
        GameFrame.start();
        MP3Player.getInstance().shutdown();
    }

    public static void initGame() {
        SceneManager.sceneManagerIns.addScene("game", new CurrentGameScene());
        SceneManager.sceneManagerIns.setCurrentScene("game");
        loadResources();
    }

    private static void loadResources() {
        ResourceManager instance = ResourceManager.getInstance();
        instance.loadAllAtlas("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game03/Asset/Animations", "%d", 0, null, ".png");
        instance.atlasPool.forEach((name, atlas) -> {
            System.out.println(name + " - " + atlas.getSize());
        });
        System.out.println(instance.atlasPool.size());
    }
}
