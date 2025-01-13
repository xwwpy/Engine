package com.xww.Engine.core.Event.Message.Impl;


import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Event.Message.Message;
import com.xww.Engine.core.Event.Message.MessageHandler;

import java.awt.event.MouseEvent;

public class MouseMessageHandler extends MessageHandler {

    public static MouseMessageHandler mouseMessageHandlerInstance = new MouseMessageHandler();
    private static boolean checked = false;
    private static Component checked_object = null;

    private MouseMessageHandler(){}

    @Override
    public void handle(Message message) {
        Message.MessageType type = message.getMessageType();
        switch (type) {
            case MouseClicked -> processMouseClicked((MouseEvent) message.getMessage());
            case MousePressed -> processMousePressed((MouseEvent) message.getMessage());
            case MouseReleased -> processMouseReleased((MouseEvent) message.getMessage());
            case MouseEnter -> processMouseEnter((MouseEvent) message.getMessage());
            case MouseExit -> processMouseExit((MouseEvent) message.getMessage());
            case MouseMoved -> processMouseMoved((MouseEvent) message.getMessage());
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
        try {
            Component.can_drag_object.stream().sorted().forEach(object -> {
                if(object.whether_mouse_in(x, y)){
                    checked = true;
                    checked_object = object;
                    throw new RuntimeException();
                }
            });
        } catch (RuntimeException e) {
            // do nothing
        }
    }

    private static void processMouseClicked(MouseEvent message) {
        int x = message.getX();
        int y = message.getY() - 29; // 减去标题栏高度
        try {
            Component.allComponents.stream().sorted().forEach(object -> {
                if(object.whether_mouse_in(x, y)){
                    if (message.getClickCount() == 3) {
                        object.setWhetherShowDebugInfo(!object.isWhetherShowDebugInfo());
                        throw new RuntimeException();
                    }
                    object.setWhetherCanDrag(!object.whetherCanDrag);
                    throw new RuntimeException();
                }
            });
        } catch (RuntimeException e) {
            // do nothing
        }
    }

    @Override
    public boolean checkValid() {
        return true;
    }

}
