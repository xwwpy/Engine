package com.xww.Engine.core.Sound;

import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MP3Player {
    public static void main(String[] args) {
        String mp3FilePath = "/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/audio/bgm.mp3";

        try (FileInputStream fileInputStream = new FileInputStream(mp3FilePath)) {
            Player player = new Player(fileInputStream);
            // 播放音频
            player.play();

        } catch (FileNotFoundException e) {
            System.err.println("找不到音频文件: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("播放音频时出错: " + e.getMessage());
        }
    }
}