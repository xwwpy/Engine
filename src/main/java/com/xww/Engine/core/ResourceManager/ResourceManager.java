package com.xww.Engine.core.ResourceManager;

import com.xww.Engine.Utils.ImgUtils;
import com.xww.Engine.Utils.StringUtils;
import com.xww.Engine.core.Animation.Atlas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class ResourceManager {

    private static final ResourceManager instance = new ResourceManager();
    private ResourceManager() {}

    private final Map<String, Atlas> atlasPool = new HashMap<>();
    public final Map<String, BufferedImage> imagePool = new HashMap<>();

    public static ResourceManager getInstance() {
        return instance;
    }

    static {
        instance.loadAll("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/enemy", true, ".png");
        instance.loadAll("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/player", true, ".png");
        instance.loadImage("background", "/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/background.png");
        instance.loadImage("ui_heart", "/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/ui_heart.png");
    }

    public Atlas findAtlas(String name) {
        Atlas atlas = atlasPool.get(name);
        if (atlas == null) {
            System.out.println("atlas " + name + " is null");
        }
        return atlas;
    }

    public BufferedImage findImage(String name) {
        BufferedImage image = imagePool.get(name);
        if (image == null) {
            System.out.println("image " + name + " is null");
        }
        return image;
    }

    public void loadAtlas(String name, String path_template, int num, int start) {
        Atlas atlas = new Atlas();
        atlas.load(path_template, num, start);
        atlasPool.put(name, atlas);
    }

    public void loadImage(String name, String path) {
        BufferedImage image = ImgUtils.loadImage(path);
        imagePool.put(name, image);
    }

    /**
     * 遍历指定文件夹的所有资源
     */
    public void loadAll(String path, boolean prefixFlag, String... suffix) {
        try (Stream<Path> stream = Files.walk(new File(path).toPath(), FileVisitOption.FOLLOW_LINKS)) {
            stream.forEach(filePath -> {
                for (String fileSuffix : suffix) {
                    if (filePath.toString().endsWith(fileSuffix)) {
                        processFileWithSuffix(filePath, fileSuffix, prefixFlag);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("遍历文件夹错误: " + path);
        }
    }

    private void processFileWithSuffix(java.nio.file.Path filePath, String fileSuffix, boolean prefixFlag) {
        if (fileSuffix.equals(".png")) {
            loadImage(getResourceName(filePath, fileSuffix, prefixFlag), filePath.toString());
        }
    }

    private String getResourceName(Path filePath, String fileSuffix, boolean prefixFlag) {
        if (!prefixFlag) {
            return StringUtils.removeSuffix(filePath.getFileName().toString(), fileSuffix);
        } else {
            return StringUtils.getDictionaryName(filePath) + "_" + StringUtils.removeSuffix(filePath.getFileName().toString(), fileSuffix);
        }
    }

}
