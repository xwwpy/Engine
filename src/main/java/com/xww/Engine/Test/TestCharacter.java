package com.xww.Engine.Test;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Animation.Atlas;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Vector.Vector;

public class TestCharacter extends Character{
    public TestCharacter(Vector worldPosition, Vector size) {
        super(worldPosition, size, Vector.build(100, 200), Vector.Zero(), Integer.MAX_VALUE, 1000, 10, true, true);
        initIdleAnimation(size);
        this.addCollider(new RectCollider(Vector.Zero(), this, size));
    }

    @Override
    public void selectCurrentAnimation() {
        this.currentAnimation = animations.get("idle");
    }
    private void initIdleAnimation(Vector size) {
        this.currentAnimation = new Animation(this, 100);
        this.animations.put("idle", this.currentAnimation);
        Atlas atlas = new Atlas();
        atlas.load("/Applications/程序/项目文件/javaProject/project01/src/game/game07/Resources/enemy/throw_sword/%d.png", 16, 1);
        this.currentAnimation.add_frame(atlas, size);
    }

}
