package com.xww.Engine.core.Event.Message;

import com.xww.Engine.core.Component.Component;

import java.util.HashSet;
import java.util.Set;

public abstract class MessageHandler {
    protected final Set<Component> components_to_add = new HashSet<>();
    public final Set<Component> components = new HashSet<>();
    protected final Set<Component> components_to_remove = new HashSet<>();


    public abstract void handle(Message message);

    /**
     *
     * @return 当返回false时，表示消息处理完毕，可以移除该消息处理器
     */
    public abstract boolean checkValid();

    public  void updateComponents() {
        components.addAll(components_to_add);
        components_to_add.clear();
        components.stream().sorted().forEach(component -> {
            if (!component.isAlive()){
                components_to_remove.add(component);
            }
        });
        components.removeAll(components_to_remove);
        components_to_remove.clear();
    }

    public void registerComponent(Component component) {
        components_to_add.add(component);
    }

    public void unregisterComponent(Component component) {
        components_to_remove.add(component);
    }

    public void clear() {
        components_to_add.clear();
        components_to_remove.clear();
        components.clear();
    }
}
