package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

import java.awt.*;

public class BossAimState extends StateNode {
    private final Animation aimLeftAnimation;
    private final Animation aimRightAnimation;
    public BossAimState(Character owner, Animation aimLeftAnimation, Animation aimRightAnimation) {
        super(owner, 100, null, "aimState");
        this.aimLeftAnimation = aimLeftAnimation;
        this.aimRightAnimation = aimRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return false;
    }

    @Override
    public boolean whetherSuitThisState() {
        return false;
    }

    @Override
    public void on_enter() {
        aimLeftAnimation.reset_animation();
        aimRightAnimation.reset_animation();
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        if (owner.getCurrentHp() <= 0){
            owner.getStateMachine().forceSwitch("deadState");
        }
        if (this.currentAnimation.isOver()) {
            this.owner.getStateMachine().forceSwitch("dashInAirState");
        }
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
