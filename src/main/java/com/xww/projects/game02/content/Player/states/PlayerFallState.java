package com.xww.projects.game02.content.Player.states;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;

public class PlayerFallState extends StateNode {
    protected Animation fallRightAnimation;
    protected Animation fallLeftAnimation;
    public PlayerFallState(Character owner, Animation fallLeftAnimation, Animation fallRightAnimation) {
        super(owner, 10, null);
        this.fallLeftAnimation = fallLeftAnimation;
        this.fallRightAnimation = fallRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !whetherSuitThisState();
    }

    @Override
    public boolean whetherSuitThisState() {
        return owner.getVelocity().getFullY() > 0;
    }

    @Override
    public void on_enter() {
        fallLeftAnimation.reset_animation();
        fallRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return fallLeftAnimation;
        } else {
            return fallRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
