package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

public class BossFallState extends StateNode {
    private final Animation fallLeftAnimation;
    private final Animation fallRightAnimation;
    public BossFallState(Character owner, Animation fallLeftAnimation, Animation fallRightAnimation) {
        super(owner, 1, null);
        this.fallLeftAnimation = fallLeftAnimation;
        this.fallRightAnimation = fallRightAnimation;
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
                && !boss.isWhetherOnGround()
                && boss.getVelocity().getFullY() > 0;
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
