package com.xww.Engine.core.Component;


import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Base;
import com.xww.Engine.core.Collision.*;
import com.xww.Engine.core.Event.Message.Impl.MouseMessageHandler;
import com.xww.Engine.core.Timer.TimerManager;
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
    public static final boolean[] x_y_return = new boolean[]{true, true};
    public static final boolean[] x_y_no_return = new boolean[]{false, false};

    public static final boolean[] x_return = new boolean[]{true, false};
    public static final boolean[] y_return = new boolean[]{false, true};

    public static final int GRAVITY = 98 * 9;

    public static final Vector gravity = Vector.build(0, GRAVITY);

    public static Set<Component> can_drag_object_to_add = new HashSet<>();
    public static Set<Component> can_drag_object = new HashSet<>();

    public static Set<Component> can_drag_object_to_remove = new HashSet<>();

    public static final ActionAfterCollision.ActionAfterCollisionType ComponentDefaultCollisionType = ActionAfterCollision.ActionAfterCollisionType.stop;

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
    protected Vector worldPosition; // 组件世界坐标
    protected GameFrame.PositionType positionType = GameFrame.PositionType.World; // 默认为世界坐标

    protected Vector size; // 组件大小
    protected AnchorMode anchorMode = AnchorMode.LeftTop; // 锚点模式 是指坐标指定的位置

    protected Vector velocity; // 速度 每秒多少像素
    protected Vector acceleration; // 加速度 每秒多少像素

    protected int order = 0; // 绘制顺序 越大图层越靠前

    public static final int infiniteMass = -1;

    protected int mass = infiniteMass; // 质量 infiniteMass 代表固定 质量无限大

    protected boolean isAlive = true;

    /**
     * 上次的移动, 用于碰撞发生时的回退
     */
    protected Vector lastMove = Vector.Zero();

    protected boolean whetherCheckCollision = true; // 是否检测碰撞 在自身的碰撞体启用的前提下 当此为false 则不会检测碰撞


    // 主动检测碰撞的层级 例如检测伤害的受击碰撞体 只需另activeCollisionZone设置为0 hitCollision设置为指定的值
    protected int activeCollisionZone = CollisionDefaultConstValue.noCollisionChecking; // 主动碰撞区域 0 代表 不会主动检测碰撞

    // 被动接受碰撞的层级
    protected int hitCollisionZone = CollisionDefaultConstValue.noCollisionChecking; // 被动接受的碰撞区域 0 代表 不会被其他组件主动检测碰撞

    protected Vector last_mouse_check_self_position = Vector.Zero();
    protected boolean is_drag_on = false; // 当此属性为false时 一定不会被注册到 can_drag_object中
    protected boolean whetherCanDrag = false; // 当该组件可以被拖动时 该属性如果是true 则可以拖动并注册到拖动组件中
    
    protected boolean whetherBeRegisteredCanDrag = false;

    // 每个组件自定义级别的展示debug信息 有且仅当DebugSetting.IS_DEBUG_ON 为true并且whetherShowDebugInfo为true时才是开启DEBUG模式
    protected boolean whetherShowDebugInfo = DebugSetting.IS_DEBUG_ON;

    protected boolean enableGravity = false; // 是否启用重力模拟

    /**
     * 默认会注册到 MouseMessageHandler.mouseMessageHandlerInstance中 进行处理鼠标事件
     * @param component 添加组件
     */

    public static void addComponent(Component component) {
        addComponent(component, true);
    }

    /**
     *
     * @param component 添加组件
     * @param registerMouseFlag 是否注册到MouseMessageHandler.mouseMessageHandlerInstance中
     */
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
        checkGravity();
        checkMove();
        // 更新子组件
        update_children(g);
        // 更新碰撞器组件
        update_collider();
        drawCollider(g);
        drawWhetherCanDrag(g);
        if (whetherShowDebugInfo && DebugSetting.IS_DEBUG_ON) showDebugInfo(g);
    }

    protected void checkGravity() {
        if (enableGravity){
            this.velocity.add_to_self(GameFrame.getFrameVelocity(gravity));
        }
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
        this.velocity = this.velocity.add_to_self(GameFrame.getFrameVelocity(this.acceleration));
        // 预移动
        pre_move();
        // 检查碰撞
        // 当组件需要检测碰撞时才进行检测 并且需要主动碰撞
        if (whetherCheckCollision && activeCollisionZone != CollisionDefaultConstValue.noCollisionChecking) {
            ActionAfterCollision.CollisionInfo collisionInfo = checkCollision();
            // 如果发生碰撞执行下面的逻辑
            if (collisionInfo.isWhetherCollider()) {
                // 发生碰撞后的回调函数
                this.return_move(collisionAction(collisionInfo, true));
            }
        }
    }

    /**
     * 碰撞发生后的行为
     * @param flag 自己触发的碰撞 ture 被动接受为 false
     * @return 是否进行回退移动 true 进行回退 false 不进行回退
     *
     */
    protected boolean[] collisionAction(ActionAfterCollision.CollisionInfo collisionInfo, boolean flag) {
        switch (this.getCollisionType()){
            case stop:
                return processStopCollision(collisionInfo);
            case rebound:
                // 将速度进行反转
                this.reboundVelocity();
                break;
            case die:
                this.setAlive(false);
                break;
            case nullAction:
                return x_y_no_return;
            case physics:
                // 对于此逻辑双方只需执行一次
                if (!flag){
                    return x_y_return;
                }
                // 如果另一方不是物理碰撞 则进行反弹
                if (collisionInfo.getColliderComponent().getCollisionType() != ActionAfterCollision.ActionAfterCollisionType.physics){
                    // 将另一个不是物理碰撞的当作质量无限大
                    this.reboundVelocity();
                    return x_y_return;
                }
                double m1 = this.mass;
                double m2 = collisionInfo.getOtherCollider().getOwner().mass;
                // 如果两个质量都为 infiniteMass 则进行反弹
                if (m1 == infiniteMass && m2 == infiniteMass){
                    collisionInfo.getColliderComponent().reboundVelocity();
                    this.reboundVelocity();
                    return x_y_return;
                }
                if (m1 == infiniteMass){
                    collisionInfo.getColliderComponent().reboundVelocity();
                    return x_y_return;
                } else if (m2 == infiniteMass){
                    this.reboundVelocity();
                    return x_y_return;
                }
                Vector v1 = this.velocity;
                Vector v2 = collisionInfo.getOtherCollider().getOwner().getVelocity();
                // 计算碰撞后的速度
                Vector v1Prime = v1.scale((m1 - m2) / (m1 + m2)).add_to_self(v2.scale(2 * m2 / (m1 + m2)));
                Vector v2Prime = v2.scale((m2 - m1) / (m1 + m2)).add_to_self(v1.scale(2 * m1 / (m1 + m2)));
                // 设置新的速度
                this.setVelocity(v1Prime);
                collisionInfo.getColliderComponent().setVelocity(v2Prime);
                collisionInfo.getColliderComponent().return_move(x_y_return);
                break;
            default:
                throw new RuntimeException("Component 组件目前不支持 碰撞发生后的指定的此行为: " + this.getCollisionType());

        }
        return x_y_return;
    }

    private boolean[] processStopCollision(ActionAfterCollision.CollisionInfo collisionInfo) {
        // TODO 需要判断在哪个方向进行回退运动
        // 判断是在哪个方向
        this.clearVeAc();
        return x_y_return;
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
        this.worldPosition.add_to_self(lastMove);
    }

    /**
     * 回退运动
     */
    protected void return_move(boolean[] x_y) {
        if (x_y[0]){
            this.worldPosition.sub_to_self(Vector.build(lastMove.getFullX(), 0));
        }
        if (x_y[1]){
            this.worldPosition.sub_to_self(Vector.build(0, lastMove.getFullY()));
        }
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
        TimerManager.instance.registerTimer(timer);
    }
    public void addCollider(BaseCollider collider){
        collider.setOwner(this);
        this.colliders_to_add.add(collider);
        CollisionHandler.colliders.add(collider);
    }

    public Set<BaseCollider> getColliders() {
        return colliders;
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
    public final boolean whetherCheckCollision(Component other) {
        // this.activeCollisionZone 与 other.hitCollisionZone 有交集
        return (this.activeCollisionZone & other.hitCollisionZone) != 0;
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


    public boolean isWhetherCanDrag() {
        return whetherCanDrag;
    }

    public int getActiveCollisionZone() {
        return activeCollisionZone;
    }

    public void registerActiveCollisionZone(int activeCollisionZone) {
        if (activeCollisionZone == CollisionDefaultConstValue.noCollisionChecking) return;
        if (this.activeCollisionZone == CollisionDefaultConstValue.noCollisionChecking) this.activeCollisionZone = activeCollisionZone;
        else this.activeCollisionZone |= activeCollisionZone;
    }

    public int getHitCollisionZone() {
        return hitCollisionZone;
    }

    public void registerHitCollisionZone(int hitCollisionZone) {
        if (hitCollisionZone == CollisionDefaultConstValue.noCollisionChecking) return;
        if (this.hitCollisionZone == CollisionDefaultConstValue.noCollisionChecking) this.hitCollisionZone = hitCollisionZone;
        else this.hitCollisionZone |= hitCollisionZone;
    }

    public boolean isEnableGravity() {
        return enableGravity;
    }

    public void setEnableGravity(boolean enableGravity) {
        this.enableGravity = enableGravity;
    }
}
