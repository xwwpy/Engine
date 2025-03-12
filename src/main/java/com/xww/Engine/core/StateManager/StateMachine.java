package com.xww.Engine.core.StateManager;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StateMachine {

    protected Map<String, StateNode> stateNodePool;
    private StateNode currentState;

    private String currentStateId;

    private boolean needInit = true;

    private boolean whetherBannedForceSwitch = false;

    public StateMachine(StateNode entryState) {
        this.currentState = entryState;
        this.stateNodePool = new HashMap<>();
    }

    public void register(String id, StateNode stateNode) {
        this.stateNodePool.put(id, stateNode);
    }

    public void forceSwitch(String id) {
        if (whetherBannedForceSwitch) return;
        if (this.stateNodePool.containsKey(id)) {
            if (this.currentState != null) {
                this.currentState.on_exit();
            }
            this.currentState = this.stateNodePool.get(id);
            currentStateId = id;
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
                this.currentStateId = currentState.id;
                currentState.on_enter();
            }
        }
    }

    public StateNode getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String id) {
        forceSwitch(id);
    }

    public String getCurrentStateId() {
        return currentStateId;
    }

    public void bannedForceSwitch() {
        whetherBannedForceSwitch = true;
    }
}
