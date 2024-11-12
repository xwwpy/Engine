package com.xww.Engine.Core;

import com.xww.Engine.Timer.Timer;
import com.xww.Engine.Vector.Vector;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class SceneComponent extends Component {
    protected boolean isAlive = true;
    protected Vector size; // 场景大小
    protected Anchor anchorMode = Anchor.LEFT_TOP; // 锚点位置
    protected Vector worldPosition = Vector.getZero(); // 游戏世界位置 并且是实际画的位置

    protected double one_frame_gap = 1; // 每一帧更新的增加

    protected double current_frame_gap = 0; // 当前的更新间隔 当达到预设的阈值

    public static int DEFAULT_FRAME_THRESHOLD = 1;
    protected int frame_threshold = DEFAULT_FRAME_THRESHOLD; // 预设的更新阈值


    protected SceneComponent parent; // 父场景

    protected Set<SceneComponent> children = new HashSet<>(); // 子场景

    protected Set<SceneComponent> children_to_remove = new HashSet<>(); // 删除失效的子场景

    protected Set<SceneComponent> children_to_add = new HashSet<>(); // 新添加的子场景

    protected Transform transform = Transform.Identity(); // 场景变换

    protected Set<Timer> timers = new HashSet<>(); // 定时器组件
    protected Set<Timer> timers_to_remove = new HashSet<>();
    protected Set<Timer> timers_to_add = new HashSet<>();
    public void update(){
        if (!isAlive){
            return;
        }
        current_frame_gap += one_frame_gap;
        int num = (int) (current_frame_gap = current_frame_gap / frame_threshold);
        for (int i = 0; i < num; i++) {
            updateTimer(); // 更新定时器组件
            logicUpdate(); // 更新逻辑
            updateChildren(); // 更新子类
            CheckValidate(); // 检测是否合法
        }
    }

    protected void updateChildren() {
        this.children.removeAll(this.children_to_remove);
        this.children_to_remove.clear();
        children.forEach((child) -> {
            child.update();
            if (!child.isAlive){
                this.children_to_remove.add(child);
            }
        });
    }

    /**
     * 检测每次更新后是否需要移除该组件
     */
    protected void CheckValidate() {
        if (!isAlive) {

            parent.removeChildren(this);
        }
    }

    // 更新逻辑
    protected abstract void logicUpdate();

    protected abstract void draw(Graphics g);

    protected void updateTimer() {
        timers.addAll(timers_to_add);
        timers_to_add.clear();
        timers.forEach((timer)->{
            timer.tick();
            if (!timer.isAlive()){
                timers_to_remove.add(timer);
            }
        });

        timers.removeAll(timers_to_remove);
        timers_to_remove.clear();
    }

    public void addTimer(Timer timer){
        this.timers_to_add.add(timer);
    }

    @Override
    public void destroy() {
        this.isAlive = false;
    }

    public void removeChildren(SceneComponent child){
        this.children_to_remove.add(child);
    }

    public Vector getWorldPosition() {
        return worldPosition;
    }

    protected void addChild(SceneComponent child) {
        child.parent = this;
        this.children_to_add.add(child);
    }
}
