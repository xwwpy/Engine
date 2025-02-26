package com.xww.Engine.Utils;

import org.github.jamm.MemoryMeter;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class CodeUtils {

    public static long getTotalLines(String dictionaryPath){
        long totalLines = 0;
        File file = new File(dictionaryPath);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(file.toPath())){
            for (Path path : stream) {
                if (path.toFile().isDirectory()){
                    // 递归处理文件夹
                    totalLines += getTotalLines(path.toString());
                } else {
                    // 处理文件
                    if (path.toString().endsWith(".java")){
                        // 获取该文件的行数
                        try (Stream<String> s = Files.lines(path)){
                            totalLines += s.count();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return totalLines;
    }

    @Deprecated
    public static long getObjSize(Object obj){
        try {
            MemoryMeter meter = new MemoryMeter();
            return meter.measureDeep(obj);
        } catch (RuntimeException e) {
            System.out.println("对象无法测量：" + e.getMessage());
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(getTotalLines("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com"));
    }
}