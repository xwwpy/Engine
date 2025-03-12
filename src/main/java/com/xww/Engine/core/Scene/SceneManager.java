package com.xww.Engine.core.Scene;


import com.xww.Engine.core.Event.Message.Message;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    // 所有的场景集合
    private final Map<String, BaseScene> scenes = new HashMap<>();

    private BaseScene currentScene;
    public static final SceneManager sceneManagerIns = new SceneManager();
//    static {
//        sceneManagerIns.addScene("game", new GameScene());
//        sceneManagerIns.setCurrentScene("game");
//    }
    private SceneManager(){}

    public void addScene(String name, BaseScene scene){
        scenes.put(name, scene);
    }
    public void removeScene(String name){
        scenes.remove(name);
    }
    public void on_update(Graphics g){
        if (currentScene != null) {
            currentScene.on_update(g);
        }
    }

    public void setCurrentScene(String name){
        if (currentScene != null) {
            currentScene.on_exit();
        }
        currentScene = scenes.get(name);
        currentScene.on_enter();
    }

    public void processMessage(Message msg){
        if (currentScene != null) {
            currentScene.processMessage(msg);
        }
    }
}
