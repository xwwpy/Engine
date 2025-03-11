package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Timer.TimerManager;
import com.xww.projects.game02.content.Boss.Boss;

import java.awt.*;

// TODO

public class BossThrowBarbState extends StateNode {
    private final Animation throwBarbLeftAnimation;
    private final Animation throwBarbRightAnimation;

    private final Timer timer = new Timer(1600, (obj) -> {
        owner.getStateMachine().forceSwitch("idleState");
    }, null);
    public BossThrowBarbState(Character owner, Animation throwBarbLeftAnimation, Animation throwBarbRightAnimation) {
        super(owner, 1, null, "throwBarbState");
        this.throwBarbLeftAnimation = throwBarbLeftAnimation;
        this.throwBarbRightAnimation = throwBarbRightAnimation;
        timer.stopStart();
        timer.neverOver();
        TimerManager.instance.registerTimer(timer);
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
        throwBarbLeftAnimation.reset_animation();
        throwBarbRightAnimation.reset_animation();
        ((Boss)owner).barb();
        MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("enemy_throw_barbs"));
        timer.restart();
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        if (owner.getCurrentHp() <= 0){
            owner.getStateMachine().forceSwitch("deadState");
        }
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return throwBarbLeftAnimation;
        } else {
            return throwBarbRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
