package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

public class BossRunState extends StateNode {
    private final Animation runLeftAnimation;
    private final Animation runRightAnimation;
    public BossRunState(Character owner, Animation runLeftAnimation, Animation runRightAnimation) {
        super(owner, 1, null);
        this.runLeftAnimation = runLeftAnimation;
        this.runRightAnimation = runRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !whetherSuitThisState();
    }

    @Override
    public boolean whetherSuitThisState() {
        Boss boss = (Boss) owner;
        return !boss.isWhetherDashingOnFloor()
                && !boss.isWhetherDashingInAir()
                && boss.getVelocity().getFullX() != 0;
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
