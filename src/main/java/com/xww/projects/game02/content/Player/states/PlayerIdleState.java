package com.xww.projects.game02.content.Player.states;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;


public class PlayerIdleState extends StateNode {
    
    private final Animation idleLeft;
    private final Animation idleRight;

    public PlayerIdleState(Character owner, Animation idleLeft, Animation idleRight) {
        super(owner, 0, null);
        this.idleLeft = idleLeft;
        this.idleRight = idleRight;
    }

    @Override
    public boolean whetherEnding() {
        return true;
    }

    @Override
    public boolean whetherSuitThisState() {
        return true;
    }

    @Override
    public void on_enter() {
        idleLeft.reset_animation();
        idleRight.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return idleLeft;
        } else {
            return idleRight;
        }
    }

    @Override
    public void on_exit() {
    }
}
