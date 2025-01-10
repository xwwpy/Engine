package com.xww.Engine.core.Animation;

import com.xww.Engine.core.Actor.Character;

@FunctionalInterface
public interface AnimationRunnable {
    public abstract boolean run(Character owner);
}
