package com.xww.projects.game02.content.Player.bullet;

import com.xww.Engine.core.Actor.Bullet;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game02.content.Player.Player;

import java.awt.*;

public class PlayerBullet extends Bullet {
    private final Animation animation;
    private static final Vector bSize = Vector.build((double) 1620 / 4, 324);

    private final Vector relativePosition;

    public static final int each_frame_time = 65;
    public PlayerBullet(Component owner, Player.AttackDirection attackDirection) {
        super(owner,
                owner.getLeftTopWorldPosition().add(attackDirection.getRelativePos()),
                bSize,
                Vector.Zero(),
                Vector.Zero(),
                65 * 5,
                true,
                Player.atkZone,
                5);
        this.relativePosition = attackDirection.getRelativePos();
        switch(attackDirection){
            case Up:
                animation = new Animation(this, each_frame_time);
                animation.add_frame_by_name("player_vfx_attack_up", 5, bSize);
                this.addCollider(new RectCollider(Vector.build(655 - 629, (double) 324 / 2), this, Vector.build((468 - 296) * 2, (498 - 446) * 1.6)));
                this.addCollider(new RectCollider(Vector.build(780 - 632, 20), this, Vector.build(863 - 778, (652 - 451) * 1.5)));
                break;
            case Down:
                animation = new Animation(this, each_frame_time);
                animation.add_frame_by_name("player_vfx_attack_down", 5, bSize);
                this.addCollider(new RectCollider(Vector.build(655 - 629, (double) 324 / 2), this, Vector.build((468 - 296) * 2, (498 - 446) * 1.6)));
                this.addCollider(new RectCollider(Vector.build(780 - 632, 20), this, Vector.build(863 - 778, (652 - 451) * 1.5)));
                break;
            case Left:
                animation = new Animation(this, each_frame_time);
                this.addCollider(new RectCollider(Vector.build(28, (541 - 451) * 1.6), this, Vector.build((468 - 296) * 2, (498 - 446) * 1.6)));
                animation.add_frame_by_name("player_vfx_attack_left", 5, bSize);
                break;
            case Right:
                animation = new Animation(this, each_frame_time);
                this.addCollider(new RectCollider(Vector.build(28, (541 - 451) * 1.6), this, Vector.build((468 - 296) * 2, (498 - 446) * 1.6)));
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
