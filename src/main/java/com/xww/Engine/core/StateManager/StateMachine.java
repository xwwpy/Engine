package com.xww.Engine.core.StateManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

// TODO
public class StateMachine {

    private final Map<String, StateNode> statePool;
    private StateNode currentState;

    private boolean needInit = true;

    public void update(Graphics g, Component owner) {
        if (currentState == null) return;
        if (needInit) {
            currentState.on_enter();
            needInit = false;
        }
        currentState.on_update(g, owner);
        if (currentState.whetherEnding()) {
            currentState.on_exit();
            currentState = currentState.selectNextState();
            if (currentState == null) {
                throw new RuntimeException("StateMachine: currentState is null");
            } else {
                currentState.on_enter();
            }
        }
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
