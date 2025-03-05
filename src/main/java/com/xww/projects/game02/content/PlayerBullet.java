package com.xww.projects.game02.content;

import com.xww.Engine.core.Actor.Bullet;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;

import java.awt.*;

public class PlayerBullet extends Bullet {
    private final Animation animation;
    private static final Vector bSize = Vector.build((double) 1620 / 8, (double) 324 / 1.6);

    private final Vector relativePosition;

    public static final int each_frame_time = 65;
    public PlayerBullet(Component owner, Player.AttackDirection attackDirection) {
        super(owner,
                owner.getLeftTopWorldPosition().add(attackDirection.getRelativePos()),
                bSize,
                Vector.Zero(),
                Vector.Zero(),
                500,
                false,
                Player.atkZone,
                20);
        this.relativePosition = attackDirection.getRelativePos();
        switch(attackDirection){
            case Up:
                animation = new Animation(this, each_frame_time);
                animation.add_frame_by_name("player_vfx_attack_up", 5, bSize);
                break;
            case Down:
                animation = new Animation(this, each_frame_time);
                animation.add_frame_by_name("player_vfx_attack_down", 5, bSize);
                break;
            case Left:
                animation = new Animation(this, each_frame_time);
                this.addCollider(new RectCollider(Vector.build(8, 541 - 451), this, Vector.build(468 - 296, 498 - 446)));
                animation.add_frame_by_name("player_vfx_attack_left", 5, bSize);
                break;
            case Right:
                animation = new Animation(this, each_frame_time);
                animation.add_frame_by_name("player_vfx_attack_right", 5, bSize);
                break;
            default:
                throw new RuntimeException("unknown attack direction");
        }
        this.enableGravity = false;
        animation.setIs_loop(false);
        animation.setOn_complete((bullet)->{
            ((Bullet) bullet).crash();
        });
    }

    @Override
    public void on_update(Graphics g) {
        this.worldPosition = this.owner.getLeftTopWorldPosition().add(relativePosition);
        animation.on_update(g);
        super.on_update(g);
    }

    @Override
    public void crash() {
        this.isAlive = false;
    }
}
