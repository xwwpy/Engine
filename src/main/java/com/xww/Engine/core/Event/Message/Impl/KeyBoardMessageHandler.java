package com.xww.Engine.core.Event.Message.Impl;


import com.xww.Engine.core.Event.Message.Message;
import com.xww.Engine.core.Event.Message.MessageHandler;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.Camera;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.DebugSetting;
import com.xww.Engine.setting.FrameSetting;

import java.awt.event.KeyEvent;


public class KeyBoardMessageHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        if (message.getMessageType().equals(Message.MessageType.KeyBoard)){
            KeyEvent msg = (KeyEvent) message.getMessage();
            if (msg == null) return;
            if (msg.getID() == KeyEvent.KEY_PRESSED){
                processKeyPressed(msg);
            }else if (msg.getID() == KeyEvent.KEY_RELEASED){
                processKeyReleased(msg);
            }
        }
    }

    @Override
    public boolean checkValid() {
        return true;
    }

    public static void processKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                Camera.velocity.add_to_self(Vector.build(0, -10));
                break;
            case KeyEvent.VK_DOWN:
                Camera.velocity.add_to_self(Vector.build(0, 10));
                break;
            case KeyEvent.VK_LEFT:
                Camera.velocity.add_to_self(Vector.build(-10, 0));
                break;
            case KeyEvent.VK_RIGHT:
                Camera.velocity.add_to_self(Vector.build(10, 0));
                break;
            case KeyEvent.VK_C:
                Camera.velocity = Vector.Zero();
                Camera.camera_position = Vector.Zero();
                break;
            case KeyEvent.VK_M:
                break;
        }
    }
    public static void processKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP | KeyEvent.VK_DOWN | KeyEvent.VK_LEFT | KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_SPACE:
                DebugSetting.IS_DEBUG_ON = !DebugSetting.IS_DEBUG_ON;
                break;
            case KeyEvent.VK_F:
                GameFrame.context.changeFps(FrameSetting.DEFAULT_FPS * 2);
                break;
            case KeyEvent.VK_G:
                if (FrameSetting.DEFAULT_FPS <= 2){
                    FrameSetting.DEFAULT_FPS = 1;
                } else GameFrame.context.changeFps(FrameSetting.DEFAULT_FPS / 2);
                break;

        }
    }
}
