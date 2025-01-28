package com.xww.Engine.core.Animation;

import com.xww.Engine.core.Component.Component;

@FunctionalInterface
public interface AnimationRunnable {
    public abstract void run(Component owner);
}
