package com.xww.NewEngine.core.Component;


import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Base;
import com.xww.NewEngine.core.Collision.ActionAfterCollision;
import com.xww.NewEngine.core.Collision.BaseCollider;
import com.xww.NewEngine.core.Collision.CollisionHandler;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;
import com.xww.NewEngine.core.Timer.Timer;

import java.awt.*;
import java.util.*;

public abstract class Component implements Base, Comparable<Component> {

    public static final ActionAfterCollision.CollisionCallBack ComponentDefaultCallBack = (obj)-> ActionAfterCollision.ActionAfterCollisionType.rebound;

    public static Set<Component> components = new HashSet<>(); // 所有组件 只包含自由组件
    public static Set<Component> components_to_add = new HashSet<>();
    public static Set<Component> components_to_remove = new HashSet<>();
    protected Component parent; // 父组件 当父组件为null时视为独立组件
    protected Set<Component> children_to_add = new HashSet<>();
    protected Set<Component> children = new HashSet<>();
    protected Set<Component> children_to_remove = new HashSet<>();

    protected Set<BaseCollider> colliders = new HashSet<>(); // 组件碰撞器
    protected Set<BaseCollider> colliders_to_add = new HashSet<>();
    protected Set<BaseCollider> colliders_to_remove = new HashSet<>();

    protected Set<Timer> timer = new HashSet<>(); // 定时器组件
    protected Set<Timer> timer_to_add = new HashSet<>();
    protected Set<Timer> timer_to_remove = new HashSet<>();
    protected Vector worldPosition; // 组件世界坐标
    protected GameFrame.PositionType positionType = GameFrame.PositionType.World; // 默认为世界坐标

    protected Vector size; // 组件大小
    protected AnchorMode anchorMode = AnchorMode.LeftTop; // 锚点模式 是指坐标指定的位置

    protected Vector velocity; // 速度 每秒多少像素
    protected Vector acceleration; // 加速度 每秒多少像素

    protected int order = 0; // 绘制顺序 越大图层越靠前

    protected boolean isAlive = true;

    /**
     * 上次的移动, 用于碰撞发生时的回退
     */
    protected Vector lastMove = Vector.Zero();

    protected int CollisionRegion = -1; // -1 代表不检测


    public static void addComponent(Component component) {
        components_to_add.add(component);
    }

    @Override
    public void on_create() {

    }

    @Override
    public void on_destroy() {

    }

    @Override
    public void on_update(Graphics g) {
        checkMove();
        // 更新子组件
        update_children(g);
        // 更新定时器
        update_timer();
        // 更新碰撞器组件
        update_collider();
        drawCollider(g);
    }

    protected void drawCollider(Graphics g) {
        colliders.forEach(collider -> {
            collider.draw(g);
        });
    }

    private void update_collider() {
        colliders.addAll(colliders_to_add);
        colliders_to_add.clear();
        colliders.removeAll(colliders_to_remove);
        CollisionHandler.colliders.removeAll(colliders_to_remove);
        colliders_to_remove.clear();
        colliders.forEach((collider)->{
            if (!collider.isAlive()){
                colliders_to_remove.add(collider);
            }
        });
    }

    private void update_timer() {
        timer.removeAll(timer_to_remove);
        timer_to_remove.clear();
        timer.addAll(timer_to_add);
        timer_to_add.clear();
        timer.forEach((timer)->{
            timer.tick();
            if (timer.isOver()){
                timer_to_remove.add(timer);
            }
        });
    }

    private void update_children(Graphics g) {
        children_to_remove.forEach(child -> {
            child.on_destroy();
            children.remove(child);
        });
        children_to_remove.clear();
        children.addAll(children_to_add);
        children_to_add.clear();
        children.stream().sorted().forEach((child)->{
            child.on_update(g);
            if (!child.isAlive) {
                children_to_remove.add(child);
            }
        }
        );
    }

    /**
     * 检测组件是否需要移动
     */
    protected void checkMove() {
        this.velocity = this.velocity.add(GameFrame.getFrameVelocity(this.acceleration));
        // 预移动
        pre_move();
        // 检查碰撞
        if (checkCollision()) {
            // 发生碰撞后的回调函数
            collisionAction();
            // 只有是自己触发的碰撞才回退移动
            this.return_move();
        }
    }

    /**
     * 碰撞发生后的行为
     */
    protected void collisionAction() {
        ActionAfterCollision.ActionAfterCollisionType actionAfterCollisionType = ComponentDefaultCallBack.callBack(this);
        switch (actionAfterCollisionType){
            case stop:
                // 将速度和加速度清零
                this.clearVeAc();
                break;
            case rebound:
                // 将速度进行反转
                this.reboundVelocity();
                break;
            case die:
                this.setAlive(false);
                break;
            case nullAction:
                lastMove = Vector.Zero();
                break;
            default:
                System.out.println("Component 组件目前不支持 碰撞发生后的指定的此行为: " + actionAfterCollisionType);
                break;

        }
    }


    protected void reboundVelocity() {
        this.velocity.divide_self(-1);
    }

    /**
     * 将速度和加速度清零
     */
    protected void clearVeAc() {
        this.velocity = Vector.Zero();
        this.acceleration = Vector.Zero();
    }

    /**
     * 主动检测碰撞
     */
    protected boolean checkCollision() {
        return CollisionHandler.checkCollision(this);
    }

    /**
     * 被动接受碰撞事件
     * @param other 发生碰撞的物体
     */

    public void receiveCollision(Component other){
        collisionAction();
    }

    /**
     * 预移动
     */
    protected void pre_move() {
        lastMove = GameFrame.getFrameVelocity(this.velocity);
        this.worldPosition = this.worldPosition.add_to_self(lastMove);
    }

    /**
     * 回退运动
     */
    protected void return_move() {
        this.worldPosition = this.worldPosition.sub_to_self(lastMove);
    }
    /**
     * 移动
     */
    @Override
    public int compareTo(Component other) {
        return Integer.compare(this.order, other.order);
    }


    public void addChild(Component child) {
        child.parent = this;
        this.children_to_add.add(child);
    }

    public void setOrder(int order) {
        this.order = order;
    }
    public boolean isAlive() {
        return isAlive;
    }
    public AnchorMode getAnchorMode() {
        return this.anchorMode;
    }

    public Component getParent() {
        return parent;
    }

    public void setParent(Component parent) {
        this.parent = parent;
    }

    public Vector getWorldPosition() {
        return worldPosition;
    }

    public void setWorldPosition(Vector worldPosition) {
        this.worldPosition = worldPosition;
    }

    public Vector getSize() {
        return size;
    }

    public void setSize(Vector size) {
        this.size = size;
    }

    public void setAnchorMode(AnchorMode anchorMode) {
        this.anchorMode = anchorMode;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public int getOrder() {
        return order;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Vector getDrawPosition() {
        return GameFrame.context.getRealDrawPosition(GameFrame.getLeftTopWorldPosition(this), this.positionType);
    }

    public Vector getLeftTopWorldPosition(){
        return GameFrame.getLeftTopWorldPosition(this);
    }

    public void addTimer(Timer timer) {
        this.timer_to_add.add(timer);
    }
    public void addCollider(BaseCollider collider){
        collider.setOwner(this);
        this.colliders_to_add.add(collider);
        CollisionHandler.colliders.add(collider);
    }

    public Set<BaseCollider> getColliders() {
        return colliders;
    }

    public int getCollisionRegion() {
        return CollisionRegion;
    }
}
