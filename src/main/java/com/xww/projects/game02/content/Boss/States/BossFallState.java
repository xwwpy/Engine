package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

import java.awt.*;

public class BossFallState extends StateNode {
    private final Animation fallLeftAnimation;
    private final Animation fallRightAnimation;
    public BossFallState(Character owner, Animation fallLeftAnimation, Animation fallRightAnimation) {
        super(owner, 1, null, "fallState");
        this.fallLeftAnimation = fallLeftAnimation;
        this.fallRightAnimation = fallRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return this.owner.isWhetherOnGround();
    }

    @Override
    public boolean whetherSuitThisState() {
        return false;
    }

    @Override
    public void on_enter() {
        fallLeftAnimation.reset_animation();
        fallRightAnimation.reset_animation();
        this.owner.setEnableGravity(true);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        if (owner.getCurrentHp() <= 0){
            owner.getStateMachine().forceSwitch("deadState");
        }
        if (this.owner.isWhetherOnGround()){
            owner.getStateMachine().forceSwitch("idleState");
        }
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
