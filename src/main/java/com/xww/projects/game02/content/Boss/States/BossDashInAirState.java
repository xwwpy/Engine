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
import com.xww.projects.game02.content.Player.Player;

import java.awt.*;

public class BossDashInAirState extends StateNode {

    private final Animation dashInAirLeftAnimation;
    private final Animation dashInAirRightAnimation;

    private final Timer dashInAirTimer;

    public BossDashInAirState(Character owner, Animation dashInAirLeftAnimation, Animation dashInAirRightAnimation) {
        super(owner, 1, null, "dashInAirState");
        this.dashInAirLeftAnimation = dashInAirLeftAnimation;
        this.dashInAirRightAnimation = dashInAirRightAnimation;
        dashInAirTimer = new Timer(1000, (obj)->{
            owner.setVelocity(Vector.Zero());
            if (!owner.isWhetherOnGround()){
                owner.getStateMachine().forceSwitch("fallState");
            } else {
                owner.getStateMachine().forceSwitch("idleState");
            }
        }, null);
        dashInAirTimer.stopStart();
        dashInAirTimer.neverOver();
        TimerManager.instance.registerTimer(dashInAirTimer);
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
        dashInAirLeftAnimation.reset_animation();
        dashInAirRightAnimation.reset_animation();
        Boss.bossHitDamage = 10;
        Player player = ((Boss) owner).getPlayer();
        Vector position = player.getLeftTopWorldPosition();
        Vector position2 = owner.getLeftTopWorldPosition();
        MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("enemy_dash"));

        Vector subbed = position.sub(position2);
        owner.setVelocity(subbed);
        dashInAirTimer.restart();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return dashInAirLeftAnimation;
        } else {
            return dashInAirRightAnimation;
        }
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        if (owner.getCurrentHp() <= 0){
            owner.getStateMachine().forceSwitch("deadState");
            dashInAirTimer.stopStart();
        }
    }

    @Override
    public void on_exit() {
        Boss.bossHitDamage = 2;
        dashInAirTimer.stopStart();
        owner.setEnableGravity(true);
    }
}
