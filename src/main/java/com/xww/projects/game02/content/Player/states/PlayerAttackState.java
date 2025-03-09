package com.xww.projects.game02.content.Player.states;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;

public class PlayerAttackState extends StateNode {
    protected Animation atkLeftAnimation;
    protected Animation atkRightAnimation;
    public PlayerAttackState(Character owner, Animation atkLeftAnimation, Animation atkRightAnimation) {
        super(owner, 100, null);
        this.atkLeftAnimation = atkLeftAnimation;
        this.atkRightAnimation = atkRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !owner.isWhetherAtking();
    }

    @Override
    public boolean whetherSuitThisState() {
        return owner.isWhetherAtking();
    }

    @Override
    public void on_enter() {
        this.atkLeftAnimation.reset_animation();
        this.atkRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return atkLeftAnimation;
        } else {
            return atkRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
