package com.xww.projects.game02.content.Player.states;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;

public class PlayerRollState extends StateNode {
    protected Animation rollLeftAnimation;
    protected Animation rollRightAnimation;
    public PlayerRollState(Character owner, Animation rollLeftAnimation, Animation rollRightAnimation) {
        super(owner, 20, null);
        this.rollLeftAnimation = rollLeftAnimation;
        this.rollRightAnimation = rollRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !owner.isRolling();
    }

    @Override
    public boolean whetherSuitThisState() {
        return owner.isRolling() && owner.isWhetherOnGround();
    }

    @Override
    public void on_enter() {
        rollLeftAnimation.reset_animation();
        rollRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return rollLeftAnimation;
        } else {
            return rollRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
