package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

public class BossJumpState extends StateNode {

    private final Animation jumpLeftAnimation;
    private final Animation jumpRightAnimation;
    public BossJumpState(Character owner, Animation jumpLeftAnimation, Animation jumpRightAnimation) {
        super(owner, 1, null);
        this.jumpLeftAnimation = jumpLeftAnimation;
        this.jumpRightAnimation = jumpRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !whetherSuitThisState();
    }

    @Override
    public boolean whetherSuitThisState() {
        Boss boss = (Boss) owner;
        return !boss.isWhetherAiming()
                && !boss.isWhetherDashingOnFloor()
                && !boss.isWhetherDashingInAir()
                && boss.getVelocity().getFullY() < 0;
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
