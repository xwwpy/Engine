package com.xww.projects.game02.content.Player;

import com.xww.Engine.core.Actor.Bullet;
import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Event.TimeEventManager;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.setting.DebugSetting;
import com.xww.projects.game02.content.Player.states.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Character {

    public static final int atkZone = 0b1000;
    public static final int beHitZone = 0b10000;

    public enum AttackDirection {
        Left(Vector.build(-80, -20)),
        Right(Vector.build(60, -20)),
        Up(Vector.build(20, -40)),
        Down(Vector.build(20, 40));
        private final Vector relativePos;
        AttackDirection(Vector relativePos){
            this.relativePos = relativePos;
        }
        public Vector getRelativePos() {
            return relativePos;
        }
    }

    public Player(Vector worldPosition) {
        super(worldPosition,
                Vector.build(180, 135),
                Vector.build(232 - 137, 333 - 257),
                Vector.build(38, 57),
                10,
                100,
                300,
                500,
                1000,
                500,
                false,
                5,
                500,
                200,
                400,
                CharacterType.Player);
        initAnimation();
        this.registerHitCollisionZone(beHitZone);
        this.addCollider(new RectCollider(Vector.build(38, 55), this, Vector.build(92, 440 - 366)));
        Component.addComponent(this);
    }

    private void initAnimation() {
        Animation idle = new Animation(this, 100);
        idle.add_frame_by_name("player_idle", 5, false);
        this.addAnimation("idle_right", idle);

        Animation idle_left = new Animation(this, 100);
        idle_left.add_frame_by_name("player_idle_left", 5, true);
        this.addAnimation("idle_left", idle_left);

        PlayerIdleState idleState = new PlayerIdleState(this, idle_left, idle);
        this.stateMachine.register("idle_state", idleState);
        this.stateMachine.setCurrentState(idleState);

        Animation run = new Animation(this, 100);
        run.add_frame_by_name("player_run", 10, false);
        this.addAnimation("run_right", run);

        Animation run_left = new Animation(this, 100);
        run_left.add_frame_by_name("player_run_left", 10, true);
        this.addAnimation("run_left", run_left);

        PlayerRunState runState = new PlayerRunState(this, run_left, run);
        this.stateMachine.register("run_state", runState);

        Animation jump = new Animation(this, 100);
        jump.add_frame_by_name("player_jump", 5, false);
        jump.setIs_loop(false);
        this.addAnimation("jump_right", jump);

        Animation jump_left = new Animation(this, 100);
        jump_left.add_frame_by_name("player_jump_left", 5, true);
        jump_left.setIs_loop(false);
        this.addAnimation("jump_left", jump_left);

        PlayerJumpState jumpState = new PlayerJumpState(this, jump_left, jump);
        this.stateMachine.register("jump_state", jumpState);

        Animation fall = new Animation(this, 100);
        fall.add_frame_by_name("player_fall", 5, false);
        fall.setIs_loop(false);
        this.addAnimation("fall_right", fall);

        Animation fall_left = new Animation(this, 100);
        fall_left.add_frame_by_name("player_fall_left", 5, true);
        fall_left.setIs_loop(false);
        this.addAnimation("fall_left", fall_left);

        PlayerFallState fallState = new PlayerFallState(this, fall_left, fall);
        this.stateMachine.register("fall_state", fallState);

        Animation roll = new Animation(this, 100);
        roll.add_frame_by_name("player_roll", 7, false);
        roll.setIs_loop(false);
        this.addAnimation("roll_right", roll);


        Animation roll_left = new Animation(this, 100);
        roll_left.add_frame_by_name("player_roll_left", 7, true);
        roll_left.setIs_loop(false);
        this.addAnimation("roll_left", roll_left);

        PlayerRollState rollState = new PlayerRollState(this, roll_left, roll);
        this.stateMachine.register("roll_state", rollState);

        Animation attack = new Animation(this, 150);
        attack.add_frame_by_name("player_attack", 5, false);
        this.addAnimation("attack_right", attack);

        Animation attack_left = new Animation(this, 150);
        attack_left.add_frame_by_name("player_attack_left", 5, true);
        this.addAnimation("attack_left", attack_left);

        PlayerAttackState attackState = new PlayerAttackState(this, attack_left, attack);
        this.stateMachine.register("attack_state", attackState);

        Animation dead = new Animation(this, 250);
        dead.add_frame_by_name("player_dead", 6, false);
        dead.setIs_loop(false);
        this.addAnimation("dead_right", dead);

        Animation dead_left = new Animation(this, 250);
        dead_left.add_frame_by_name("player_dead_left", 6, true);
        dead_left.setIs_loop(false);
        this.addAnimation("dead_left", dead_left);

        PlayerDeadState deadState = new PlayerDeadState(this, dead_left, dead);
        this.stateMachine.register("dead_state", deadState);
        deadState.setNextStates(new StateNode[]{idleState, runState, jumpState, fallState, rollState, attackState});
        attackState.setNextStates(new StateNode[]{idleState, runState, jumpState, fallState, rollState, deadState});
        rollState.setNextStates(new StateNode[]{idleState, runState, jumpState, fallState, attackState, deadState});
        idleState.setNextStates(new StateNode[]{runState, jumpState, fallState, rollState, attackState, deadState});
        runState.setNextStates(new StateNode[]{idleState, jumpState, fallState, rollState, attackState, deadState});
        jumpState.setNextStates(new StateNode[]{idleState, runState, fallState, rollState, attackState, deadState});
        fallState.setNextStates(new StateNode[]{idleState, runState, jumpState, rollState, attackState, deadState});
    }

    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
    }

    @Override
    protected boolean tryAtk() {
        if (!this.whetherCanAtk) return false;
        // 攻击
        Component.addComponent(new PlayerBullet(this, (!whetherFacingLeft) ? AttackDirection.Right: AttackDirection.Left));
        return true;
    }

    @Override
    protected void onInvulnerableHit() {

    }

    @Override
    protected void onHit() {
        MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("player_hurt"));
    }

    @Override
    public void attackByBullet(Bullet bullet){
        if (!whetherInvulnerable) {
            lastBeAttackedBullet = bullet;
            lastBeAttackedTime = TimeEventManager.currentTime;
            this.currentHp -= bullet.getDamage();
            this.whetherInvulnerable = true;
            invulnerableStateTimer.restart();
            blinkTimer.restart();
            this.onHit();
            if (this.currentHp <= 0) {
                this.stateMachine.forceSwitch("dead_state");
            }
        } else {
            onInvulnerableHit();
        }
    }

    @Override
    public void processKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    this.whetherRunLeft = true;
                    break;
                case KeyEvent.VK_D:
                    this.whetherRunRight = true;
                    break;
                case KeyEvent.VK_W:
                    if (this.jumpCount < this.jumpMaxCount) {
                        this.velocity.y = -jumpSpeed;
                        this.whetherJumping = true;
                        this.whetherOnGround = false;
                        this.whetherDownGround = false;
                        this.cantOnThisGround = null;
                        this.jumpCount++;
                        this.stateMachine.forceSwitch("jump_state");
                        MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("player_jump"));
                    }
                    break;
                case KeyEvent.VK_J:
                    if (this.tryAtk()) {
                        MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("player_attack_1"));
                        this.whetherCanAtk = false;
                        this.stateMachine.forceSwitch("attack_state");
                        this.whetherAtking = true;
                        this.atkBackSwingTimer.restart();
                        this.atk_intervalTimer.restart();
                    }
                    break;
                case KeyEvent.VK_S:
                    if (this.whetherOnGround && this.getLastOnGround().isWhetherCanDown()){
                        this.whetherDownGround = true;
                        this.whetherOnGround = false;
                        cantOnThisGround = this.getLastOnGround();
                    }
                    break;
                case KeyEvent.VK_SHIFT:
                    if (this.whetherCanRoll && !this.isRolling) {
                        // 进行翻滚操作
                        this.onRoll();
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

    @Override
    protected void processFall(double fullY) {
        if (fullY > 500){
            MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("player_land"));
            this.currentHp -= (int) ((fullY / 1000) * this.getMass());
        }
    }

    @Override
    protected void onRoll() {
        this.isRolling = true;
        this.whetherCanRoll = false;
        this.rollWholeTimeTimer.restart();
        this.rollCdTimer.restart();
        this.velocity.x = this.whetherFacingLeft ? -this.rollSpeed: this.rollSpeed;
        this.beInvulnerable();
        MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("player_roll"));
    }

    @Override
    protected void showDebugInfo(Graphics g) {
        g.setColor(DebugSetting.DebugInfoColor);
        g.drawString("速度: " + this.velocity, this.getDrawPosition().getX(), this.getDrawPosition().getY() - 20);
        g.drawString("是否无敌: " + (this.whetherInvulnerable ? "是" : "否"), this.getDrawPosition().getX(), this.getDrawPosition().getY());
        g.drawString("是否正在翻滚: " + (this.isRolling ? "是" : "否"), this.getDrawPosition().getX(), this.getDrawPosition().getY() + 20);
        g.drawString("是否可以攻击: " + (this.whetherCanAtk ? "是" : "否"), this.getDrawPosition().getX(), this.getDrawPosition().getY() + 40);
        g.drawString("连跳次数: " + this.jumpMaxCount, this.getDrawPosition().getX(), this.getDrawPosition().getY() + 60);
        g.drawString("当前连跳次数: " + this.jumpCount, this.getDrawPosition().getX(), this.getDrawPosition().getY() + 80);
        g.drawString("当前生命值: " + this.currentHp + "/" + this.hp, this.getDrawPosition().getX(), this.getDrawPosition().getY() + 100);
    }
}
