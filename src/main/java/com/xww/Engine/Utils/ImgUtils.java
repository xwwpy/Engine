package com.xww.Engine.Utils;

import game.game07.content.Card.CardsBar;
import game.game07.core.Animation.Animation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImgUtils {
    public static class Rect{
        public int x, y, width, height;
        public Rect(int x, int y, int width, int height) {
            this.x = x;
            // 画的y坐标
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
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

    /**
     *
     * @return 顺时针旋转90度后的图片
     */
    public static BufferedImage vertical_to_horizontal(BufferedImage image) {
        BufferedImage destImage = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());

        for (int i = 0; i < destImage.getWidth(); i++){
            for (int j = 0; j < destImage.getHeight(); j++){
                destImage.setRGB(destImage.getWidth() - i - 1, j, image.getRGB(j, i));
            }
        }
        return destImage;
    }


    public static Image getBackground(){
        //        img = vertical_to_horizontal(img);
        Image image = loadImage("/Applications/程序/项目文件/javaProject/project01/src/game/game07/Resources/res/images/bg5.jpg");
        image = getScaledImage(image, (int) (747 * 2.3), (int) (320 * 2.3));
        return image;
    }

    public static Image getBarImg(){
        Image img = loadImage("/Applications/程序/项目文件/javaProject/project01/src/game/game07/Resources/res/images/bar.png");
        return getScaledImage(img, CardsBar.BAR_WIDTH * (8), CardsBar.BAR_HEIGHT + 20);
    }
    public static void DrawRectImage(BufferedImage source, Rect origin, Rect target, Graphics g) {
        // TODO 不要每次都重新创建新的图片
        Image img = source.getSubimage(origin.x, origin.y, origin.width, origin.height).getScaledInstance(target.width, target.height, Image.SCALE_DEFAULT);
//         if (SceneObjectRoot.IS_DEBUG_ON) {
//             // 在图片的周围 画一圈
//
//         }
         g.drawImage(img, target.x, target.y, null);
    }
    public static void DrawRectImage(Animation.Frame frame, Rect tar, Graphics g) {

//        if (SceneObjectRoot.IS_DEBUG_ON){
//            // 画一圈线
//            g.setColor(Color.white);
//            int width = frame.image.getWidth(null);
//            int height = frame.image.getHeight(null);
//            g.drawLine(tar.x, tar.y, tar.x + width, tar.y);
//            g.drawLine(tar.x, tar.y, tar.x, tar.y + height);
//            g.drawLine(tar.x, tar.y + height, tar.x + width, tar.y + height);
//            g.drawLine(tar.x + width, tar.y, tar.x + width, tar.y + height);
//        }
        g.drawImage(frame.image, tar.x, tar.y, null);
    }

    public static Image GetDrawRectImage(BufferedImage source, Rect origin, Rect target){
        return source.getSubimage(origin.x, origin.y, origin.width, origin.height).getScaledInstance(target.width, target.height, Image.SCALE_DEFAULT);
    }

    public static Image getScaledImage(Image image, int width, int height){
        return image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
}

