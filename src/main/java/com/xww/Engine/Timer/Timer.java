package com.xww.Engine.Timer;

import com.xww.Engine.CallBack.TimerCallback;
import com.xww.Engine.Core.Base;
import com.xww.Engine.EventHandler.TimeEventHandler;

public class Timer {

    private boolean isAlive = true;

    private final int loopTimes; // 循环次数

    private int currentTimes; // 当前的次数

    private final double waitTimes; // 每次执行所需的时间

    private double startTime; // 开始时间

    private boolean flag = false; // 是否开始计时的标志

    private final TimerCallback callback; // 回掉函数

    private final Base owner; // 计时器的所有者


    public Timer(int loopTimes, double waitTimes, TimerCallback timerCallback, Base owner) {
        this.loopTimes = loopTimes;
        this.currentTimes = loopTimes;
        this.waitTimes = waitTimes;
        this.callback = timerCallback;
        this.owner = owner;
    }

    public void tick() {
        if (!flag) return;
        if(TimeEventHandler.timeEventHandlerIns.getCurrentTime() - startTime >= waitTimes) {
            callback.run(this.owner);
            if (--currentTimes <= 0) {
                stopTimer();
            } else {
                updateStartTime();
            }
        }
    }

    // 更新开始时间 重新进行计时
    private void updateStartTime() {
        this.startTime = TimeEventHandler.timeEventHandlerIns.getCurrentTime();
    }

    // 重置定时器
    public void reStartTimer() {
        this.currentTimes = loopTimes;
        updateStartTime();
    }
    public boolean isAlive(){
        return isAlive;
    }

    public void startTimer() {
        flag = true;
        updateStartTime();
    }
    public void stopTimer() {
        isAlive = false;
    }
}
