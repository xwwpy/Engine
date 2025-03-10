package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

public class BossThrowSilkState extends StateNode {
    private final Animation throwSilkLeftAnimation;
    private final Animation throwSilkRightAnimation;
    public BossThrowSilkState(Character owner, Animation throwSilkLeftAnimation, Animation throwSilkRightAnimation) {
        super(owner, 1, null);
        this.throwSilkLeftAnimation = throwSilkLeftAnimation;
        this.throwSilkRightAnimation = throwSilkRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !((Boss) owner).isWhetherThrowingSilk();
    }

    @Override
    public boolean whetherSuitThisState() {
        return ((Boss) owner).isWhetherThrowingSilk();
    }

    @Override
    public void on_enter() {
        throwSilkLeftAnimation.reset_animation();
        throwSilkRightAnimation.reset_animation();
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

    }
}
