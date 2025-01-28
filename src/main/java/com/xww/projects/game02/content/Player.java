package com.xww.projects.game02.content;

import com.xww.Engine.Utils.ImgUtils;
import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Animation.Animation;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Player extends FreeComponent {
    protected Map<String, Animation> animations = new HashMap<>();
    protected Animation currentAnimation;

    public Player(Vector worldPosition) {
        super(worldPosition, GameFrame.PositionType.World, Vector.build(180, 135), AnchorMode.CenterBottom, Vector.Zero(), Vector.Zero(), 0, -1, -1, true, true);
        initAnimation();
    }

    private void initAnimation() {
        Animation idle = new Animation(this, 100);
        idle.add_frame(ImgUtils.loadImage("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/player/idle.png"), 5);
        this.addAnimation("idle", idle);

        Animation dead = new Animation(this, 100);
        dead.add_frame(ImgUtils.loadImage("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/player/dead.png"), 6);
        this.addAnimation("dead", dead);

        Animation attack = new Animation(this, 100);
        attack.add_frame(ImgUtils.loadImage("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/player/attack.png"), 5);
        this.addAnimation("attack", attack);

        Animation fall = new Animation(this, 100);
        fall.add_frame(ImgUtils.loadImage("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/player/fall.png"), 5);
        this.addAnimation("fall", fall);

        Animation jump = new Animation(this, 100);
        jump.add_frame(ImgUtils.loadImage("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/player/jump.png"), 5);
        this.addAnimation("jump", jump);

        Animation roll = new Animation(this, 100);
        roll.add_frame(ImgUtils.loadImage("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/player/roll.png"), 7);
        this.addAnimation("roll", roll);

        Animation run = new Animation(this, 100);
        run.add_frame(ImgUtils.loadImage("/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game02/Resources/player/run.png"), 10);
        this.addAnimation("run", run);

        this.setAnimation("run");
        jump.setOn_complete((player)->{
            fall.reset_animation();
            ((Player) player).setAnimation("fall");
        });
        fall.setOn_complete((player)->{
            jump.reset_animation();
            ((Player) player).setAnimation("jump");
        });

    }

    public void addAnimation(String name, Animation animation) {
        animations.put(name, animation);
    }

    public void setAnimation(String name) {
        Animation animation = animations.get(name);
        if (animation == null){
            System.out.println("animation" + name + " is null");
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
