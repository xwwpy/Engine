package com.xww.Engine.core.Timer;

import com.xww.Engine.core.Base;
import com.xww.Engine.core.Event.TimeEventManager;

public class Timer {

    public static interface TimerCallBack {
        public abstract void run(Base obj);
    }
    public static final int INFINITE_TIMES = -1;
    /**
     * 单位纳秒
     */
    private double wait_time;

    private TimerCallBack callback;

    private boolean isOver = false;

    private long start_time;

    private final Base owner;

    /**
     * 执行次数
     * -1表示无限次
     */
    private int run_times = 1;

    private int recovery_times = run_times;

    private boolean whether_start = true;

    private boolean neverOver = false;

    /**
     *
     * @param wait_time 单位 毫秒
     * @param callback 到达定时时间后执行的 回调函数
     */
    public Timer(double wait_time, TimerCallBack callback, Base obj) {
        this.wait_time = wait_time * 1000_000;
        this.start_time = TimeEventManager.currentTime;
        this.callback = callback;
        this.owner = obj;
    }

    public void tick() {
        if (!whether_start) return;
        if (isOver || run_times == 0) return;
        if (TimeEventManager.currentTime - start_time >= this.wait_time){
            if (callback != null) callback.run(owner);
            if (run_times == Timer.INFINITE_TIMES){
                restart0();
            } else {
                run_times--;
                restart0();
                if (run_times == 0){
                    if (!neverOver) isOver = true;
                }
            }
        }
    }

    public void setWaitTime(long wait_time) {
        this.wait_time = wait_time;
    }

    public void setCallback(TimerCallBack callback) {
        this.callback = callback;
    }

    public boolean isOver() {
        return isOver;
    }

    public int getRun_times() {
        return run_times;
    }

    public void setRun_times(int run_times) {
        this.run_times = run_times;
        this.recovery_times = run_times;
    }

    public void stop() {
        isOver = true;
    }

    private void restart0(){
        isOver = false;
        start_time = TimeEventManager.currentTime;
    }
    public void restart() {
        isOver = false;
        whether_start = true;
        this.run_times = this.recovery_times;
        start_time = TimeEventManager.currentTime;
    }

    public boolean isWhether_start() {
        return whether_start;
    }

    public void stopStart(){
        whether_start = false;
    }

    public void start(){
        restart();
    }

    /**
     * 即永远不会被销毁
     */
    public void neverOver(){
        neverOver = true;
    }

    public void canOver() {
        neverOver = false;
    }
}
