package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.Utils.CodeUtils;
import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Timer.TimerManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game02.content.Boss.Boss;

import java.awt.*;

public class BossJumpState extends StateNode {

    private final Animation jumpLeftAnimation;
    private final Animation jumpRightAnimation;

    private final Timer jumpTimer;
    public BossJumpState(Character owner, Animation jumpLeftAnimation, Animation jumpRightAnimation) {
        super(owner, 1, null, "jump_state");
        this.jumpLeftAnimation = jumpLeftAnimation;
        this.jumpRightAnimation = jumpRightAnimation;
        jumpTimer = new Timer(500, (obj) -> {
            this.owner.setVelocity(Vector.Zero());
            int random = CodeUtils.getRandomNum(0, 90);
            if (random <= 50){
                owner.getStateMachine().forceSwitch("aimState");
            } else {
                owner.getStateMachine().forceSwitch("throwSilkState");
            }
        }, null);
        jumpTimer.setRun_times(1);
        jumpTimer.stopStart();
        jumpTimer.neverOver();
        TimerManager.instance.registerTimer(jumpTimer);
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
        jumpLeftAnimation.reset_animation();
        jumpRightAnimation.reset_animation();
        jumpTimer.restart();
        owner.setWhetherOnGround(false);
        this.owner.setEnableGravity(false);
        this.owner.setVelocity(Vector.build(0, -this.owner.getJumpSpeed()));
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        if (this.owner.getCurrentHp() <= 0){
            owner.getStateMachine().forceSwitch("deadState");
            jumpTimer.stopStart();
        }
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
        jumpTimer.stopStart();
    }
}
