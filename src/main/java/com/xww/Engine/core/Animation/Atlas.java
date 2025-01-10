package com.xww.Engine.core.Animation;

import com.xww.Engine.Utils.ImgUtils;
import com.xww.Engine.Utils.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;



public class Atlas {
    private final List<Image> img_lists = new ArrayList<>();
    public void load(String path_template, int num, int start) {
        img_lists.clear();
        for (int i = start; i < num; i++) {
            img_lists.add(ImgUtils.loadImage(StringUtils.format(path_template, i)));
        }
    }

    public void clear() {
        img_lists.clear();
    }

    public int getSize() {
        return img_lists.size();
    }

    public Image getImage(int index) {
        return img_lists.get(index);
    }
    void addImage(BufferedImage image) {
        img_lists.add(image);
    }

    public void loadSingle(String path) {
        img_lists.add(ImgUtils.loadImage(path));
    }

    public void loadSingle(String path, int width, int height) {
        img_lists.add(ImgUtils.getScaledImage(ImgUtils.loadImage(path), width, height));
    }
}