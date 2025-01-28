package com.xww.projects.game02;

import com.xww.Engine.core.Component.Component;

import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.projects.game02.content.Player;

public class Main {
    public static void main(String[] args) {
        GameFrame.init();
        initGame();
        GameFrame.start();
    }

    public static void initGame() {
        Player player = new Player(Vector.build(1400, 900));
        Component.addComponent(player);
    }
}
