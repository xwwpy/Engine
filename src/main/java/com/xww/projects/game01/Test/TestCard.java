package com.xww.projects.game01.Test;

import com.xww.Engine.Test.TestCircle;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game01.Card.BaseCard;

public class TestCard extends BaseCard {
    public TestCard(Vector pos) {
        super(pos, "/Applications/程序/项目文件/javaProject/GameEngine/src/main/java/com/xww/projects/game01/Res/SunFlower.png", 3000, 50, true);
    }

    @Override
    public boolean generateCharacter(Vector position) {
        TestCard card = new TestCard(position);
        card.addCollider(new RectCollider(Vector.Zero(), this, card.size));
        card.CollisionRegion = ++TestCircle.collisionRegion;
        Component.addComponent(card);
        return true;
    }

}
