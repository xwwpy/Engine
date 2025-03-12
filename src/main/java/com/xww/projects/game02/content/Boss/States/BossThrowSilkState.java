package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.Utils.CodeUtils;
import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Timer.TimerManager;
import com.xww.projects.game02.content.Boss.Boss;
import com.xww.projects.game02.content.Boss.BossBullet.Silk;

import java.awt.*;

public class BossThrowSilkState extends StateNode {
    private final Animation throwSilkLeftAnimation;
    private final Animation throwSilkRightAnimation;

    private final Timer throwSilkTimer;

    private Silk silk;
    public BossThrowSilkState(Character owner, Animation throwSilkLeftAnimation, Animation throwSilkRightAnimation) {
        super(owner, 1, null, "throwSilkState");
        this.throwSilkLeftAnimation = throwSilkLeftAnimation;
        this.throwSilkRightAnimation = throwSilkRightAnimation;
        throwSilkTimer = new Timer(2000, (obj)->{
            if (silk != null){
                silk.setAlive(false);
            }
            if (owner.isWhetherOnGround()) {
                owner.getStateMachine().forceSwitch("idleState");
            } else {
                int ran = CodeUtils.getRandomNum(0, 2);
                if (ran != 0) {
                    owner.getStateMachine().forceSwitch("aimState");
                } else {
                    owner.getStateMachine().forceSwitch("fallState");
                }
            }
        }, null);
        throwSilkTimer.stopStart();
        throwSilkTimer.neverOver();
        TimerManager.instance.registerTimer(throwSilkTimer);
        // throwSilkState.setNextStates(new StateNode[]{deadState, idleState, aimState, fallState});
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
            throwSilkTimer.stopStart();
        }
    }

    @Override
    public boolean whetherSuitThisState() {
        return false;
    }

    @Override
    public void on_enter() {
        throwSilkLeftAnimation.reset_animation();
        throwSilkRightAnimation.reset_animation();
        MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("enemy_throw_silk"));
        this.silk = ((Boss)owner).silk();
        throwSilkTimer.restart();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return throwSilkLeftAnimation;
        } else {
            return throwSilkRightAnimation;
        }
    }

    @Override
    public void on_exit() {
        if (silk != null && !throwSilkTimer.isOver()){
            silk.setAlive(false);
        }
        throwSilkTimer.stopStart();
    }
}
