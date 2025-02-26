package com.xww.Engine.core.Timer;

import java.util.HashSet;
import java.util.Set;

public class TimerManager {
    public static final TimerManager instance = new TimerManager();

    private TimerManager() {}
    protected Set<Timer> timer = new HashSet<>(); // 定时器组件
    protected Set<Timer> timer_to_add = new HashSet<>();
    protected Set<Timer> timer_to_remove = new HashSet<>();

    public void update() {
        timer.removeAll(timer_to_remove);
        timer_to_remove.clear();
        timer.addAll(timer_to_add);
        timer_to_add.clear();
        timer.forEach((timer)->{
            timer.tick();
            if (timer.isOver()){
                timer_to_remove.add(timer);
            }
        });
    }

    public void registerTimer(Timer timer) {
        timer_to_add.add(timer);
    }
}
