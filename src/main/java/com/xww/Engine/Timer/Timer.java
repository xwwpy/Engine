package com.xww.Engine.Timer;

public class Timer {

    private boolean isAlive = true;

    private int loopTimes = 1; // 循环次数

    private double waitTimes; // 每次执行所需的时间

    private double startTime; // 开始时间

    private boolean flag = false; // 开始的标志


    public Timer(int loopTimes, double waitTimes) {
        this.loopTimes = loopTimes;
        this.waitTimes = waitTimes;
    }

    public void tick() {
        if (!flag) return;
    }

    public boolean isAlive(){
        return isAlive;
    }

    public void startTimer() {
        flag = true;
    }
    public void stopTimer() {
        isAlive = false;
    }

    public void setLoopTimes(int loopTimes) {
        this.loopTimes = loopTimes;
    }
}
