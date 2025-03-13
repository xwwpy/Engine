package com.xww.Engine.core.Actor;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Barrier.BaseGround;
import com.xww.Engine.core.Barrier.BaseWall;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Event.Message.Impl.KeyBoardMessageHandler;
import com.xww.Engine.core.Event.TimeEventManager;
import com.xww.Engine.core.StateManager.StateMachine;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public abstract class Character extends FreeComponent {

    public enum CharacterType {
        Player,
        Enemy
    }

    protected Vector logicSize; // 实际大小
    protected Vector relativePosition; // 角色实际大小的左上角距离指定大小左上角的距离

    protected CharacterType characterType;
    protected int hp; // 生命值
    protected int currentHp; // 当前生命值
    protected float atk_interval; // 攻击的间隔

    protected Timer atk_intervalTimer; // 攻击间隔定时器

    protected boolean whetherCanAtk = true;

    protected int invulnerableTime; // 被攻击后的无敌时间
    protected boolean whetherInvulnerable; // 是否处于无敌状态

    protected boolean whetherRender = true; // 是否进行渲染 配合闪烁定时器实现闪烁效果
    protected Timer blinkTimer; // 无敌闪烁定时器
    protected int blinkCount = 30; // 闪烁次数
    protected Timer invulnerableStateTimer; // 切换无敌状态定时器

    protected boolean whetherFacingLeft; // 是否朝向左
    protected int jumpSpeed; // 跳跃速度

    protected int runSpeed; // 跑动速度

    protected boolean whetherJumping = false; // 当进行跳跃后只有接触地面才会置为false 在跳跃中以及跳跃后的下降过程 都为true

    protected int jumpMaxCount; // 支持连跳的次数
    protected int jumpCount; // 当前的跳跃次数

    protected boolean whetherOnGround = false;

    protected BaseGround lastOnGround = null;
    protected BaseGround cantOnThisGround = null;
    protected boolean whetherDownGround = false;

    protected boolean whetherRunLeft = false;
    protected boolean whetherRunRight = false;

    protected Map<String, Animation> animations = new HashMap<>();

    protected StateMachine stateMachine = new StateMachine(null); // 状态机

    protected Bullet lastBeAttackedBullet = null;

    protected long lastBeAttackedTime = 0; // 单位纳秒

    protected final Timer rollCdTimer;

    protected boolean isRolling = false;

    protected final Timer rollWholeTimeTimer;

    protected int rollCd;

    protected int rollWholeTime; // 翻滚的持续时间 默认与无敌时间一致

    protected double rollSpeed;

    protected boolean whetherCanRoll = true;

    protected boolean whetherAtking = false;

    protected Timer atkBackSwingTimer;
    protected int atkBackSwingTime;

    public Character(Vector worldPosition,
                     Vector size,
                     Vector logicSize, // 角色实际大小
                     Vector relativePosition, // 角色实际大小的左上角距离指定大小左上角的距离
                     int mass,
                     int hp,
                     int atk_interval,
                     int invulnerableTime,
                     int rollCd,
                     double rollSpeed,
                     boolean whetherFacingLeft,
                     int jumpMaxCount,
                     int jumpSpeed,
                     int runSpeed,
                     int atkBackSwingTime,
                     CharacterType characterType) {
        super(worldPosition,
                GameFrame.PositionType.World,
                size,
                AnchorMode.LeftTop,
                Vector.Zero(),
                Vector.Zero(),
                Integer.MAX_VALUE - 2,
                CollisionDefaultConstValue.noCollisionChecking,
                CollisionDefaultConstValue.noCollisionChecking,
                mass,
                true,
                false);
        // 开启重力模拟
        this.enableGravity = true;
        this.hp = hp;
        this.currentHp = hp;
        this.atk_interval = atk_interval;
        this.invulnerableTime = invulnerableTime;
        this.rollCd = rollCd;
        this.rollWholeTime = this.rollCd;
        this.rollSpeed = rollSpeed;
        this.whetherFacingLeft = whetherFacingLeft;
        this.jumpMaxCount = jumpMaxCount;
        this.jumpCount = 0;
        this.jumpSpeed = jumpSpeed;
        this.runSpeed = runSpeed;
        this.characterType = characterType;
        this.logicSize = logicSize;
        this.relativePosition = relativePosition;
        this.atkBackSwingTime = atkBackSwingTime;
        // 攻击间隔定时器
        atk_intervalTimer = new Timer(atk_interval, (obj) -> {
            this.whetherCanAtk = true;
        }, this);
        atk_intervalTimer.setRun_times(1);
        atk_intervalTimer.stopStart();
        atk_intervalTimer.neverOver();
        // 无敌闪烁
        blinkTimer = new Timer((double) invulnerableTime / blinkCount, (obj) -> {
            ((Character) obj).whetherRender = !((Character) obj).whetherRender;
        }, this);
        blinkTimer.setFinalCallback((owner)->{
            ((Character) owner).whetherRender = true;
        });
        blinkTimer.setRun_times(blinkCount);
        blinkTimer.stopStart();
        blinkTimer.neverOver();
        // 切换无敌状态
        invulnerableStateTimer = new Timer(invulnerableTime, (obj) -> {
            this.whetherInvulnerable = false;
            this.whetherRender = true;
        }, this);
        invulnerableStateTimer.setRun_times(1);
        invulnerableStateTimer.stopStart();
        invulnerableStateTimer.neverOver();
        // 翻滚Cd
        rollCdTimer = new Timer(rollCd, (obj) -> {
            this.whetherCanRoll = true;
        }, this);
        rollCdTimer.setRun_times(1);
        rollCdTimer.stopStart();
        rollCdTimer.neverOver();

        rollWholeTimeTimer = new Timer(this.invulnerableTime, (obj) -> {
            this.isRolling = false;
        }, this);
        rollWholeTimeTimer.setRun_times(1);
        rollWholeTimeTimer.stopStart();
        rollWholeTimeTimer.neverOver();

        atkBackSwingTimer = new Timer(atkBackSwingTime, (obj) -> {
            this.whetherAtking = false;
        }, this);
        atkBackSwingTimer.setRun_times(1);
        atkBackSwingTimer.stopStart();
        atkBackSwingTimer.neverOver();

        this.addTimer(atk_intervalTimer);
        this.addTimer(blinkTimer);
        this.addTimer(invulnerableStateTimer);
        this.addTimer(rollCdTimer);
        this.addTimer(rollWholeTimeTimer);
        this.addTimer(atkBackSwingTimer);
        if (characterType == CharacterType.Player){
            // 监听键盘事件
            KeyBoardMessageHandler.keyBoardMessageHandlerInstance.registerComponent(this);
        }
    }

    public void addAnimation(String name, Animation animation) {
        animations.put(name, animation);
    }

    @Override
    public void on_update(Graphics g) {
        on_render(g);
        super.on_update(g);
    }

    @Override
    protected void checkGravity() {
        if (!whetherOnGround) super.checkGravity();
    }

    @Override
    protected void checkMove() {
        double tempVelocity = 0;
        if (this.whetherRunLeft) {
            tempVelocity -= runSpeed;
        }
        if (this.whetherRunRight) {
            tempVelocity += runSpeed;
        }
        update_direction();
        // 翻滚时禁止移动
        if (!this.isRolling) {
            this.velocity.x = tempVelocity;
            this.velocity = this.velocity.add_to_self(GameFrame.getFrameVelocity(this.acceleration));
        }
        // 预移动
        Vector position = this.getLogicPosition();
        pre_move();
        Vector position1 = this.getLogicPosition();
        BaseGround.whetherOnGround(position, position1, this);
        BaseWall.whetherCrossWall(position, position1, this);
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

    @Override
    public void process_mouse_choose_self(int x, int y) {
        double last_mouse_check_self_positionFullY = this.last_mouse_check_self_position.getFullY();
        if (y - last_mouse_check_self_positionFullY < 0) this.whetherOnGround = false;
        super.process_mouse_choose_self(x, y);
    }

    /**
     * 执行渲染逻辑
     */
    protected void on_render(Graphics g) {
        this.stateMachine.update(g);
        Vector drawPosition = this.getDrawPosition();
        g.setColor(Color.RED);
        g.drawRect(drawPosition.getX(), drawPosition.getY() - 30, this.hp * 2, 20);
        g.fillRect(drawPosition.getX(), drawPosition.getY() - 30, this.currentHp * 2, 20);
        g.setColor(Color.BLACK);
        g.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        g.drawString("" + this.currentHp + "/" + this.hp, drawPosition.getX(), drawPosition.getY() - 20);
    }

    /**
     *  尝试攻击
     * @return 攻击成功返回true，否则返回false
     */
    protected abstract boolean tryAtk();


    protected void resetJumpState() {
        this.whetherJumping = false;
        this.jumpCount = 0;
    }

    @Override
    public void receiveCollision(ActionAfterCollision.CollisionInfo collisionInfo) {

    }

    public void attackByBullet(Bullet bullet){
        if (!whetherInvulnerable) {
            lastBeAttackedBullet = bullet;
            lastBeAttackedTime = TimeEventManager.currentTime;
            this.currentHp -= bullet.getDamage();
            invulnerableStateTimer.restart();
            if (blinkTimer.whetherOnTheEnd()){
                blinkTimer.restart();
            }
            this.onHit();
        } else {
            onInvulnerableHit();
        }
    }

    /**
     * 单位纳秒
     * @return 得到上次被攻击后到现在的时间
     */
    public long getLastBeAttackedTime() {
        return (TimeEventManager.currentTime - lastBeAttackedTime) / 1000_1000;
    }

    /**
     * 在无敌状态时受到攻击
     */
    protected abstract void onInvulnerableHit();

    protected void beInvulnerable(){
        this.whetherInvulnerable = true;
        invulnerableStateTimer.restart();
    }

    /**
     * 受到攻击
     */
    protected abstract void onHit();

    public void on_ground(BaseGround barrier) {
        if (whetherOnGround && barrier == lastOnGround) return;
        if (this.whetherDownGround){
            if (barrier == cantOnThisGround){
                return;
            } else {
                this.whetherDownGround = false;
                this.cantOnThisGround = null;
            }
        }
        this.whetherOnGround = true;
        lastOnGround = barrier;
        resetJumpState();
        if (this.velocity.getFullY() > 0){
            this.processFall(this.velocity.getFullY());
            this.velocity.setY(0);
        }
        // 修正位置
        double dis = barrier.getWorldPosition().getFullY() - (this.getLogicSize().getFullY() + this.getLogicPosition().getFullY());
        this.worldPosition.add_to_self(Vector.build(0, dis));
        // 防止某些极端情况
        lastMove.setY(0);
    }

    /**
     * 处理坠落时的逻辑
     * @param fullY 坠落时的速度
     */
    protected void processFall(double fullY) {

    }


    public boolean isWhetherOnGround() {
        return whetherOnGround;
    }

    public void setWhetherOnGround(boolean whetherOnGround) {
        this.whetherOnGround = whetherOnGround;
    }

    public Vector getLogicSize() {
        return logicSize;
    }

    public void setLogicSize(Vector logicSize) {
        this.logicSize = logicSize;
    }

    public Vector getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Vector relativePosition) {
        this.relativePosition = relativePosition;
    }

    public Vector getLogicPosition() {
        return this.getLeftTopWorldPosition().add(this.relativePosition);
    }

    public BaseGround getLastOnGround() {
        return lastOnGround;
    }

    public void setLastOnGround(BaseGround lastOnGround) {
        this.lastOnGround = lastOnGround;
    }


    protected void onRoll() {

    }
    @Override
    public void on_destroy() {
        this.atk_intervalTimer.canOver();
        this.atk_intervalTimer.stop();

        this.blinkTimer.canOver();
        this.blinkTimer.stop();

        this.invulnerableStateTimer.canOver();
        this.invulnerableStateTimer.stop();
        super.on_destroy();
    }

    protected void update_direction() {
        if (this.velocity.getFullX() > 0) {
            this.whetherFacingLeft = false;
        } else if (this.velocity.getFullX() < 0) {
            this.whetherFacingLeft = true;
        }
    }

    public CharacterType getCharacterType() {
        return characterType;
    }

    public void setCharacterType(CharacterType characterType) {
        this.characterType = characterType;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public float getAtk_interval() {
        return atk_interval;
    }

    public void setAtk_interval(float atk_interval) {
        this.atk_interval = atk_interval;
    }

    public boolean isWhetherCanAtk() {
        return whetherCanAtk;
    }

    public void setWhetherCanAtk(boolean whetherCanAtk) {
        this.whetherCanAtk = whetherCanAtk;
    }

    public boolean isWhetherInvulnerable() {
        return whetherInvulnerable;
    }

    public void setWhetherInvulnerable(boolean whetherInvulnerable) {
        this.whetherInvulnerable = whetherInvulnerable;
    }

    public boolean isWhetherRender() {
        return whetherRender;
    }

    public void setWhetherRender(boolean whetherRender) {
        this.whetherRender = whetherRender;
    }

    public boolean isWhetherFacingLeft() {
        return whetherFacingLeft;
    }

    public void setWhetherFacingLeft(boolean whetherFacingLeft) {
        this.whetherFacingLeft = whetherFacingLeft;
    }

    public int getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(int jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public int getRunSpeed() {
        return runSpeed;
    }

    public void setRunSpeed(int runSpeed) {
        this.runSpeed = runSpeed;
    }

    public boolean isWhetherJumping() {
        return whetherJumping;
    }

    public void setWhetherJumping(boolean whetherJumping) {
        this.whetherJumping = whetherJumping;
    }

    public int getJumpMaxCount() {
        return jumpMaxCount;
    }

    public void setJumpMaxCount(int jumpMaxCount) {
        this.jumpMaxCount = jumpMaxCount;
    }

    public int getJumpCount() {
        return jumpCount;
    }

    public void setJumpCount(int jumpCount) {
        this.jumpCount = jumpCount;
    }

    public BaseGround getCantOnThisGround() {
        return cantOnThisGround;
    }

    public void setCantOnThisGround(BaseGround cantOnThisGround) {
        this.cantOnThisGround = cantOnThisGround;
    }

    public boolean isWhetherDownGround() {
        return whetherDownGround;
    }

    public void setWhetherDownGround(boolean whetherDownGround) {
        this.whetherDownGround = whetherDownGround;
    }

    public boolean isWhetherRunLeft() {
        return whetherRunLeft;
    }

    public void setWhetherRunLeft(boolean whetherRunLeft) {
        this.whetherRunLeft = whetherRunLeft;
    }

    public boolean isWhetherRunRight() {
        return whetherRunRight;
    }

    public void setWhetherRunRight(boolean whetherRunRight) {
        this.whetherRunRight = whetherRunRight;
    }


    public boolean isRolling() {
        return isRolling;
    }

    public void setRolling(boolean rolling) {
        isRolling = rolling;
    }

    public int getRollWholeTime() {
        return rollWholeTime;
    }

    public void setRollWholeTime(int rollWholeTime) {
        this.rollWholeTime = rollWholeTime;
    }

    public double getRollSpeed() {
        return rollSpeed;
    }

    public void setRollSpeed(double rollSpeed) {
        this.rollSpeed = rollSpeed;
    }

    public boolean isWhetherCanRoll() {
        return whetherCanRoll;
    }

    public void setWhetherCanRoll(boolean whetherCanRoll) {
        this.whetherCanRoll = whetherCanRoll;
    }

    public int getAtkBackSwingTime() {
        return atkBackSwingTime;
    }

    public void setAtkBackSwingTime(int atkBackSwingTime) {
        this.atkBackSwingTime = atkBackSwingTime;
    }

    public boolean isWhetherAtking() {
        return whetherAtking;
    }

    public void setWhetherAtking(boolean whetherAtking) {
        this.whetherAtking = whetherAtking;
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }
}
