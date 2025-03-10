package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;

public class BossDeadState extends StateNode {
    private final Animation deadLeftAnimation;
    private final Animation deadRightAnimation;
    public BossDeadState(Character owner, Animation deadLeftAnimation, Animation deadRightAnimation) {
        super(owner, 1000, null);
        this.deadLeftAnimation = deadLeftAnimation;
        this.deadRightAnimation = deadRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return owner.getCurrentHp() > 0;
    }

    @Override
    public boolean whetherSuitThisState() {
        return !whetherEnding();
    }

    @Override
    public void on_enter() {
        deadLeftAnimation.reset_animation();
        deadRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return deadLeftAnimation;
        } else {
            return deadRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
