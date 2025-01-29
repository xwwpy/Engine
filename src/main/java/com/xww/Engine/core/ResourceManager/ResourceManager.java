package com.xww.Engine.core.ResourceManager;

import com.xww.Engine.Utils.ImgUtils;
import com.xww.Engine.core.Animation.Atlas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {

    private static final ResourceManager instance = new ResourceManager();
    private ResourceManager() {}

    private final Map<String, Atlas> atlasPool = new HashMap<>();
    public final Map<String, BufferedImage> imagePool = new HashMap<>();

    public static ResourceManager getInstance() {
        return instance;
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
    public void loadAll(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                loadAll(f.getPath());
            }
        } else {
            if (file.getName().endsWith(".png")) {
                String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                loadImage(name, file.getPath());
            }
        }
    }
}
