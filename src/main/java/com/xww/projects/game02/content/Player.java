package com.xww.projects.game02.content;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.ActionAfterCollision;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.ResourceManager.ResourceManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Player extends Character {


    public Player(Vector worldPosition) {
        super(worldPosition,
                Vector.build(180, 135),
                10,
                100,
                200,
                1000,
                false,
                40,
                500,
                200,
                CharacterType.Player,
                true);
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

    @Override
    protected boolean tryAtk() {
        return false;
    }

    @Override
    protected void onInvulnerableHit() {

    }

    @Override
    protected void onHit() {

    }
}
