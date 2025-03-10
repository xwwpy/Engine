package com.xww.projects.game02.content.Boss;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.StateManager.StateNode;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.setting.DebugSetting;
import com.xww.projects.game02.content.Boss.States.*;
import com.xww.projects.game02.content.Player.PlayerJumpComponent;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Boss extends Character {
    public static final int beHitZone = 0b1000;
    public static final int atkZone = 0b10000;

    private boolean whetherThrowingBarb = false; // 是否正在发射弹道
    private boolean whetherThrowingSilk = false; // 是否正在发射 silk
    private boolean whetherThrowingSword = false; // 是否正在发射 sword

    private boolean whetherDashingOnFloor = false; // 是否正在地上dash

    private boolean whetherDashingInAir = false; // 是否在空中dash

    private boolean whetherAiming = false; // 是否在瞄准

    public Boss(Vector worldPosition) {
        super(worldPosition,
                Vector.build(150, 150),
                Vector.build(150, 150),
                Vector.build(0, 0),
                20,
                100,
                200,
                500,
                Integer.MAX_VALUE,
                0,
                true,
                1,
                700,
                300,
                100,
                CharacterType.Player);
        initAnimation();
        this.registerHitCollisionZone(beHitZone);
        this.registerActiveCollisionZone(atkZone);
        this.callBack = ActionAfterCollision.ActionAfterCollisionType.nullAction;
        this.addCollider(new RectCollider(this.relativePosition, this, size));
        this.whetherCanRoll = false;
        Component.addComponent(this);
    }

    private void initAnimation() {
    // idle
        Animation idle_left = new Animation(this, 100);
        idle_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_idle"));
        this.addAnimation("idle_left", idle_left);

        Animation idle_right = new Animation(this, 100);
        idle_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_idle_right"));
        this.addAnimation("idle_right", idle_right);
        BossIdleState idleState = new BossIdleState(this, idle_left, idle_right);
        this.stateMachine.register("idle_state", idleState);
        this.stateMachine.forceSwitch("idle_state");

    // jump
        Animation jump_left = new Animation(this, 100);
        jump_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_jump"));
        jump_left.setIs_loop(false);
        this.addAnimation("jump_left", jump_left);

        Animation jump_right = new Animation(this, 100);
        jump_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_jump_right"));
        jump_right.setIs_loop(false);
        this.addAnimation("jump_right", jump_right);

        BossJumpState jumpState = new BossJumpState(this, jump_left, jump_right);
        this.stateMachine.register("jump_state", jumpState);


     // fall
        Animation fall_left = new Animation(this, 100);
        fall_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_fall"));
        fall_left.setIs_loop(false);
        this.addAnimation("fall_left", fall_left);

        Animation fall_right = new Animation(this, 100);
        fall_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_fall_right"));
        fall_right.setIs_loop(false);
        this.addAnimation("fall_right", fall_right);

        BossFallState fallState = new BossFallState(this, fall_left, fall_right);
        this.stateMachine.register("fall_state", fallState);


     // run
        Animation run_left = new Animation(this, 100);
        run_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_run"));
        this.addAnimation("run_left", run_left);

        Animation run_right = new Animation(this, 100);
        run_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_run_right"));
        this.addAnimation("run_right", run_right);

        BossRunState runState = new BossRunState(this, run_left, run_right);
        this.stateMachine.register("run_state", runState);

     // throw_barb
        Animation throw_bard_left = new Animation(this, 100);
        throw_bard_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_throw_barb"));
        throw_bard_left.setIs_loop(false);
        this.addAnimation("throw_bard_left", throw_bard_left);

        Animation throw_bard_right = new Animation(this, 100);
        throw_bard_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_throw_barb_right"));
        throw_bard_right.setIs_loop(false);
        this.addAnimation("throw_bard_right", throw_bard_right);

        BossThrowBarbState throwBarbState = new BossThrowBarbState(this, throw_bard_left, throw_bard_right);
        this.stateMachine.register("throw_barb_state", throwBarbState);


     // throw_silk
        Animation throw_silk_left = new Animation(this, 100);
        throw_silk_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_throw_silk"));
        throw_silk_left.setIs_loop(false);
        this.addAnimation("throw_silk_left", throw_silk_left);

        Animation throw_silk_right = new Animation(this, 100);
        throw_silk_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_throw_silk_right"));
        throw_silk_right.setIs_loop(false);
        this.addAnimation("throw_silk_right", throw_silk_right);

        BossThrowSilkState throwSilkState = new BossThrowSilkState(this, throw_silk_left, throw_silk_right);
        this.stateMachine.register("throw_silk_state", throwSilkState);


     // throw_sword
        Animation throw_sword_left = new Animation(this, 100);
        throw_sword_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_throw_sword"));
        throw_sword_left.setIs_loop(false);
        this.addAnimation("throw_sword_left", throw_sword_left);

        Animation throw_sword_right = new Animation(this, 100);
        throw_sword_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_throw_sword_right"));
        throw_sword_right.setIs_loop(false);
        this.addAnimation("throw_sword_right", throw_sword_right);

        BossThrowSwordState throwSwordState = new BossThrowSwordState(this, throw_sword_left, throw_sword_right);
        this.stateMachine.register("throw_sword_state", throwSwordState);


     // dash_in_air
        Animation dash_in_air_left = new Animation(this, 100);
        dash_in_air_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_dash_in_air"));
        this.addAnimation("dash_in_air_left", dash_in_air_left);

        Animation dash_in_air_right = new Animation(this, 100);
        dash_in_air_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_dash_in_air_right"));
        this.addAnimation("dash_in_air_right", dash_in_air_right);

        BossDashInAirState dashInAirState = new BossDashInAirState(this, dash_in_air_left, dash_in_air_right);
        this.stateMachine.register("dash_in_air_state", dashInAirState);


     // dash_on_floor
        Animation dash_on_floor_left = new Animation(this, 100);
        dash_on_floor_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_dash_on_floor"));
        this.addAnimation("dash_on_floor_left", dash_on_floor_left);

        Animation dash_on_floor_right = new Animation(this, 100);
        dash_on_floor_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_dash_on_floor_right"));
        this.addAnimation("dash_on_floor_right", dash_on_floor_right);

        BossDashOnFloorState dashOnFloorState = new BossDashOnFloorState(this, dash_on_floor_left, dash_on_floor_right);
        this.stateMachine.register("dash_on_floor_state", dashOnFloorState);

        // aim
        Animation aim_left = new Animation(this, 100);
        aim_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_aim"));
        aim_left.setIs_loop(false);
        this.addAnimation("aim_left", aim_left);

        Animation aim_right = new Animation(this, 100);
        aim_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_aim_right"));
        aim_right.setIs_loop(false);
        this.addAnimation("aim_right", aim_right);

        BossAimState aimState = new BossAimState(this, aim_left, aim_right);
        this.stateMachine.register("aim_state", aimState);

        // squat
        Animation squat_left = new Animation(this, 100);
        squat_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_squat"));
        squat_left.setIs_loop(false);
        this.addAnimation("squat_left", squat_left);

        Animation squat_right = new Animation(this, 100);
        squat_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_squat_right"));
        squat_right.setIs_loop(false);
        this.addAnimation("squat_right", squat_right);

        BossSquatState squatState = new BossSquatState(this, squat_left, squat_right);
        this.stateMachine.register("squat_state", squatState);

        // dead TODO
        Animation dead_left = new Animation(this, 100);
        dead_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_aim"));
        dead_left.setIs_loop(false);
        this.addAnimation("dead_left", dead_left);

        Animation dead_right = new Animation(this, 100);
        dead_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_aim_right"));
        dead_right.setIs_loop(false);
        this.addAnimation("dead_right", dead_right);

        BossDeadState deadState = new BossDeadState(this, dead_left, dead_right);
        this.stateMachine.register("dead_state", deadState);

        aimState.setNextStates(new StateNode[]{dashInAirState, deadState});
        dashInAirState.setNextStates(new StateNode[]{idleState, deadState, fallState});
        dashOnFloorState.setNextStates(new StateNode[]{idleState, deadState, fallState});
        fallState.setNextStates(new StateNode[]{idleState, deadState});
        idleState.setNextStates(new StateNode[]{deadState, fallState, runState, jumpState, squatState, throwBarbState, throwSilkState, throwSwordState});
        jumpState.setNextStates(new StateNode[]{deadState, fallState, aimState, throwSilkState});
        runState.setNextStates(new StateNode[]{deadState, idleState, throwSilkState});
        squatState.setNextStates(new StateNode[]{deadState, dashOnFloorState});
        throwBarbState.setNextStates(new StateNode[]{deadState, idleState});
        throwSilkState.setNextStates(new StateNode[]{deadState, idleState, aimState, fallState});
        throwSwordState.setNextStates(new StateNode[]{deadState, idleState, squatState, jumpState, throwSilkState});
    }

    @Override
    protected boolean tryAtk() {
        return false;
    }

    @Override
    protected void onInvulnerableHit() {

    }

    @Override
    protected void onHit() {
        // 获取1 - 3之间的随机数
        int random = (int) (Math.random() * 3) + 1;
        MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("enemy_hurt_" + random));
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
                        if (this.whetherOnGround) {
                            Vector pos = Vector.build(this.worldPosition.x, this.worldPosition.y).add_to_self(Vector.build(size.getFullX() / 6, size.getFullY() / 4));
                            PlayerJumpComponent jumpComponent = new PlayerJumpComponent(pos);
                            Component.addComponent(jumpComponent);
                        }
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
                    if (this.whetherOnGround && this.getLastOnGround().isWhetherCanDown()) {
                        this.whetherDownGround = true;
                        this.whetherOnGround = false;
                        cantOnThisGround = this.getLastOnGround();
                    }
                    break;
                case KeyEvent.VK_SHIFT:
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


    public boolean isWhetherThrowingBarb() {
        return whetherThrowingBarb;
    }

    public void setWhetherThrowingBarb(boolean whetherThrowingBarb) {
        this.whetherThrowingBarb = whetherThrowingBarb;
    }

    public boolean isWhetherThrowingSilk() {
        return whetherThrowingSilk;
    }

    public void setWhetherThrowingSilk(boolean whetherThrowingSilk) {
        this.whetherThrowingSilk = whetherThrowingSilk;
    }

    public boolean isWhetherThrowingSword() {
        return whetherThrowingSword;
    }

    public void setWhetherThrowingSword(boolean whetherThrowingSword) {
        this.whetherThrowingSword = whetherThrowingSword;
    }

    public boolean isWhetherDashingOnFloor() {
        return whetherDashingOnFloor;
    }

    public void setWhetherDashingOnFloor(boolean whetherDashingOnFloor) {
        this.whetherDashingOnFloor = whetherDashingOnFloor;
    }

    public boolean isWhetherDashingInAir() {
        return whetherDashingInAir;
    }

    public void setWhetherDashingInAir(boolean whetherDashingInAir) {
        this.whetherDashingInAir = whetherDashingInAir;
    }

    public boolean isWhetherAiming() {
        return whetherAiming;
    }

    public void setWhetherAiming(boolean whetherAiming) {
        this.whetherAiming = whetherAiming;
    }
}
