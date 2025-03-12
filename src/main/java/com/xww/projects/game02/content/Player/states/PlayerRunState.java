package com.xww.projects.game02.content.Player.states;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Timer.TimerManager;
import javazoom.jl.player.Player;

import java.awt.*;

public class PlayerRunState extends StateNode {
    protected Animation runLeftAnimation;
    protected Animation runRightAnimation;

    private Player runSoundPlayer;

    private boolean running = false;
    public PlayerRunState(Character owner, Animation runLeftAnimation, Animation runRightAnimation) {
        super(owner, 1, null, "run_state");
        this.runLeftAnimation = runLeftAnimation;
        this.runRightAnimation = runRightAnimation;
        Timer timer = new Timer(500, (obj) -> {
//            if ((runSoundPlayer == null || runSoundPlayer.isComplete()) && running){
//                runSoundPlayer = MP3Player.getInstance().getPlayer(ResourceManager.getInstance().findAudioPath("player_run"));
//                MP3Player.getInstance().addPlayer(runSoundPlayer);
//            }
//            if (!running && runSoundPlayer != null){
//                runSoundPlayer.close();
//                runSoundPlayer = null;
//            }
        }, null);
        timer.setRun_times(Timer.INFINITE_TIMES);
        timer.start();
        TimerManager.instance.registerTimer(timer);
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
    }

    @Override
    public boolean whetherEnding() {
        return owner.isRolling() || owner.getVelocity().getFullX() == 0 || owner.getVelocity().getFullY() != 0;
    }

    @Override
    public boolean whetherSuitThisState() {
        return !whetherEnding();
    }

    @Override
    public void on_enter() {
        running = true;
        runLeftAnimation.reset_animation();
        runRightAnimation.reset_animation();
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
        running = false;
//        if (runSoundPlayer != null) runSoundPlayer.close();
//        runSoundPlayer = null;
    }
}
