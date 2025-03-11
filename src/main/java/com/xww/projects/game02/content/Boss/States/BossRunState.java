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

public class BossRunState extends StateNode {
    private final Animation runLeftAnimation;
    private final Animation runRightAnimation;

    private final Timer runTimer;
    public BossRunState(Character owner, Animation runLeftAnimation, Animation runRightAnimation) {
        super(owner, 1, null, "run_state");
        this.runLeftAnimation = runLeftAnimation;
        this.runRightAnimation = runRightAnimation;
        runTimer = new Timer(1000, (obj) -> {
            owner.setVelocity(Vector.Zero());
            if (!owner.isWhetherOnGround()) {
                owner.getStateMachine().forceSwitch("fallState");
            } else {
                int ran = CodeUtils.getRandomNum(0, 1);
                if (ran == 0) owner.getStateMachine().forceSwitch("idleState");
                else owner.getStateMachine().forceSwitch("throwSilkState");
            }
        }, null);
        runTimer.stopStart();
        runTimer.neverOver();
        TimerManager.instance.registerTimer(runTimer);
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
    public void on_update(Graphics g) {
        super.on_update(g);
        if (this.owner.getCurrentHp() <= 0){
            owner.getStateMachine().forceSwitch("deadState");
            runTimer.stopStart();
        }
    }

    @Override
    public void on_enter() {
        runLeftAnimation.reset_animation();
        runRightAnimation.reset_animation();
        owner.setEnableGravity(true);
        owner.setVelocity(new Vector(owner.isWhetherFacingLeft() ?  -owner.getRunSpeed() : owner.getRunSpeed(), 0));
        runTimer.restart();
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
        runTimer.stopStart();
    }
}
