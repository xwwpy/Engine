package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

public class BossDashInAirState extends StateNode {

    private final Animation dashInAirLeftAnimation;
    private final Animation dashInAirRightAnimation;

    public BossDashInAirState(Character owner, Animation dashInAirLeftAnimation, Animation dashInAirRightAnimation) {
        super(owner, 1, null);
        this.dashInAirLeftAnimation = dashInAirLeftAnimation;
        this.dashInAirRightAnimation = dashInAirRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !whetherSuitThisState();
    }

    @Override
    public boolean whetherSuitThisState() {
        return ((Boss)owner).isWhetherDashingInAir();
    }

    @Override
    public void on_enter() {
        dashInAirLeftAnimation.reset_animation();
        dashInAirRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return dashInAirLeftAnimation;
        } else {
            return dashInAirRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
