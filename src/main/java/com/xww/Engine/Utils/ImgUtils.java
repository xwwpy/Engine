package com.xww.Engine.Utils;

import com.xww.Engine.core.Animation.Rect;
import com.xww.Engine.core.ResourceManager.ResourceManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImgUtils {
    public static final int LEFT_DIR = 0;
    public static final int RIGHT_DIR = 1;

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static Image flipImage(Image image){
        BufferedImage originalImage = (BufferedImage) image;
        BufferedImage flippedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        for (int i = 0; i < originalImage.getWidth(); i++){
            for (int j = 0; j < originalImage.getHeight(); j++){
                flippedImage.setRGB(originalImage.getWidth() - i - 1, j, originalImage.getRGB(i, j));
            }
        }
        return flippedImage;
    }
    public static Image[][] loadImages(String prefix, String suffix, int dirNum, int nums, int startIndex){
        Image[][] res = new Image[dirNum][nums];
        for (int i = 0 ; i < nums; i++){
            res[RIGHT_DIR][i] = loadImage(prefix + (i + startIndex) + suffix);
            res[LEFT_DIR][i] = flipImage(res[RIGHT_DIR][i]);
        }
        return res;
    }

    // TODO 不完善
    public static Image GetDrawRectImage_in_pool(String sourceName, BufferedImage source, Rect origin, Rect target){
        BufferedImage resImage = ResourceManager.getInstance().findImage(sourceName + origin + target);
        if (resImage != null){
            return resImage;
        } else {
            Image img = source.getSubimage(origin.getPosition().getX(), origin.getPosition().getY(), origin.getSize().getX(), origin.getSize().getY()).getScaledInstance(target.getSize().getX(), target.getSize().getY(), Image.SCALE_DEFAULT);
            ResourceManager.getInstance().imagePool.put(sourceName + origin + target, (BufferedImage) img);
            return img;
        }
    }

    public static Image GetDrawRectImage(BufferedImage source, Rect origin, Rect target){
        return source.getSubimage(origin.getPosition().getX(), origin.getPosition().getY(), origin.getSize().getX(), origin.getSize().getY()).getScaledInstance(target.getSize().getX(), target.getSize().getY(), Image.SCALE_DEFAULT);
    }
    // TODO 不完善
    public static Image getScaledImage_in_pool(String sourceName, Image image, int width, int height){
        BufferedImage resImage = ResourceManager.getInstance().findImage(sourceName + width + height);
        if (resImage != null){
            return resImage;
        } else {
            Image img = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            ResourceManager.getInstance().imagePool.put(sourceName + width + height, (BufferedImage) img);
            return img;
        }
    }

    public static Image getScaledImage(Image image, int width, int height){
        return image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
}

