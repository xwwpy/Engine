package com.xww.NewEngine.core.Event.Message.Impl;


import com.xww.NewEngine.core.Component.Component;
import com.xww.NewEngine.core.Event.Message.Message;
import com.xww.NewEngine.core.Event.Message.MessageHandler;

import java.awt.event.MouseEvent;

public class MouseMessageHandler implements MessageHandler {

    private static boolean checked = false;
    private static Component checked_object = null;
    @Override
    public void handle(Message message) {
        Message.MessageType type = message.getMessageType();
        switch (type) {
            case MouseClicked -> processMouseClicked((MouseEvent) message.getMessage());
            case MousePressed -> processMousePressed((MouseEvent) message.getMessage());
            case MouseReleased -> processMouseReleased((MouseEvent) message.getMessage());
            case MouseEnter -> processMouseEnter((MouseEvent) message.getMessage());
            case MouseExit -> processMouseExit((MouseEvent) message.getMessage());
//            case MouseMoved -> {
//                processMouseMoved((MouseEvent) message.getMessage());
//            }
            default -> {
            } // do nothing
        }
    }

    public void processMouseMoved(MouseEvent message) {
        if (checked && checked_object != null){
            int x = message.getX();
            int y = message.getY();
            checked_object.process_mouse_choose_self(x, y);
        }
    }

    private static void processMouseExit(MouseEvent message) {

    }

    private static void processMouseEnter(MouseEvent message) {


    }

    private static void processMouseReleased(MouseEvent message) {
        checked = false;
        if (checked_object != null) {
            checked_object.process_mouse_release();
            checked_object = null;
        }
    }

    private static void processMousePressed(MouseEvent message) {
        int x = message.getX();
        int y = message.getY() - 29; // 减去标题栏高度
        for (Component object : Component.can_drag_object){
            if(object.whether_mouse_in(x, y)){
                checked = true;
                object.process_mouse_choose_self(x, y + 29);
                checked_object = object;
                return;
            }
        }
    }

    private static void processMouseClicked(MouseEvent message) {
        int x = message.getX();
        int y = message.getY() - 29; // 减去标题栏高度
        for (Component object : Component.allComponents){
            if(object.whether_mouse_in(x, y)){
                object.whetherCanDrag = !object.whetherCanDrag;
                return;
            }
        }
    }

    @Override
    public boolean checkValid() {
        return true;
    }


}
