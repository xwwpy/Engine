package com.xww.Engine.core.StateManager;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;

import java.awt.*;

public abstract class StateNode {

    protected Character owner; // 状态所属角色
    protected int order; // 状态的优先级，值越大优先级越高


    protected Animation currentAnimation;

    protected StateNode[] nextStates; // 可能进行跳转的下一状态

    public abstract boolean whetherEnding(); // 状态是否结束

    public abstract boolean whetherSuitThisState(); // 状态是否适合当前角色

    public String id;


    public StateNode(Character owner, int order, StateNode[] nextStates, String id) {
        this.owner = owner;
        this.order = order;
        this.nextStates = nextStates;
        this.id = id;
    }

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
            if (targetState == null) return this;
            else return targetState;
        }
        return this;
    }
    public abstract void on_enter();

    public void on_update(Graphics g){
        Animation temp = this.selectCurrentAnimation();
        if (temp != currentAnimation) {
            currentAnimation = temp;
            currentAnimation.reset_animation();
        }
        on_render(g);
    }

    protected abstract Animation selectCurrentAnimation();

    public void on_render(Graphics g) {
        if (currentAnimation != null && owner.isWhetherRender()) currentAnimation.on_update(g);
    }

    public abstract void on_exit();

    public Character getOwner() {
        return owner;
    }

    public void setOwner(Character owner) {
        this.owner = owner;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    public StateNode[] getNextStates() {
        return nextStates;
    }

    public void setNextStates(StateNode[] nextStates) {
        this.nextStates = nextStates;
    }
}
