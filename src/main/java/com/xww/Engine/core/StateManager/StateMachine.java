package com.xww.Engine.core.StateManager;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StateMachine {

    protected Map<String, StateNode> stateNodePool;
    private StateNode currentState;

    private boolean needInit = true;

    public StateMachine(StateNode entryState) {
        this.currentState = entryState;
        this.stateNodePool = new HashMap<>();
    }

    public void register(String id, StateNode stateNode) {
        this.stateNodePool.put(id, stateNode);
    }

    public void forceSwitch(String id) {
        if (this.stateNodePool.containsKey(id)) {
            this.currentState = this.stateNodePool.get(id);
            needInit = true;
        }
    }
    public void update(Graphics g) {
        if (currentState == null) return;
        if (needInit) {
            currentState.on_enter();
            needInit = false;
        }
        currentState.on_update(g);
        if (currentState.whetherEnding()) {
            StateNode tempNode = currentState.selectNextState();
            if (tempNode != currentState) {
                currentState.on_exit();
                currentState = tempNode;
                currentState.on_enter();
            }
        }
    }

    public StateNode getCurrentState() {
        return currentState;
    }

    public void setCurrentState(StateNode currentState) {
        this.currentState = currentState;
    }
}
