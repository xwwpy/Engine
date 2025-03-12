package com.xww.Engine.core.Scene;

import com.xww.Engine.core.Barrier.BaseGround;
import com.xww.Engine.core.Barrier.BaseWall;
import com.xww.Engine.core.Collision.CollisionHandler;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Event.Message.Impl.KeyBoardMessageHandler;
import com.xww.Engine.core.Event.Message.Impl.MouseMessageHandler;
import com.xww.Engine.core.Event.Message.Message;
import com.xww.Engine.core.Timer.TimerManager;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameScene implements BaseScene{

    // 用于指定 重置场景的回调函数
    public static Runnable resetRunnable;
    // 用于指定 退出场景的回调函数
    public static Runnable exitRunnable;

    @Override
    public void on_enter() {
        GameFrame.addNecessaryComponent();
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
        if (resetRunnable != null) {
            resetRunnable.run();
        }
    }

    @Override
    public void on_exit() {
        if (exitRunnable != null) {
            exitRunnable.run();
        }
        Component.components.clear();
        Component.components_to_remove.clear();
        Component.components_to_add.clear();
        Component.allComponents.clear();
        Component.allComponents_to_add.clear();
        Component.allComponents_to_remove.clear();
        TimerManager.instance.clear();
        MouseMessageHandler.mouseMessageHandlerInstance.clear();
        KeyBoardMessageHandler.keyBoardMessageHandlerInstance.clear();
        CollisionHandler.clear();
        BaseGround.clear();
        BaseWall.clear();
    }

    private static void updateComponent(Graphics g) {
        com.xww.Engine.core.Component.Component.components.addAll(com.xww.Engine.core.Component.Component.components_to_add);

        com.xww.Engine.core.Component.Component.components_to_add.clear();


        com.xww.Engine.core.Component.Component.components_to_remove.forEach(component -> {
            com.xww.Engine.core.Component.Component.components.remove(component);
        });
        com.xww.Engine.core.Component.Component.components_to_remove.clear();
        com.xww.Engine.core.Component.Component.components.stream().sorted().forEach(component -> {
            component.on_update(g);
        });
        com.xww.Engine.core.Component.Component.allComponents.addAll(com.xww.Engine.core.Component.Component.allComponents_to_add);
        com.xww.Engine.core.Component.Component.allComponents_to_add.clear();
        Component.allComponents_to_remove.forEach(component -> {
            component.on_destroy();
            com.xww.Engine.core.Component.Component.allComponents.remove(component);
        });
        com.xww.Engine.core.Component.Component.allComponents_to_remove.clear();
        // 更新拖拽组件
        Component.updateDragComponents();
        // 更新障碍物
        BaseGround.updateBarriers(g);
        BaseWall.updateWalls(g);
        // 更新碰撞器
        CollisionHandler.update();
    }
}
