package com.xww.Engine.core.Animation;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Vector.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 * 每一帧的左上角与owner的左上角位置相同
 * 动画
 */
public class Animation {

    private final Character owner;
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

    public Animation(Character owner, int each_frame_time) {
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
     * TODO
     * @param image 图片
     * @param num_h 切割后的图片个数
     */
    public void add_frame(BufferedImage image, int num_h) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int width_per_frame = width / num_h;
        for (int i = 0; i < num_h; i++) {
            frames.add(new Frame(image, new Rect(Vector.build(i * width_per_frame, 0), Vector.build(width_per_frame, height))));
        }
    }

    /**
     * 根据图集添加帧 默认不缩放
     * @param atlas 图集
     */
    public void add_frame(Atlas atlas) {
        for (int i = 0; i < atlas.getSize(); i++) {
            frames.add(new Frame(atlas.getImage(i), new Rect(Vector.build(0, 0), Vector.build(atlas.getImage(i).getWidth(null), atlas.getImage(i).getHeight(null)))));
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

    public void on_update(Graphics g) {
        timer.tick();
        this.on_render(g);
    }

    public void on_render(Graphics g) {
        Frame frame = frames.get(frame_index);
        Vector drawPosition = this.owner.getDrawPosition();
        g.drawImage(frame.image, drawPosition.getX(), drawPosition.getY(), null);
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

    public Character getOwner() {
        return owner;
    }


    public int getEach_frame_time() {
        return each_frame_time;
    }
}
