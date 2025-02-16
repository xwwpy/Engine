package com.xww.Engine.core.Animation;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.setting.DebugSetting;
import com.xww.Engine.setting.FrameSetting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 * 每一帧的左上角与owner的左上角位置相同
 * 动画
 */
public class Animation {

    private final Component owner;
    private Timer timer;
    boolean is_loop = true;
    int frame_index = 0;
    private final List<Frame> frames = new ArrayList<>();
    /**
     * 动画完成后的回调
     */
    private AnimationRunnable on_complete;

    // 单位毫秒
    private final int each_frame_time;

    /**
     *
     * @param owner 动画的所有者
     * @param each_frame_time 多长时间切换下一帧
     */

    public Animation(Component owner, int each_frame_time) {
        if (owner == null){
            throw new RuntimeException("动画的所有者不应为空");
        }
        this.owner = owner;
        this.each_frame_time = each_frame_time;

        timer = new Timer(each_frame_time, (obj)->{
            frame_index++;
            if (frame_index >= frames.size()) {
                frame_index = is_loop ? 0 : frames.size() - 1;
                if (!is_loop && on_complete != null) {
                    on_complete.run(this.owner);
                    timer.stop();
                }
            }
        }, null);
        timer.setRun_times(Timer.INFINITE_TIMES);
    }


    public void reset_animation() {
        timer.restart();
        frame_index = 0;
    }

    /**
     * 默认为所有者的大小
     * @param image 图片
     * @param num_h 切割后的图片个数
     */
    public void add_frame(BufferedImage image, int num_h) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int width_per_frame = width / num_h;
        for (int i = 0; i < num_h; i++) {
            frames.add(new Frame(image, new Rect(Vector.build(i * width_per_frame, 0), Vector.build(width_per_frame, height)), owner.getSize()));
        }
    }

    /**
     * 默认为所有者的大小
     * @param name 通过名称在资源管理器中查找图片
     * @param num_h 切割后的图片个数
     */
    public void add_frame_by_name(String name, int num_h, boolean whetherInverse) {
        BufferedImage image = ResourceManager.getInstance().findImage(name);
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int width_per_frame = width / num_h;
        for (int i = 0; i < num_h; i++) {
            frames.add(new Frame(image, new Rect(Vector.build(i * width_per_frame, 0), Vector.build(width_per_frame, height)), owner.getSize()));
        }
        if (whetherInverse) {
            // 翻转图片的播放顺序
            for (int i = 0; i < frames.size() / 2; i++) {
                Frame temp = frames.get(i);
                frames.set(i, frames.get(frames.size() - i - 1));
                frames.set(frames.size() - i - 1, temp);
            }
        }
    }

    /**
     * 指定缩放后的大小
     * @param image 图片
     * @param num_h 分割的个数
     * @param size 缩放后的大小
     */
    public void add_frame(BufferedImage image, int num_h, Vector size) {
        int width = image.getWidth(null);
        int width_per_frame = width / num_h;
        for (int i = 0; i < num_h; i++) {
            frames.add(new Frame(image, new Rect(Vector.build(i * width_per_frame, 0), size)));
        }
    }

    /**
     * 指定缩放后的大小
     * @param name 通过名称在资源管理器中查找图片
     * @param num_h 分割的个数
     * @param size 缩放后的大小
     */
    public void add_frame_by_name(String name, int num_h, Vector size) {
        BufferedImage image = ResourceManager.getInstance().findImage(name);
        int width = image.getWidth(null);
        int width_per_frame = width / num_h;
        for (int i = 0; i < num_h; i++) {
            frames.add(new Frame(image, new Rect(Vector.build(i * width_per_frame, 0), size)));
        }
    }

    /**
     * 根据图集添加帧 默认缩放为owner的大小
     * @param atlas 图集
     */
    public void add_frame(Atlas atlas) {
        for (int i = 0; i < atlas.getSize(); i++) {
            frames.add(new Frame(atlas.getImage(i), new Rect(Vector.build(0, 0), Vector.build(atlas.getImage(i).getWidth(null), atlas.getImage(i).getHeight(null))), owner.getSize()));
        }
    }

    /**
     * 根据图集添加帧 默认缩放为owner的大小
     * @param name 通过名称在资源管理器中查找图集
     */
    public void add_frame(String name) {
        Atlas atlas = ResourceManager.getInstance().findAtlas(name);
        for (int i = 0; i < atlas.getSize(); i++) {
            frames.add(new Frame(atlas.getImage(i), new Rect(Vector.build(0, 0), Vector.build(atlas.getImage(i).getWidth(null), atlas.getImage(i).getHeight(null))), owner.getSize()));
        }
    }
    /**
     *
     * @param atlas 图集
     * @param size 得到的目标大小
     */
    public void add_frame(Atlas atlas, Vector size) {
        for (int i = 0; i < atlas.getSize(); i++) {
            frames.add(new Frame(atlas.getImage(i), new Rect(Vector.build(0, 0), Vector.build(atlas.getImage(i).getWidth(null), atlas.getImage(i).getHeight(null))), size));
        }
    }

    /**
     *
     * @param name 通过名称在资源管理器中查找图集
     * @param size 得到的目标大小
     */
    public void add_frame_by_name(String name, Vector size) {
        Atlas atlas = ResourceManager.getInstance().findAtlas(name);
        for (int i = 0; i < atlas.getSize(); i++) {
            frames.add(new Frame(atlas.getImage(i), new Rect(Vector.build(0, 0), Vector.build(atlas.getImage(i).getWidth(null), atlas.getImage(i).getHeight(null))), size));
        }
    }

    public void on_update(Graphics g) {
        timer.tick();
        this.on_render(g);
    }

    public void on_render(Graphics g) {
        Frame frame = frames.get(frame_index);
        Vector drawPosition = this.owner.getDrawPosition();
        if (DebugSetting.IS_DEBUG_ON){
            g.setColor(DebugSetting.DebugInfoColor);
            g.drawRect(drawPosition.getX(), drawPosition.getY(), frame.size.getX(), frame.size.getY());
        }
        if (FrameSetting.whetherInScreen(drawPosition, frame.size)) {
            g.drawImage(frame.image, drawPosition.getX(), drawPosition.getY(), null);
        }
    }



    public boolean isIs_loop() {
        return is_loop;
    }

    public void setIs_loop(boolean is_loop) {
        this.is_loop = is_loop;
    }

    public int getFrame_index() {
        return frame_index;
    }

    public void setFrame_index(int frame_index) {
        this.frame_index = frame_index;
    }


    public List<Frame> getFrames() {
        return frames;
    }


    public AnimationRunnable getOn_complete() {
        return on_complete;
    }

    public void setOn_complete(AnimationRunnable on_complete) {
        this.on_complete = on_complete;
    }

    public Component getOwner() {
        return owner;
    }


    public int getEach_frame_time() {
        return each_frame_time;
    }
}
