package com.xww.projects.game02.content.Player.states;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;

public class PlayerRunState extends StateNode {
    protected Animation runLeftAnimation;
    protected Animation runRightAnimation;
    public PlayerRunState(Character owner, Animation runLeftAnimation, Animation runRightAnimation) {
        super(owner, 1, null);
        this.runLeftAnimation = runLeftAnimation;
        this.runRightAnimation = runRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return owner.isRolling() || owner.getVelocity().getFullX() == 0 || owner.getVelocity().getFullY() != 0;
    }

    @Override
    public boolean whetherSuitThisState() {
        return !whetherEnding();
    }

    @Override
    public void on_enter() {
        runLeftAnimation.reset_animation();
        runRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return runLeftAnimation;
        } else {
            return runRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
