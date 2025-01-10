package com.xww.Engine.core.Animation;

import com.xww.Engine.Utils.ImgUtils;
import com.xww.Engine.core.Vector.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Frame {
    public Image image;
    public Frame(Image image, Rect rect_src) {
        BufferedImage image1 = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image1.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        this.image = ImgUtils.GetDrawRectImage(image1, rect_src, rect_src);
    }

    /**
     *
     * @param image 原始图片
     * @param rect_src 截取的部分
     * @param size 目标的图像大小
     */
    public Frame(Image image, Rect rect_src, Vector size) {
        BufferedImage image1 = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image1.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        Rect tar = new Rect(Vector.Zero(), size);
        this.image = ImgUtils.GetDrawRectImage(image1, rect_src, tar);
    }
}
