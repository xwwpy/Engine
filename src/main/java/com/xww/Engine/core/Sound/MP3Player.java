package com.xww.Engine.core.Sound;

import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MP3Player {

    private static final MP3Player instance = new MP3Player();
    private final ExecutorService executorService;
    private final LinkedBlockingQueue<String> audioQueue;
    private volatile boolean isPlayingBGM = false;
    private String bgmPath;

    private MP3Player() {
        executorService = Executors.newFixedThreadPool(2); // 一个线程用于播放背景音乐，另一个用于播放其他音频
        audioQueue = new LinkedBlockingQueue<>();
    }

    public static MP3Player getInstance() {
        return instance;
    }

    public void setBGMPath(String bgmPath) {
        this.bgmPath = bgmPath;
    }

    public void startBGM() {
        if (!isPlayingBGM && bgmPath != null) {
            isPlayingBGM = true;
            executorService.submit(() -> {
                while (isPlayingBGM) {
                    try (FileInputStream fileInputStream = new FileInputStream(bgmPath)) {
                        Player player = new Player(fileInputStream);
                        player.play();
                    } catch (FileNotFoundException e) {
                        System.err.println("找不到音频文件: " + e.getMessage());
                        break;
                    } catch (Exception e) {
                        System.err.println("播放音频时出错: " + e.getMessage());
                        break;
                    }
                }
            });
        }
    }

    public void stopBGM() {
        isPlayingBGM = false;
    }

    public void addAudio(String filePath) {
        audioQueue.add(filePath);
        executorService.submit(() -> {
            while (!audioQueue.isEmpty()) {
                String path = audioQueue.poll();
                try (FileInputStream fileInputStream = new FileInputStream(path)) {
                    Player player = new Player(fileInputStream);
                    player.play();
                } catch (FileNotFoundException e) {
                    System.err.println("找不到音频文件: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("播放音频时出错: " + e.getMessage());
                }
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}


