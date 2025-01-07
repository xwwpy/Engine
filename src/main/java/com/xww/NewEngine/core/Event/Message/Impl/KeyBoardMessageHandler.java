package com.xww.NewEngine.core.Event.Message.Impl;


import com.xww.NewEngine.core.Event.Message.Message;
import com.xww.NewEngine.core.Event.Message.MessageHandler;
import com.xww.NewEngine.gui.GameFrame;
import com.xww.NewEngine.setting.DebugSetting;
import com.xww.NewEngine.setting.FrameSetting;

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
                break;
            case KeyEvent.VK_DOWN:
                break;
            case KeyEvent.VK_LEFT:
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_C:
                break;
            case KeyEvent.VK_M:
                break;
        }
    }
    public static void processKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP | KeyEvent.VK_DOWN | KeyEvent.VK_LEFT | KeyEvent.VK_RIGHT:
//                        camera.velocity.clear();
                break;
            case KeyEvent.VK_SPACE:
                DebugSetting.IS_DEBUG_ON = !DebugSetting.IS_DEBUG_ON;
                break;
            case KeyEvent.VK_F:
                GameFrame.context.changeFps(FrameSetting.DEFAULT_FPS * 2);
                break;
            case KeyEvent.VK_G:
                GameFrame.context.changeFps(FrameSetting.DEFAULT_FPS / 2);
                if (FrameSetting.DEFAULT_FPS <= 2){
                    FrameSetting.DEFAULT_FPS = 1;
                }
                break;

        }
    }
}
