package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.Utils.CodeUtils;
import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Timer.TimerManager;
import com.xww.projects.game02.content.Boss.Boss;

import java.awt.*;

public class BossIdleState extends StateNode {
    protected Animation idleLeftAnimation;
    protected Animation idleRightAnimation;

    private final Timer idleTimer;
    public BossIdleState(Character owner, Animation idleLeftAnimation, Animation idleRightAnimation) {
        super(owner, 0, null, "idleState");
        this.idleLeftAnimation = idleLeftAnimation;
        this.idleRightAnimation = idleRightAnimation;
        idleTimer = new Timer(200, (obj)->{
            int randomNum = CodeUtils.getRandomNum(0, 200);
            if (owner.getLeftTopWorldPosition().distance(((Boss)owner).getPlayer().getLeftTopWorldPosition()) < 300 && randomNum <= 40){
                owner.getStateMachine().forceSwitch("throwSilkState");
                return;
            }
            if (owner.getCurrentHp() >= owner.getHp() / 2) {
                if (randomNum <= 10) {
                    // 继续处于idle 状态
                } else if (randomNum <= 30) {
                    if (owner.isWhetherOnGround()) {
                        owner.getStateMachine().forceSwitch("jumpState");
                    } else {
                        owner.getStateMachine().forceSwitch("fallState");
                    }
                } else if (randomNum <= 50) {
                    if (owner.isWhetherOnGround()) {
                        owner.getStateMachine().forceSwitch("runState");
                    } else {
                        owner.getStateMachine().forceSwitch("fallState");
                    }
                } else if (randomNum <= 70) {
                    owner.getStateMachine().forceSwitch("squatState");
                } else if (randomNum <= 73) {
                    owner.getStateMachine().forceSwitch("throwSilkState");
                } else if (randomNum <= 90) {
                    owner.getStateMachine().forceSwitch("throwBarbState");
                } else {
                    if (owner.isWhetherOnGround()) {
                        owner.getStateMachine().forceSwitch("throwSwordState");
                    } else {
                        owner.getStateMachine().forceSwitch("fallState");
                    }
                }
            } else {
                if (randomNum <= 5) {
                    // 继续处于idle 状态
                } else if (randomNum <= 20) {
                    if (owner.isWhetherOnGround()) {
                        owner.getStateMachine().forceSwitch("jumpState");
                    } else {
                        owner.getStateMachine().forceSwitch("fallState");
                    }
                } else if (randomNum <= 25) {
                    if (owner.isWhetherOnGround()) {
                        owner.getStateMachine().forceSwitch("runState");
                    } else {
                        owner.getStateMachine().forceSwitch("fallState");
                    }
                } else if (randomNum <= 30) {
                    owner.getStateMachine().forceSwitch("squatState");
                } else if (randomNum <= 50) {
                    owner.getStateMachine().forceSwitch("throwSilkState");
                } else if (randomNum <= 70) {
                    owner.getStateMachine().forceSwitch("throwBarbState");
                } else {
                    if (owner.isWhetherOnGround()) {
                        owner.getStateMachine().forceSwitch("throwSwordState");
                    } else {
                        owner.getStateMachine().forceSwitch("fallState");
                    }
                }
            }
        }, null);
        idleTimer.setRun_times(Timer.INFINITE_TIMES);
        idleTimer.stopStart();
        idleTimer.neverOver();
        TimerManager.instance.registerTimer(idleTimer);
    }

    @Override
    public boolean whetherEnding() {
        return false;
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        if (owner.getCurrentHp() <= 0){
            owner.getStateMachine().forceSwitch("deadState");
            idleTimer.stopStart();
        }
    }

    @Override
    public boolean whetherSuitThisState() {
        return true;
    }

    @Override
    public void on_enter() {
        idleLeftAnimation.reset_animation();
        idleRightAnimation.reset_animation();
        idleTimer.restart();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return idleLeftAnimation;
        } else {
            return idleRightAnimation;
        }
    }

    @Override
    public void on_exit() {
        idleTimer.stopStart();
    }
}
