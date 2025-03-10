package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

public class BossThrowSwordState extends StateNode {
    private final Animation throwSwordLeftAnimation;
    private final Animation throwSwordRightAnimation;
    public BossThrowSwordState(Character owner, Animation throwSwordLeftAnimation, Animation throwSwordRightAnimation) {
        super(owner, 1, null);
        this.throwSwordLeftAnimation = throwSwordLeftAnimation;
        this.throwSwordRightAnimation = throwSwordRightAnimation;

    }

    @Override
    public boolean whetherEnding() {
        return !whetherSuitThisState();
    }

    @Override
    public boolean whetherSuitThisState() {
        return ((Boss)owner).isWhetherThrowingSword();
    }

    @Override
    public void on_enter() {
        throwSwordLeftAnimation.reset_animation();
        throwSwordRightAnimation.reset_animation();
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

    }
}
