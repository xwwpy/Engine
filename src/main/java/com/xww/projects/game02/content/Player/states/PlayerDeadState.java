package com.xww.projects.game02.content.Player.states;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Scene.SceneManager;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.GameScene.PlayerScene;

import java.awt.*;

public class PlayerDeadState extends StateNode {
    protected Animation deadLeftAnimation;
    protected Animation deadRightAnimation;
    public PlayerDeadState(Character owner, Animation deadLeftAnimation, Animation deadRightAnimation) {
        super(owner, 1000, null, "dead_state");
        this.deadLeftAnimation = deadLeftAnimation;
        this.deadRightAnimation = deadRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return owner.getCurrentHp() > 0;
    }

    @Override
    public boolean whetherSuitThisState() {
        return !whetherEnding();
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
        if (this.currentAnimation.isOver()){
            PlayerScene.running = false;
            PlayerScene.targetScene = "playerFailure";
        }
    }

    @Override
    public void on_enter() {
        owner.getStateMachine().bannedForceSwitch();
        deadLeftAnimation.reset_animation();
        deadRightAnimation.reset_animation();
    }

    @Override
    protected Animation selectCurrentAnimation() {
        if (owner.isWhetherFacingLeft()){
            return deadLeftAnimation;
        } else {
            return deadRightAnimation;
        }
    }

    @Override
    public void on_exit() {

    }
}
