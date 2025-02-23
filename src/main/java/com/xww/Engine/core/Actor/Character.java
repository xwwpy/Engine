package com.xww.Engine.core.Actor;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Event.Message.Impl.KeyBoardMessageHandler;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public abstract class Character extends FreeComponent {

    public enum CharacterType {
        Player,
        Enemy
    }

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
    protected int blinkCount = 100; // 闪烁次数
    protected Timer invulnerableStateTimer; // 切换无敌状态定时器

    protected boolean whetherFacingLeft; // 是否朝向左
    protected int jumpSpeed; // 跳跃速度

    protected int runSpeed; // 跑动速度

    protected boolean whetherJumping = false; // 当进行跳跃后只有接触地面才会置为false 在跳跃中以及跳跃后的下降过程 都为true

    protected int jumpMaxCount; // 支持连跳的次数
    protected int jumpCount; // 当前的跳跃次数
    protected boolean whetherCanJumpThroughWall; // 在跳跃过程中是否可以穿过物体

    protected boolean whetherOnGround = false;

    protected boolean whetherRunLeft = false;
    protected boolean whetherRunRight = false;

    protected Map<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation; // 当前的动画

    public Character(Vector worldPosition,
                     Vector size,
                     int mass,
                     int hp,
                     int atk_interval,
                     int invulnerableTime,
                     boolean whetherFacingLeft,
                     int jumpMaxCount,
                     int jumpSpeed,
                     int runSpeed,
                     CharacterType characterType,
                     boolean whetherCanJumpThroughWall) {
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
        this.whetherFacingLeft = whetherFacingLeft;
        this.jumpMaxCount = jumpMaxCount;
        this.jumpCount = 0;
        this.jumpSpeed = jumpSpeed;
        this.runSpeed = runSpeed;
        this.whetherCanJumpThroughWall = whetherCanJumpThroughWall;
        this.characterType = characterType;
        // 攻击间隔定时器
        atk_intervalTimer = new Timer(atk_interval, (obj) -> {
            this.whetherCanAtk = true;
        }, this);
        atk_intervalTimer.setRun_times(1);
        atk_intervalTimer.stopStart();
        // 无敌闪烁
        blinkTimer = new Timer((double) invulnerableTime / blinkCount, (obj) -> {
            this.whetherRender = !this.whetherRender;
        }, this);
        blinkTimer.setRun_times(blinkCount * 2);
        blinkTimer.stopStart();
        // 切换无敌状态
        invulnerableStateTimer = new Timer(invulnerableTime, (obj) -> {
            this.whetherInvulnerable = false;
        }, this);
        invulnerableStateTimer.setRun_times(1);
        invulnerableStateTimer.stopStart();
        this.addTimer(atk_intervalTimer);
        this.addTimer(blinkTimer);
        this.addTimer(invulnerableStateTimer);
        if (characterType == CharacterType.Player){
            // 监听键盘事件
            KeyBoardMessageHandler.keyBoardMessageHandlerInstance.registerComponent(this);
        }
    }

    public void addAnimation(String name, Animation animation) {
        animations.put(name, animation);
    }

    public void setAnimation(String name) {
        Animation animation = animations.get(name);
        if (animation == null){
            System.out.println("animation: " + name + " is null");
            return;
        }
        currentAnimation = animation;
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
        this.velocity.x = tempVelocity;
        super.checkMove();
    }

    /**
     * 执行渲染逻辑
     */
    protected void on_render(Graphics g) {
        // 选择动画
        if (currentAnimation != null) {
            if (whetherRender) currentAnimation.on_update(g);
        } else {
            System.out.println("currentAnimation is null");
        }
    }

    @Override
    public void processKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    this.whetherRunLeft = true;
                    this.whetherFacingLeft = true;
                    break;
                case KeyEvent.VK_D:
                    this.whetherRunRight = true;
                    this.whetherFacingLeft = false;
                    break;
                case KeyEvent.VK_W:
                    if (this.jumpCount < this.jumpMaxCount) {
                        this.velocity.y = -jumpSpeed;
                        this.whetherJumping = true;
                        this.whetherOnGround = false;
                        this.jumpCount++;
                    }
                    break;
                case KeyEvent.VK_J:
                    if (this.whetherCanAtk) {
                        if (this.tryAtk()) {
                            this.whetherCanAtk = false;
                            this.atk_intervalTimer.restart();
                        }
                    }
                    break;
                default:
                    break;
            }
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    this.whetherRunLeft = false;
                    break;
                case KeyEvent.VK_D:
                    this.whetherRunRight = false;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *  尝试攻击
     * @return 攻击成功返回true，否则返回false
     */
    protected abstract boolean tryAtk();


    private void resetJumpState() {
        this.whetherJumping = false;
        this.jumpCount = 0;
    }

    @Override
    public void receiveCollision(ActionAfterCollision.CollisionInfo collisionInfo) {
        Component otherComponent =  collisionInfo.getOtherCollider().getOwner();
        if (otherComponent instanceof Bullet bullet) {
            if (!whetherInvulnerable) {
                this.currentHp -= bullet.getDamage();
                this.whetherInvulnerable = true;
                invulnerableStateTimer.restart();
                blinkTimer.restart();
                this.onHit();
            } else {
                onInvulnerableHit();
            }
        } else {
            System.out.println("Component: " + this.getClass().getName() + " receiveCollision: " + otherComponent.getClass().getName() + " is not supported");
        }
    }

    /**
     * 在无敌状态时受到攻击
     */
    protected abstract void onInvulnerableHit();

    /**
     * 受到攻击
     */
    protected abstract void onHit();
}
