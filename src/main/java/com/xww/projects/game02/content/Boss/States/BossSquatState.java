package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;

import java.awt.*;

public class BossSquatState extends StateNode {
    private final Animation squatLeftAnimation;
    private final Animation squatRightAnimation;

    public BossSquatState(Character owner, Animation squatLeftAnimation, Animation squatRightAnimation) {
        super(owner, 1, null, "squatState");
        this.squatLeftAnimation = squatLeftAnimation;
        this.squatRightAnimation = squatRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return false;
    }

    @Override
    public boolean whetherSuitThisState() {
        return false;
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        if (this.owner.getCurrentHp() <= 0){
            owner.getStateMachine().forceSwitch("deadState");
        }
        if (this.currentAnimation.isOver()){
            owner.getStateMachine().forceSwitch("dashOnFloorState");
        }
    }
    @Override
    public void on_enter() {
        squatLeftAnimation.reset_animation();
        squatRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return squatLeftAnimation;
        } else {
            return squatRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
