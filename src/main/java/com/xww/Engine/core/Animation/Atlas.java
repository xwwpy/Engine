package com.xww.Engine.core.Animation;

import com.xww.Engine.Utils.ImgUtils;
import com.xww.Engine.Utils.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class Atlas {
    // 有序
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
    public void addImage(BufferedImage image) {
        img_lists.add(image);
    }

    public void loadSingle(String path) {
        img_lists.add(ImgUtils.loadImage(path));
    }

    public void loadSingle(String path, int index) {
        if (img_lists.size() < index + 1){
            for (int i = img_lists.size(); i <= index + 1; i++) {
                img_lists.add(null);
            }
        }
        img_lists.add(index, ImgUtils.loadImage(path));
    }

    public void loadSingle(String path, int width, int height) {
        img_lists.add(ImgUtils.getScaledImage(ImgUtils.loadImage(path), width, height));
    }

    public void inverse() {
        // 反转图集中图片的顺序
        for (int i = 0; i < img_lists.size() / 2; i++) {
            Image temp = img_lists.get(i);
            img_lists.set(i, img_lists.get(img_lists.size() - i - 1));
            img_lists.set(img_lists.size() - i - 1, temp);
        }
    }

    public void clearNull() {
        img_lists.removeIf(Objects::isNull);
    }
}