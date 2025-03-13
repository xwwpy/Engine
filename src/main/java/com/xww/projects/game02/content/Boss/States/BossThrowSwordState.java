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
import com.xww.projects.game02.content.Boss.BossBullet.Sword;

import java.awt.*;

public class BossThrowSwordState extends StateNode {
    private final Animation throwSwordLeftAnimation;
    private final Animation throwSwordRightAnimation;

    private final Timer throwSwordTimer;

    private final Timer createSwordTimer;


    public BossThrowSwordState(Character owner, Animation throwSwordLeftAnimation, Animation throwSwordRightAnimation) {
        super(owner, 1, null, "throwSwordState");
        this.throwSwordLeftAnimation = throwSwordLeftAnimation;
        this.throwSwordRightAnimation = throwSwordRightAnimation;
        throwSwordTimer = new Timer(1600, (obj)->{
            int ran = CodeUtils.getRandomNum(0, 40);
            if (ran <= 10){
                owner.getStateMachine().forceSwitch("idleState");
            } else if (ran <= 20){
                owner.getStateMachine().forceSwitch("squatState");
            } else if (ran <= 35){
                owner.getStateMachine().forceSwitch("jumpState");
            } else if (ran <= 40){
                owner.getStateMachine().forceSwitch("throwSilkState");
            };
        }, null);
        throwSwordTimer.neverOver();
        throwSwordTimer.stopStart();
        TimerManager.instance.registerTimer(throwSwordTimer);
        createSwordTimer = new Timer(1000, (obj)->{
            int ran = CodeUtils.getRandomNum(0, 80);
            ((Boss) owner).sword();
            if (ran >= 40) {
                owner.getStateMachine().forceSwitch("throwSwordState");
            }
        }, null);
        createSwordTimer.neverOver();
        createSwordTimer.stopStart();
        TimerManager.instance.registerTimer(createSwordTimer);

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
        if (owner.getCurrentHp() <= 0){
            owner.getStateMachine().forceSwitch("deadState");
        }
    }

    @Override
    public void on_enter() {
        throwSwordLeftAnimation.reset_animation();
        throwSwordRightAnimation.reset_animation();
        MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("enemy_throw_sword"));
        throwSwordTimer.restart();
        createSwordTimer.restart();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return throwSwordLeftAnimation;
        } else {
            return throwSwordRightAnimation;
        }
    }

    @Override
    public void on_exit() {
        throwSwordTimer.stopStart();
        createSwordTimer.stopStart();
    }
}
