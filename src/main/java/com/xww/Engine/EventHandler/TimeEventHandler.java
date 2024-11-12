package com.xww.Engine.EventHandler;

import com.xww.Engine.Core.Base;

/**
 * 单位纳秒
 */
public class TimeEventHandler extends Base {

    private double currentTime; // 当前时间
    private double ApplicationStartTime = 0; // 应用程序的启动时间

    public static TimeEventHandler timeEventHandlerIns = new TimeEventHandler();
    private TimeEventHandler(){}

    @Override
    public void update() {
        updateTime();
    }

    private void updateTime() {
        this.currentTime = System.nanoTime();
    }

    public void start() {
        ApplicationStartTime = System.nanoTime();
    }

    /**
     *
     * @return 返回应用的启动总时长
     */
    public double getApplicationWholeTime(){
        return currentTime - ApplicationStartTime;
    }

    public double getCurrentTime(){
        return currentTime;
    }

}
