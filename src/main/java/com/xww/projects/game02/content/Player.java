package com.xww.projects.game02.content;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Player extends FreeComponent {

    protected Map<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation;

    public Player(Vector worldPosition) {
        super(worldPosition, GameFrame.PositionType.World, Vector.build(180, 135), AnchorMode.CenterBottom, Vector.Zero(), Vector.Zero(), 0, CollisionDefaultConstValue.noCollisionChecking, CollisionDefaultConstValue.noCollisionChecking, -1, true, true);
        initAnimation();
    }

    private void initAnimation() {
        Animation idle = new Animation(this, 100);
        idle.add_frame_by_name("player_idle", 5, false);
        this.addAnimation("idle_right", idle);

        Animation idle_left = new Animation(this, 100);
        idle_left.add_frame_by_name("player_idle_left", 5, true);
        this.addAnimation("idle_left", idle_left);

        Animation dead = new Animation(this, 250);
        dead.add_frame_by_name("player_dead", 6, false);
        this.addAnimation("dead_right", dead);

        Animation dead_left = new Animation(this, 250);
        dead_left.add_frame_by_name("player_dead_left", 6, true);
        this.addAnimation("dead_left", dead_left);

        Animation attack = new Animation(this, 100);
        attack.add_frame_by_name("player_attack", 5, false);
        this.addAnimation("attack_right", attack);

        Animation attack_left = new Animation(this, 100);
        attack_left.add_frame_by_name("player_attack_left", 5, true);
        this.addAnimation("attack_left", attack_left);

        Animation fall = new Animation(this, 100);
        fall.add_frame_by_name("player_fall", 5, false);
        fall.setIs_loop(false);
        this.addAnimation("fall_right", fall);

        Animation fall_left = new Animation(this, 100);
        fall_left.add_frame_by_name("player_fall_left", 5, true);
        fall_left.setIs_loop(false);
        this.addAnimation("fall_left", fall_left);

        Animation jump = new Animation(this, 100);
        jump.add_frame_by_name("player_jump", 5, false);
        jump.setIs_loop(false);
        this.addAnimation("jump_right", jump);

        Animation jump_left = new Animation(this, 100);
        jump_left.add_frame_by_name("player_jump_left", 5, true);
        jump_left.setIs_loop(false);
        this.addAnimation("jump_left", jump_left);

        Animation roll = new Animation(this, 100);
        roll.add_frame_by_name("player_roll", 7, false);
        this.addAnimation("roll_right", roll);


        Animation roll_left = new Animation(this, 100);
        roll_left.add_frame_by_name("player_roll_left", 7, true);
        this.addAnimation("roll_left", roll_left);

        Animation run = new Animation(this, 100);
        run.add_frame_by_name("player_run", 10, false);
        this.addAnimation("run_right", run);

        Animation run_left = new Animation(this, 100);
        run_left.add_frame_by_name("player_run_left", 10, true);
        this.addAnimation("run_left", run_left);

        this.setAnimation("run_left");
        jump.setOn_complete((player)->{
            fall.reset_animation();
            ((Player) player).setAnimation("fall_right");
        });
        fall.setOn_complete((player)->{
            jump.reset_animation();
            ((Player) player).setAnimation("jump_right");
        });

        jump_left.setOn_complete((player)->{
            fall_left.reset_animation();
            ((Player) player).setAnimation("fall_left");
        });
        fall_left.setOn_complete((player)->{
            jump_left.reset_animation();
            ((Player) player).setAnimation("jump_left");
        });


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
        super.on_update(g);
        // 选择动画
        if (currentAnimation != null) {
            currentAnimation.on_update(g);
        } else {
            System.out.println("currentAnimation is null");
        }
    }
}
