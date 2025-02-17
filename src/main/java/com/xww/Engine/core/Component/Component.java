package com.xww.Engine.core.Component;


import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Base;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.BaseCollider;
import com.xww.Engine.core.Collision.CollisionHandler;
import com.xww.Engine.core.Event.Message.Impl.MouseMessageHandler;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.Camera;
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

    public static final ActionAfterCollision.ActionAfterCollisionType ComponentDefaultCollisionType = ActionAfterCollision.ActionAfterCollisionType.physics;

    protected ActionAfterCollision.ActionAfterCollisionType callBack = ComponentDefaultCollisionType;

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

    protected boolean whetherCheckCollision = true; // 是否检测碰撞 当此为false 则不会检测碰撞

    protected int CollisionRegion = -1; // -1 代表不检测

    protected Vector last_mouse_check_self_position = Vector.Zero();
    protected boolean is_drag_on = false; // 当此属性为false时 一定不会被注册到 can_drag_object中
    protected boolean whetherCanDrag = false; // 当该组件可以被拖动时 该属性如果是true 则可以拖动并注册到拖动组件中
    
    protected boolean whetherBeRegisteredCanDrag = false;

    // 每个组件自定义级别的展示debug信息 有且仅当DebugSetting.IS_DEBUG_ON 为true并且whetherShowDebugInfo为true时才是开启DEBUG模式
    protected boolean whetherShowDebugInfo = DebugSetting.IS_DEBUG_ON;


    public static void addComponent(Component component) {
        addComponent(component, true);
    }

    public static void addComponent(Component component, boolean registerMouseFlag) {
        components_to_add.add(component);
        if (registerMouseFlag) {
            MouseMessageHandler.mouseMessageHandlerInstance.registerComponent(component);
        }
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
        drawWhetherCanDrag(g);
        if (whetherShowDebugInfo && DebugSetting.IS_DEBUG_ON) showDebugInfo(g);
    }

    public void drawWhetherCanDrag(Graphics g) {
        if (whetherBeRegisteredCanDrag && whetherCanDrag && DebugSetting.IS_DEBUG_ON && this.whetherShowDebugInfo){
            g.setColor(DebugSetting.DebugInfoColor);
            g.drawString("can drag", this.getDrawPosition().getX() - 20, this.getDrawPosition().getY() - 20);
        }
    }

    /**
     * 输出debug信息
     */
    protected void showDebugInfo(Graphics g){
        DebugSetting.ShowDebugInfo(this, g);
    }

    protected void checkDrag() {
        if (!is_drag_on) return;
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
        if (DebugSetting.IS_DEBUG_ON && whetherShowDebugInfo) colliders.forEach(collider -> collider.draw(g));
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
        if (whetherCheckCollision) {
            ActionAfterCollision.CollisionInfo collisionInfo = checkCollision();
            if (collisionInfo.isWhetherCollider()) {
                // 发生碰撞后的回调函数
                if (collisionAction(collisionInfo, true)) {
                    // 只有是自己触发的碰撞才回退移动
                    this.return_move();
                }
            }
        }
    }

    /**
     * 碰撞发生后的行为
     * @param flag 自己触发的碰撞 ture 被动接受为 false
     * @return 是否进行回退移动 true 进行回退 false 不进行回退
     *
     */
    protected boolean collisionAction(ActionAfterCollision.CollisionInfo collisionInfo, boolean flag) {
        switch (this.getCollisionType()){
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
                return false;
            case physics:
                // 对于此逻辑双方只需执行一次
                if (!flag){
                    return true;
                }
                // 如果另一方不是物理碰撞 则进行反弹
                if (collisionInfo.getColliderComponent().getCollisionType() != ActionAfterCollision.ActionAfterCollisionType.physics){
                    // 将另一个不是物理碰撞的当作质量无限大
                    this.reboundVelocity();
                    return true;
                }
                double m1 = this.mass;
                double m2 = collisionInfo.getOtherCollider().getOwner().mass;
                // 如果两个质量都为-1 则进行反弹
                if (m1 == -1 && m2 == -1){
                    collisionInfo.getColliderComponent().reboundVelocity();
                    this.reboundVelocity();
                    return true;
                }
                if (m1 == -1){
                    collisionInfo.getColliderComponent().reboundVelocity();
                    return true;
                } else if (m2 == -1){
                    this.reboundVelocity();
                    return true;
                }
                Vector v1 = this.velocity;
                Vector v2 = collisionInfo.getOtherCollider().getOwner().getVelocity();
                // 计算碰撞后的速度
                Vector v1Prime = v1.scale((m1 - m2) / (m1 + m2)).add_to_self(v2.scale(2 * m2 / (m1 + m2)));
                Vector v2Prime = v2.scale((m2 - m1) / (m1 + m2)).add_to_self(v1.scale(2 * m1 / (m1 + m2)));
                // 设置新的速度
                this.setVelocity(v1Prime);
                collisionInfo.getColliderComponent().setVelocity(v2Prime);
                collisionInfo.getColliderComponent().return_move();
                break;
            default:
                throw new RuntimeException("Component 组件目前不支持 碰撞发生后的指定的此行为: " + this.getCollisionType());

        }
        return true;
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
        lastMove = Vector.Zero();
    }
    /**
     * 移动
     */
    @Override
    public int compareTo(Component other) {
        return Integer.compare(this.order, other.order);
    }


    /**
     * 默认注册到监听鼠标事件的对象组中
     */
    public void addChild(Component child) {
        addChild(child, true);
    }

    /**
     *
     * @param child
     * @param registerMouseFlag 指定是否注册到监听鼠标事件的对象组中
     */
    public void addChild(Component child, boolean registerMouseFlag) {
        child.parent = this;
        this.children_to_add.add(child);
        allComponents_to_add.add(child);
        if (registerMouseFlag) {
            MouseMessageHandler.mouseMessageHandlerInstance.registerComponent(child);
        }
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
        switch (this.positionType) {
            case World -> {
                return worldPosition;
            }
            case Screen -> {
                return Camera.camera_position.add(worldPosition);
            }
            default -> {
                throw new RuntimeException("Component 组件目前不支持 获取世界坐标的坐标类型: " + this.positionType);
            }
        }
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
          return GameFrame.getRealDrawPosition(this.getLeftTopWorldPosition());
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

    /**
     * 被选中后 释放鼠标调用的函数
     */
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

    public ActionAfterCollision.ActionAfterCollisionType getCollisionType() {
        return this.callBack;
    }

    public void setCallBack(ActionAfterCollision.ActionAfterCollisionType callBack){
        this.callBack = callBack;
    }

    public void setWhetherCanDrag(boolean whetherCanDrag) {
        this.whetherCanDrag = whetherCanDrag;
    }

    /**
     * 判断是否与此组件检测碰撞
     * @param other 其它组件
     * @return true 检测碰撞 false 不检测碰撞
     */
    public boolean whetherCheckCollision(Component other) {
        return !(other.getCollisionRegion() == -1 || this.getCollisionRegion() == -1 || this.getCollisionRegion() == other.getCollisionRegion());
    }

    /**
     *
     * @return 是否开启了拖拽功能
     */
    public boolean isIs_drag_on() {
        return is_drag_on;
    }

    public void setIs_drag_on(boolean is_drag_on) {
        this.is_drag_on = is_drag_on;
    }

    public boolean isWhetherCheckCollision() {
        return whetherCheckCollision;
    }

    public void setWhetherCheckCollision(boolean whetherCheckCollision) {
        this.whetherCheckCollision = whetherCheckCollision;
    }

    public void setCollisionRegion(int collisionRegion) {
        CollisionRegion = collisionRegion;
    }

    public boolean isWhetherCanDrag() {
        return whetherCanDrag;
    }

}
