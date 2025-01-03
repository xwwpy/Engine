package com.xww.NewEngine.core.Event;


public class TimeEventManager{
    public static long currentTime = 0; // 当前时间
    public static long gameStartTime = 0; // 游戏开始时间
    // 每帧调用更新时间
    public static void tick(){
        currentTime = System.nanoTime();
    }
    public static void start(){
        gameStartTime = System.nanoTime();
        tick();
    }
    public static double getRunTime(){
        return (currentTime - gameStartTime) / 1000_000_000.0;
    }
}
