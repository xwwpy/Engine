package com.xww.projects.game02.content.Boss.BossBullet;

import com.xww.Engine.Utils.CodeUtils;
import com.xww.Engine.core.Actor.Bullet;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Sound.MP3Player;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Timer.TimerManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.setting.FrameSetting;
import com.xww.projects.game02.content.Boss.Boss;
import com.xww.projects.game02.content.Player.Player;
import com.xww.projects.game02.content.Player.PlayerBullet;

import java.awt.*;

public class Barb extends Bullet {

    private Timer idleTimer;

    private Timer dashTimer;

    private Timer breakTimer;


    private final Player player;
    private Animation currentAnimation;
    private Animation looseLeftAnimation;
    private Animation looseRightAnimation;

    private Animation breakLeftAnimation;
    private Animation breakRightAnimation;

    private boolean whetherFacingLeft;
    public Barb(Component owner, Vector worldPosition) {
        super(owner,
                worldPosition,
                Vector.build(40, 40),
                Vector.Zero(),
                Vector.Zero(),
                20,
                false,
                Boss.atkZone,
                10);
        this.registerHitCollisionZone(Player.atkZone);
        this.player = ((Boss) owner).getPlayer();

        this.whetherFacingLeft = CodeUtils.getRandomNum(0, 1) == 0;
        initAnimation();
        this.idleTimer = new Timer(3000, (obj) -> {
            Barb obj1 = (Barb) obj;
            this.velocity = obj1.getLeftTopWorldPosition().add(obj1.size.divide(2)).sub(player.getLeftTopWorldPosition().add(player.getSize().divide(2))).divide(-2);
            this.dashTimer.restart();
        }, this);

        this.dashTimer = new Timer(2000, (obj) -> {
            this.velocity = Vector.Zero();
            this.currentAnimation = whetherFacingLeft ? breakLeftAnimation : breakRightAnimation;
            this.damage = 0;
            this.breakTimer.restart();
            MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("barb_break"));
        }, this);

        this.breakTimer = new Timer(600, (obj) -> {
            this.crash();
        }, this);
        this.dashTimer.stopStart();
        this.breakTimer.stopStart();
        this.addTimer(this.idleTimer);
        this.addTimer(this.dashTimer);
        this.addTimer(this.breakTimer);
        this.addCollider(new RectCollider(Vector.Zero(), this, this.getSize()));
        Component.addComponent(this);
    }

    private void initAnimation() {
        this.looseLeftAnimation = new Animation(this, 200);
        this.looseLeftAnimation.add_frame(ResourceManager.getInstance().findAtlas("enemy_barb_loose"));

        this.looseRightAnimation = new Animation(this, 200);
        this.looseRightAnimation.add_frame(ResourceManager.getInstance().findAtlas("enemy_barb_loose_right"));

        this.breakLeftAnimation = new Animation(this, 200);
        this.breakLeftAnimation.add_frame(ResourceManager.getInstance().findAtlas("enemy_barb_break"));
        this.breakLeftAnimation.setIs_loop(false);
        this.breakRightAnimation = new Animation(this, 200);
        this.breakRightAnimation.add_frame(ResourceManager.getInstance().findAtlas("enemy_barb_break_right"));
        this.breakRightAnimation.setIs_loop(false);

        this.currentAnimation = whetherFacingLeft ? looseLeftAnimation : looseRightAnimation;
    }

    @Override
    public void crash() {
        this.setAlive(false);
    }

    @Override
    public void on_update(Graphics g) {
        currentAnimation.on_update(g);
        super.on_update(g);
    }

    @Override
    public void receiveCollision(ActionAfterCollision.CollisionInfo collisionInfo) {
        if (collisionInfo.getOtherCollider().getOwner() instanceof PlayerBullet){
            this.currentAnimation = whetherFacingLeft ? breakLeftAnimation : breakRightAnimation;
            MP3Player.getInstance().addAudio(ResourceManager.getInstance().findAudioPath("barb_break"));
            this.damage = 0;
            this.idleTimer.stopStart();
            this.dashTimer.stopStart();
            this.breakTimer.restart();
        }
    }

    public static void generateBarbs(int num, Boss boss){
        // 根据throwBarbAnimation的时间内生成足够的barbs
        Timer generateBarbs = new Timer((double) 1600 / num, (obj) -> {
            int x = CodeUtils.getRandomNum(0, FrameSetting.DEFAULT_WIDTH - 50);
            int y = CodeUtils.getRandomNum(0, FrameSetting.DEFAULT_HEIGHT - 400);
            new Barb(boss, Vector.build(x, y));
        }, boss);
        generateBarbs.setRun_times(num);
        TimerManager.instance.registerTimer(generateBarbs);
    }
}
