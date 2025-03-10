package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

public class BossAimState extends StateNode {
    private final Animation aimLeftAnimation;
    private final Animation aimRightAnimation;
    public BossAimState(Character owner, Animation aimLeftAnimation, Animation aimRightAnimation) {
        super(owner, 100, null);
        this.aimLeftAnimation = aimLeftAnimation;
        this.aimRightAnimation = aimRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !whetherSuitThisState();
    }

    @Override
    public boolean whetherSuitThisState() {
        return ((Boss)owner).isWhetherAiming();
    }

    @Override
    public void on_enter() {
        aimLeftAnimation.reset_animation();
        aimRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return aimLeftAnimation;
        } else {
            return aimRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
