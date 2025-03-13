package com.xww.projects.game02.content.GameScene;

import com.xww.Engine.core.Event.Message.Message;
import com.xww.Engine.core.Scene.BaseScene;
import com.xww.Engine.core.Scene.SceneManager;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuScene implements BaseScene {
    @Override
    public void on_enter() {
    }

    @Override
    public void on_update(Graphics g) {
        g.setColor(Color.WHITE);
        Font font = g.getFont();
        g.setFont(new Font("微软雅黑", Font.BOLD, 50));
        g.drawString("按回车键开始", 100, 200);
        g.setFont(font);
    }

    @Override
    public void processMessage(Message message) {
        if (message.getMessageType() == Message.MessageType.KeyBoard){
            KeyEvent keyEvent = (KeyEvent) message.getMessage();
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                SceneManager.sceneManagerIns.setCurrentScene("game");
            }
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void on_exit() {

    }
}
