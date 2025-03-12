package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Scene.SceneManager;
import com.xww.Engine.core.StateManager.StateNode;

public class BossDeadState extends StateNode {
    private final Animation deadLeftAnimation;
    private final Animation deadRightAnimation;
    public BossDeadState(Character owner, Animation deadLeftAnimation, Animation deadRightAnimation) {
        super(owner, 1000, null, "deadState");
        this.deadLeftAnimation = deadLeftAnimation;
        this.deadRightAnimation = deadRightAnimation;
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
        owner.setAlive(false);
        owner.getStateMachine().bannedForceSwitch();
        SceneManager.sceneManagerIns.setCurrentScene("playerWin");
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
