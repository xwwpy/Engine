package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

public class BossDashOnFloorState extends StateNode {

    private final Animation dashOnFloorLeftAnimation;
    private final Animation dashOnFloorRightAnimation;

    public BossDashOnFloorState(Character owner, Animation dashOnFloorLeftAnimation, Animation dashOnFloorRightAnimation) {
        super(owner, 1, null);
        this.dashOnFloorLeftAnimation = dashOnFloorLeftAnimation;
        this.dashOnFloorRightAnimation = dashOnFloorRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !whetherSuitThisState();
    }

    @Override
    public boolean whetherSuitThisState() {
        return ((Boss)owner).isWhetherDashingOnFloor();
    }

    @Override
    public void on_enter() {
        dashOnFloorLeftAnimation.reset_animation();
        dashOnFloorRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return dashOnFloorLeftAnimation;
        } else {
            return dashOnFloorRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
