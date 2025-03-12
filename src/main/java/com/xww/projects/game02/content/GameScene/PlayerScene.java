package com.xww.projects.game02.content.GameScene;

import com.xww.Engine.core.Scene.GameScene;
import com.xww.Engine.core.Scene.SceneManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.projects.game02.Main;

import java.awt.*;

public class PlayerScene extends GameScene {
    public static boolean running = true;
    public static String targetScene;

    @Override
    public void on_enter() {
        running = true;
        targetScene = null;
        MP3Player.getInstance().setWhetherCloseMusic(false);
        super.on_enter();
        Main.initGame();
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        if (!running) {
            SceneManager.sceneManagerIns.setCurrentScene(targetScene);
        }
    }

    @Override
    public void on_exit() {
        super.on_exit();
        MP3Player.getInstance().setWhetherCloseMusic(true);
    }
}
