package com.xww.projects.game02.content.Player.states;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;

public class PlayerJumpState extends StateNode {
    protected Animation jumpLeftAnimation;
    protected Animation jumpRightAnimation;

    public PlayerJumpState(Character owner, Animation jumpLeftAnimation, Animation jumpRightAnimation) {
        super(owner, 10, null, "jump_state");
        this.jumpLeftAnimation = jumpLeftAnimation;
        this.jumpRightAnimation = jumpRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !whetherSuitThisState();
    }

    @Override
    public boolean whetherSuitThisState() {
        return owner.getVelocity().getFullY() < 0;
    }

    @Override
    public void on_enter() {
        jumpLeftAnimation.reset_animation();
        jumpRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return jumpLeftAnimation;
        } else {
            return jumpRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
