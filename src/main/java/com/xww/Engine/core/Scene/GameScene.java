package com.xww.Engine.core.Scene;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Event.Message.Impl.KeyBoardMessageHandler;
import com.xww.Engine.core.Event.Message.Impl.MouseMessageHandler;
import com.xww.Engine.core.Event.Message.Message;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameScene implements BaseScene{

    @Override
    public void on_enter() {
        this.reset();
    }

    @Override
    public void on_update(Graphics g) {
        updateComponent(g);
    }

    @Override
    public void processMessage(Message message) {
        MouseMessageHandler.mouseMessageHandlerInstance.updateComponents();
        KeyBoardMessageHandler.keyBoardMessageHandlerInstance.updateComponents();
        if (message.getMessage() instanceof MouseEvent mouseEvent){
            MouseMessageHandler.mouseMessageHandlerInstance.components.stream().sorted().forEach(component -> {
                component.processMouseEvent(mouseEvent);
            });
        } else if (message.getMessage() instanceof KeyEvent keyEvent) {
            KeyBoardMessageHandler.keyBoardMessageHandlerInstance.components.stream().sorted().forEach(component -> {
                component.processKeyEvent(keyEvent);
            });
        } else {
            throw new RuntimeException("Unknown message type");
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void on_exit() {

    }

    private static void updateComponent(Graphics g) {
        com.xww.Engine.core.Component.Component.components.addAll(com.xww.Engine.core.Component.Component.components_to_add);
        com.xww.Engine.core.Component.Component.allComponents_to_add.addAll(com.xww.Engine.core.Component.Component.components_to_add);

        com.xww.Engine.core.Component.Component.components_to_add.clear();

        com.xww.Engine.core.Component.Component.allComponents_to_remove.addAll(com.xww.Engine.core.Component.Component.components_to_remove);

        com.xww.Engine.core.Component.Component.components_to_remove.forEach(component -> {
            component.on_destroy();
            com.xww.Engine.core.Component.Component.components.remove(component);
        });
        com.xww.Engine.core.Component.Component.components_to_remove.clear();
        com.xww.Engine.core.Component.Component.components.stream().sorted().forEach(component -> {
            component.on_update(g);
        });
        com.xww.Engine.core.Component.Component.allComponents.addAll(com.xww.Engine.core.Component.Component.allComponents_to_add);
        com.xww.Engine.core.Component.Component.allComponents_to_add.clear();
        com.xww.Engine.core.Component.Component.allComponents.removeAll(com.xww.Engine.core.Component.Component.allComponents_to_remove);
        com.xww.Engine.core.Component.Component.allComponents_to_remove.clear();
        // 更新拖拽组件
        Component.updateDragComponents();
    }
}
