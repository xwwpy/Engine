package com.xww.projects.game02.content.Boss.States;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.projects.game02.content.Boss.Boss;

public class BossThrowBarbState extends StateNode {
    private final Animation throwBarbLeftAnimation;
    private final Animation throwBarbRightAnimation;
    public BossThrowBarbState(Character owner, Animation throwBarbLeftAnimation, Animation throwBarbRightAnimation) {
        super(owner, 1, null);
        this.throwBarbLeftAnimation = throwBarbLeftAnimation;
        this.throwBarbRightAnimation = throwBarbRightAnimation;
    }

    @Override
    public boolean whetherEnding() {
        return !((Boss) owner).isWhetherThrowingSword();
    }

    @Override
    public boolean whetherSuitThisState() {
        return !whetherEnding();
    }

    @Override
    public void on_enter() {
        throwBarbLeftAnimation.reset_animation();
        throwBarbRightAnimation.reset_animation();
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
