package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;

public class BossSquatState extends StateNode {
    private final Animation squatLeftAnimation;
    private final Animation squatRightAnimation;

    public BossSquatState(Character owner, Animation squatLeftAnimation, Animation squatRightAnimation) {
        super(owner, 1, null);
        this.squatLeftAnimation = squatLeftAnimation;
        this.squatRightAnimation = squatRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        // TODO
        return true;
    }

    @Override
    public boolean whetherSuitThisState() {
        // TODO
        return false;
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
