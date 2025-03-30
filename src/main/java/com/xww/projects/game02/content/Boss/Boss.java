package com.xww.projects.game02.content.Boss;

import com.xww.Engine.core.Actor.Bullet;
import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Barrier.BaseGround;
import com.xww.Engine.core.Barrier.BaseWall;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Event.TimeEventManager;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.setting.DebugSetting;
import com.xww.projects.game02.content.Boss.BossBullet.Barb;
import com.xww.projects.game02.content.Boss.BossBullet.Silk;
import com.xww.projects.game02.content.Boss.BossBullet.Sword;
import com.xww.projects.game02.content.Boss.States.*;
import com.xww.projects.game02.content.Player.Player;

import java.awt.*;

public class Boss extends Character {
    public static final int beHitZone = 0b1000;
    public static final int atkZone = 0b10000;

    private final Player player;

    public static int bossHitDamage = 2;

    public Boss(Vector worldPosition, Player player) {
        super(worldPosition,
                Vector.build(150, 150),
                Vector.build(80, 150),
                Vector.build(35, 0),
                20,
                100,
                200,
                500,
                Integer.MAX_VALUE,
                0,
                true,
                1,
                500,
                300,
                100,
                CharacterType.Enemy);
        this.registerHitCollisionZone(beHitZone);
        this.registerActiveCollisionZone(atkZone);
        this.callBack = ActionAfterCollision.ActionAfterCollisionType.nullAction;
        this.addCollider(new RectCollider(this.relativePosition, this, logicSize));
        this.whetherCanRoll = false;
        this.player = player;
        this.stateMachine = new BossStateMachine(this);
        initAnimation();
        this.stateMachine.forceSwitch("idleState");
        Component.addComponent(this);
    }

    @Override
    protected void checkMove() {
        update_direction();
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
    private void initAnimation() {
    // idle
        Animation idle_left = new Animation(this, 100);
        idle_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_idle"));
        this.addAnimation("idle_left", idle_left);

        Animation idle_right = new Animation(this, 100);
        idle_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_idle_right"));
        this.addAnimation("idle_right", idle_right);
        BossIdleState idleState = new BossIdleState(this, idle_left, idle_right);
        this.stateMachine.register("idleState", idleState);

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
        this.stateMachine.register("jumpState", jumpState);


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
        this.stateMachine.register("fallState", fallState);


     // run
        Animation run_left = new Animation(this, 100);
        run_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_run"));
        this.addAnimation("run_left", run_left);

        Animation run_right = new Animation(this, 100);
        run_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_run_right"));
        this.addAnimation("run_right", run_right);

        BossRunState runState = new BossRunState(this, run_left, run_right);
        this.stateMachine.register("runState", runState);

     // throw_barb
        Animation throw_bard_left = new Animation(this, 200);
        throw_bard_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_throw_barb"));
        this.addAnimation("throw_bard_left", throw_bard_left);

        Animation throw_bard_right = new Animation(this, 200);
        throw_bard_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_throw_barb_right"));
        this.addAnimation("throw_bard_right", throw_bard_right);

        BossThrowBarbState throwBarbState = new BossThrowBarbState(this, throw_bard_left, throw_bard_right);
        this.stateMachine.register("throwBarbState", throwBarbState);


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
        this.stateMachine.register("throwSilkState", throwSilkState);


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
        this.stateMachine.register("throwSwordState", throwSwordState);


     // dash_in_air
        Animation dash_in_air_left = new Animation(this, 100);
        dash_in_air_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_dash_in_air"));
        this.addAnimation("dash_in_air_left", dash_in_air_left);

        Animation dash_in_air_right = new Animation(this, 100);
        dash_in_air_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_dash_in_air_right"));
        this.addAnimation("dash_in_air_right", dash_in_air_right);

        BossDashInAirState dashInAirState = new BossDashInAirState(this, dash_in_air_left, dash_in_air_right);
        this.stateMachine.register("dashInAirState", dashInAirState);


     // dash_on_floor
        Animation dash_on_floor_left = new Animation(this, 100);
        dash_on_floor_left.add_frame(ResourceManager.getInstance().findAtlas("enemy_dash_on_floor"));
        this.addAnimation("dash_on_floor_left", dash_on_floor_left);

        Animation dash_on_floor_right = new Animation(this, 100);
        dash_on_floor_right.add_frame(ResourceManager.getInstance().findAtlas("enemy_dash_on_floor_right"));
        this.addAnimation("dash_on_floor_right", dash_on_floor_right);

        BossDashOnFloorState dashOnFloorState = new BossDashOnFloorState(this, dash_on_floor_left, dash_on_floor_right);
        this.stateMachine.register("dashOnFloorState", dashOnFloorState);

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
        this.stateMachine.register("aimState", aimState);

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
        this.stateMachine.register("squatState", squatState);

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
        this.stateMachine.register("deadState", deadState);
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
    protected void update_direction() {

    }

    public void updateBossDirection(){
        this.whetherFacingLeft = player.getLeftTopWorldPosition().getFullX() < this.getLeftTopWorldPosition().getFullX();
    }



    @Override
    public void on_update(Graphics g) {
        super.on_update(g);
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
                this.stateMachine.forceSwitch("deadState");
            }
        } else {
            onInvulnerableHit();
        }
    }


    @Override
    protected void showDebugInfo(Graphics g) {
        g.setColor(DebugSetting.DebugInfoColor);
        g.drawString("速度: " + this.velocity, this.getDrawPosition().getX(), this.getDrawPosition().getY() - 20);
        g.drawString("是否无敌: " + (this.whetherInvulnerable ? "是" : "否"), this.getDrawPosition().getX(), this.getDrawPosition().getY());
        g.drawString("是否在地面: " + (this.whetherOnGround ? "是" : "否"), this.getDrawPosition().getX(), this.getDrawPosition().getY() + 20);
        g.drawString("当前生命值: " + this.currentHp + "/" + this.hp, this.getDrawPosition().getX(), this.getDrawPosition().getY() + 40);
        g.drawString("当前动作: " + this.stateMachine.getCurrentStateId(), this.getDrawPosition().getX(), this.getDrawPosition().getY() + 60);
        g.drawString("当前速度: " + this.velocity, this.getDrawPosition().getX(), this.getDrawPosition().getY() + 80);
    }

    public Player getPlayer() {
        return player;
    }

    // 创建一个Silk对象
    public Silk silk() {
        return new Silk(this, this.getLeftTopWorldPosition());
    }

    public void sword() {
        new Sword(this, this.getLeftTopWorldPosition());
    }

    public void barb() {
        Barb.generateBarbs((200 - currentHp) / 15, this);
    }
}
