package com.xww.Engine.core.Component;


import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Base;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.BaseCollider;
import com.xww.Engine.core.Collision.CollisionHandler;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.setting.DebugSetting;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

@SuppressWarnings("all")
public abstract class Component implements Base, Comparable<Component> {

    public static Set<Component> can_drag_object_to_add = new HashSet<>();
    public static Set<Component> can_drag_object = new HashSet<>();

    public static Set<Component> can_drag_object_to_remove = new HashSet<>();

    public static final ActionAfterCollision.CollisionCallBack ComponentDefaultCallBack = (obj)-> ActionAfterCollision.ActionAfterCollisionType.physics;

    public static Set<Component> components = new HashSet<>(); // 只包含自由组件 即顶层组件
    public static Set<Component> components_to_add = new HashSet<>();
    public static Set<Component> components_to_remove = new HashSet<>();

    public static Set<Component> allComponents = new HashSet<>(); // 所有组件 包括自由组件和子组件
    public static Set<Component> allComponents_to_add = new HashSet<>();
    public static Set<Component> allComponents_to_remove = new HashSet<>();


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

    protected int mass = 1; // 质量 -1 代表固定 质量无限大

    protected boolean isAlive = true;

    /**
     * 上次的移动, 用于碰撞发生时的回退
     */
    protected Vector lastMove = Vector.Zero();

    protected int CollisionRegion = -1; // -1 代表不检测

    protected Vector last_mouse_check_self_position = Vector.Zero();
    
    public boolean whetherCanDrag = false;
    
    protected boolean whetherBeRegisteredCanDrag = false;

    // 每个组件自定义级别的展示debug信息
    protected boolean whetherShowDebugInfo = DebugSetting.IS_DEBUG_ON;


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
        if (!this.isAlive()){
            Component.components_to_remove.add(this);
        }
        // 检查拖动属性
        checkDrag();
        checkMove();
        // 更新子组件
        update_children(g);
        // 更新定时器
        update_timer();
        // 更新碰撞器组件
        update_collider();
        drawCollider(g);
        if (whetherBeRegisteredCanDrag && whetherCanDrag){
            g.setColor(DebugSetting.DebugInfoColor);
            g.drawString("can drag", this.getDrawPosition().getX() - 20, this.getDrawPosition().getY() - 20);
        }
        if (whetherShowDebugInfo && DebugSetting.IS_DEBUG_ON) showDebugInfo(g);
    }

    /**
     * 输出debug信息
     */
    protected void showDebugInfo(Graphics g){
        DebugSetting.ShowDebugInfo(this, g);
    }

    protected void checkDrag() {
        if (whetherCanDrag && !whetherBeRegisteredCanDrag){
            Component.registerDragComponent(this);
            this.whetherBeRegisteredCanDrag = true;
            return;
        }
        if (whetherBeRegisteredCanDrag && !whetherCanDrag) {
            Component.unregisterDragComponent(this);
            this.whetherBeRegisteredCanDrag = false;
        }
    }

    protected void drawCollider(Graphics g) {
        colliders.forEach(collider -> collider.draw(g));
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
        ActionAfterCollision.CollisionInfo collisionInfo = checkCollision();
        if (collisionInfo.isWhetherCollider()) {
            // 发生碰撞后的回调函数
            collisionAction(collisionInfo, true);
            // 只有是自己触发的碰撞才回退移动
            this.return_move();
        }
    }

    /**
     * 碰撞发生后的行为
     * @param flag 自己触发的碰撞 ture 被动接受为 false
     */
    protected void collisionAction(ActionAfterCollision.CollisionInfo collisionInfo, boolean flag) {
        ActionAfterCollision.ActionAfterCollisionType actionAfterCollisionType = ComponentDefaultCallBack.callBack(collisionInfo);
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
            case physics:
                // 对于此逻辑双方只需执行一次
                if (!flag){
                    return;
                }
                double m1 = this.mass;
                double m2 = collisionInfo.getOtherCollider().getOwner().mass;
                if (m1 == -1){
                    collisionInfo.getColliderComponent().reboundVelocity();
                    return;
                } else if (m2 == -1){
                    this.reboundVelocity();
                    return;
                }
                Vector v1 = this.velocity;
                Vector v2 = collisionInfo.getOtherCollider().getOwner().getVelocity();
                // 计算碰撞后的速度
                Vector v1Prime = v1.scale((m1 - m2) / (m1 + m2)).add_to_self(v2.scale(2 * m2 / (m1 + m2)));
                Vector v2Prime = v2.scale((m2 - m1) / (m1 + m2)).add_to_self(v1.scale(2 * m1 / (m1 + m2)));
                // 设置新的速度
                this.setVelocity(v1Prime);
                collisionInfo.getColliderComponent().setVelocity(v2Prime);
                break;
            default:
                throw new RuntimeException("Component 组件目前不支持 碰撞发生后的指定的此行为: " + actionAfterCollisionType);
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
    protected ActionAfterCollision.CollisionInfo checkCollision() {
        return CollisionHandler.checkCollision(this);
    }

    /**
     * 被动接受碰撞事件
     * @param collisionInfo 发生碰撞的信息
     */

    public void receiveCollision(ActionAfterCollision.CollisionInfo collisionInfo){
        collisionAction(collisionInfo, false);
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
        allComponents_to_add.add(child);
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
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

    public boolean isWhetherShowDebugInfo() {
        return whetherShowDebugInfo;
    }

    public void setWhetherShowDebugInfo(boolean whetherShowDebugInfo) {
        this.whetherShowDebugInfo = whetherShowDebugInfo;
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

    public void processMouseEvent(MouseEvent e){
        // TODO
    }

    public void processKeyEvent(KeyEvent e){
        // TODO
    }

    public void changePosition(Vector tar) {
        this.worldPosition.add_to_self(tar);
    }

    public static void updateDragComponents() {
        can_drag_object.addAll(can_drag_object_to_add);
        can_drag_object_to_add.clear();
        can_drag_object.forEach((Component) -> {
            if (!Component.isAlive){
                can_drag_object_to_remove.add(Component);
            }
        });
        can_drag_object.removeAll(can_drag_object_to_remove);
        can_drag_object_to_remove.clear();

    }


    public void process_mouse_choose_self(int x, int y) {
        int x_gap = x - this.last_mouse_check_self_position.getX();
        int y_gap = y - this.last_mouse_check_self_position.getY();
        this.changePosition(new Vector(x_gap, y_gap));
        last_mouse_check_self_position = new Vector(x, y);
    }

    public boolean whether_mouse_in(double x, double y) {
        Vector absolutePosition = this.getDrawPosition();
        if (x >= absolutePosition.getFullX() && x <= absolutePosition.getFullX() + this.size.getFullX()
                && y >= absolutePosition.getFullY() && y <= absolutePosition.getFullY() + this.size.getFullY()) {
            this.last_mouse_check_self_position = new Vector(x, y + 29);
            return true;
        } else {
            return false;
        }
    }

    public void process_mouse_release() {
        // do nothing
    }

    /**
     * 注册可拖拽组件 必须先被添加在组件列表中 或者作为 子类节点
     */
    public static void registerDragComponent(Component component){
        if (component.getSize().getX() == 0 && component.getSize().getY() == 0){
            return;
        }
        can_drag_object_to_add.add(component);
    }
    /**
     * 解注册可拖拽组件
     */
    public static void unregisterDragComponent(Component component) {
        Component.can_drag_object_to_remove.add(component);
    }

    public void registerDrag() {
        this.whetherCanDrag = true;
    }
}
