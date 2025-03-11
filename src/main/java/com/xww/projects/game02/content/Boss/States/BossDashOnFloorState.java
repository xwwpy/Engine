package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Timer.TimerManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game02.content.Boss.Boss;

import java.awt.*;

public class BossDashOnFloorState extends StateNode {

    private final Animation dashOnFloorLeftAnimation;
    private final Animation dashOnFloorRightAnimation;

    private final Timer dashOnFloorTimer;

    public BossDashOnFloorState(Character owner, Animation dashOnFloorLeftAnimation, Animation dashOnFloorRightAnimation) {
        super(owner, 1, null, "dashOnFloorState");
        this.dashOnFloorLeftAnimation = dashOnFloorLeftAnimation;
        this.dashOnFloorRightAnimation = dashOnFloorRightAnimation;
        dashOnFloorTimer = new Timer(1000, (obj)->{
            owner.setVelocity(Vector.build(0, 0));
            if (!owner.isWhetherOnGround()) {
                owner.getStateMachine().forceSwitch("fallState");
            } else {
                owner.getStateMachine().forceSwitch("idleState");
            }
        }, null);
        dashOnFloorTimer.neverOver();
        dashOnFloorTimer.stopStart();
        TimerManager.instance.registerTimer(dashOnFloorTimer);
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
            dashOnFloorTimer.stopStart();
        }
    }

    @Override
    public void on_enter() {
        dashOnFloorLeftAnimation.reset_animation();
        dashOnFloorRightAnimation.reset_animation();
        MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("enemy_dash"));
        owner.setVelocity(Vector.build(owner.isWhetherFacingLeft() ? -owner.getRunSpeed() * 2 : owner.getRunSpeed() * 2, 0));
        dashOnFloorTimer.restart();
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
        dashOnFloorTimer.stopStart();
        owner.setEnableGravity(true);
    }
}
