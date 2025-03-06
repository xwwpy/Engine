package com.xww.Engine.core.StateManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StateMachine {

    private final Map<String, StateNode> statePool;
    private StateNode currentState;

    private boolean needInit = true;

    public void update(Graphics g, Component owner) {
        selectState(owner);
        if (currentState == null) return;
        if (needInit) {
            currentState.on_enter();
            needInit = false;
        }
        currentState.on_update(g, owner);
    }

    protected void selectState(Component owner) {

    }

    public void set_entry(String id){
        currentState = statePool.get(id);
    }

    public void switch_to(String id){
        if (currentState != null) {
            currentState.on_exit();
        }
        currentState = statePool.get(id);
        if (currentState != null) currentState.on_enter();
    }

    public StateMachine() {
        this.statePool = new HashMap<>();
    }

    public void registerState(String id, StateNode state) {
        statePool.put(id, state);
    }

}
