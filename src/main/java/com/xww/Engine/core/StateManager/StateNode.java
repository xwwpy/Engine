package com.xww.Engine.core.StateManager;

import com.xww.Engine.core.Actor.Character;

import java.awt.*;

// TODO
public abstract class StateNode {
    protected Character owner; // 状态所属角色

    protected int order; // 状态的优先级，值越大优先级越高

    protected StateNode[] nextStates; // 可能进行跳转的下一状态

    public abstract boolean whetherEnding(); // 状态是否结束

    public abstract boolean whetherSuitThisState(); // 状态是否适合当前角色

    public StateNode selectNextState() {
        if (nextStates != null){
            StateNode targetState = null;
            int curStateOrder = -1;
            for (StateNode nextState : nextStates) {
                if (nextState.whetherSuitThisState()) {
                    if (targetState == null) {
                        targetState = nextState;
                        curStateOrder = nextState.order;
                        continue;
                    }
                    if (nextState.order > curStateOrder) {
                        targetState = nextState;
                        curStateOrder = nextState.order;
                    }
                }
            }
            return targetState;
        }
        return null;
    }
    public abstract void on_enter();

    public abstract void on_update(Graphics g, Component owner);

    public abstract void on_exit();
}
