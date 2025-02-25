package com.xww.Engine.Test;

import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Animation.Atlas;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.core.Actor.Character;

public class TestCharacter extends Character{
    public TestCharacter(Vector worldPosition, Vector size) {
        super(worldPosition,
                size,
                size,
                Vector.Zero(),
                10,
                100,
                100,
                1000,
                false,
                1,
                500,
                100,
                CharacterType.Player);
        initIdleAnimation();
        this.addCollider(new RectCollider(Vector.Zero(), this, size));
    }


    private void initIdleAnimation() {
        this.currentAnimation = new Animation(this, 100);
        this.animations.put("idle", this.currentAnimation);
        Atlas atlas = new Atlas();
        atlas.load("/Applications/程序/项目文件/javaProject/project01/src/game/game07/Resources/enemy/throw_sword/%d.png", 16, 1);
        this.currentAnimation.add_frame(atlas);
    }

    @Override
    protected boolean tryAtk() {
        System.out.println("try atk");
        return false;
    }

    @Override
    protected void onInvulnerableHit() {
        System.out.println("invulnerable hit");
    }

    @Override
    protected void onHit() {
        System.out.println("hit");
    }
}
