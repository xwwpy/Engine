package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;

public class BossIdleState extends StateNode {
    protected Animation idleLeftAnimation;
    protected Animation idleRightAnimation;
    public BossIdleState(Character owner, Animation idleLeftAnimation, Animation idleRightAnimation) {
        super(owner, 0, null);
        this.idleLeftAnimation = idleLeftAnimation;
        this.idleRightAnimation = idleRightAnimation;
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
        idleLeftAnimation.reset_animation();
        idleRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return idleLeftAnimation;
        } else {
            return idleRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
