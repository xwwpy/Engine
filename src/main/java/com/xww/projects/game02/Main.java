package com.xww.projects.game02;

import com.xww.Engine.core.Component.Component;

import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.FrameSetting;
import com.xww.projects.game02.content.Player;

public class Main {
    public static void main(String[] args) {
        GameFrame.init();
        initGame();
        GameFrame.start();
    }

    public static void initGame() {
        for (int i = 0; i < 50; i++) {
            Player player = new Player(Vector.build(100, 200));
            Component.addComponent(player);
        }
    }
}
