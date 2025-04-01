package com.xww.projects.game02.content.GameScene;

import com.xww.Engine.core.Event.Message.Message;
import com.xww.Engine.core.Scene.BaseScene;
import com.xww.Engine.core.Scene.SceneManager;
import com.xww.projects.game02.Main;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayerWinScene implements BaseScene {
    @Override
    public void on_enter() {

    }

    @Override
    public void on_update(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("微软雅黑", Font.BOLD, 50));
        g.drawString("You Win!", 100, 100);
        g.drawString("按回车键重新开始", 100, 200);
        g.drawString("按ESC键回到菜单", 100, 300);
    }

    @Override
    public void processMessage(Message message) {
        if (message.getMessageType() == Message.MessageType.KeyBoard){
            KeyEvent keyEvent = (KeyEvent) message.getMessage();
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                Main.level += 1;
                SceneManager.sceneManagerIns.setCurrentScene("game");
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
                SceneManager.sceneManagerIns.setCurrentScene("menu");
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
