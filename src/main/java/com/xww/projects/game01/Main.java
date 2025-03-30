package com.xww.projects.game01;

import com.xww.Engine.Test.TestCharacter;
import com.xww.Engine.Test.TestComponent;
import com.xww.Engine.Test.TestRelativeComponent;
import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Collision.CircleCollider;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Scene.GameScene;
import com.xww.Engine.core.Scene.SceneManager;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.FrameSetting;
import com.xww.projects.game01.Card.CardBar;
import com.xww.projects.game01.Chess.Chess;
import com.xww.projects.game01.Test.TestCard;

public class Main {
    public static void main(String[] args) {
        GameFrame.init(Main::initGame);
        GameFrame.start();
    }

    public static void initGame(){
        TestComponent component = new TestComponent(Vector.build(100, 100), Vector.build(50, 50), AnchorMode.LeftTop, Vector.build(100, 200), Vector.Zero(), 0, CollisionDefaultConstValue.noCollisionChecking, CollisionDefaultConstValue.noCollisionChecking);
        TestRelativeComponent child1 = new TestRelativeComponent(component, Vector.build(100, 100), AnchorMode.LeftTop, Vector.build(30, 0), Vector.Zero(),   Vector.build(50, 200), false, 1, 0b0001, 0b000);
        child1.addCollider(new RectCollider(Vector.build(0, 0), child1, child1.getSize()));
        component.addChild(child1);
        TestRelativeComponent child2 = new TestRelativeComponent(child1, Vector.build(50, 50), AnchorMode.LeftTop, Vector.build(10, 0), Vector.Zero(),  Vector.build(50, 200), false, 1, 0b0001, 0b000);
        child1.addChild(child2);
        child2.addCollider(new RectCollider(Vector.Zero(), child2, child2.getSize()));
        Component.addComponent(component);
        Component.addComponent(new TestComponent(Vector.build(200, 200), Vector.build(100, 100), AnchorMode.LeftTop, Vector.build(0, 50), Vector.Zero(), 0, 0b0001, 0b000));
        Component.addComponent(new TestComponent(Vector.build(300, 300), Vector.build(100, 100), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 0b0001, 0b000));
        Component.addComponent(new TestComponent(Vector.build(400, 400), Vector.build(100, 100), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0,0b0001, 0b000));
        Component.addComponent(new TestComponent(Vector.build(600, 200), Vector.build(200, 100), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 0b0001, 0b000));
        Component.addComponent(new TestComponent(Vector.build(200, 600), Vector.build(100, 100), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 0b0001, 0b000));

        TestComponent testComponent = new TestComponent(Vector.build(600, 550), Vector.build(100, 150), AnchorMode.RightTop, Vector.build(0, 0), Vector.Zero(), 0, 0b0001, 0b000);
        testComponent.addCollider(new CircleCollider(Vector.build(-100, 0), testComponent, 30));
        testComponent.addCollider(new CircleCollider(Vector.build(200, 0), testComponent, 40));
        Component.addComponent(testComponent);
        testComponent.setMass(10000);

        TestRelativeComponent testRelativeComponent = new TestRelativeComponent(testComponent, Vector.build(50, 60), AnchorMode.LeftTop, Vector.build(500, 0), Vector.build(50, 20), Vector.build(300, 0), true, 0, 0b0001, 0b000);
        testComponent.addChild(testRelativeComponent);
        testRelativeComponent.addCollider(new RectCollider(Vector.build(0, 0), testRelativeComponent, testRelativeComponent.getSize()));
        TestRelativeComponent testRelativeComponent1 = new TestRelativeComponent(testRelativeComponent, Vector.build(30, 30), AnchorMode.LeftTop, Vector.build(20, 0), Vector.build(0, 0), Vector.build(100, 0), true, 0, 0b0001, 0b000);
        testRelativeComponent1.addCollider(new CircleCollider(Vector.Zero(), testRelativeComponent1, 15));
        testRelativeComponent.addChild(testRelativeComponent1);

        TestComponent testComponent1 = new TestComponent(Vector.build(1100, 600), Vector.build(100, 100), AnchorMode.LeftBottom, Vector.build(100, 20), Vector.Zero(), 0, 0b0001, 0b000);

        Component.addComponent(testComponent1);
        TestComponent boundaryLeft = new TestComponent(Vector.build(-10, 0), Vector.build(10, FrameSetting.DEFAULT_HEIGHT), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 0, CollisionDefaultConstValue.noCollisionChecking);
        boundaryLeft.addCollider(new RectCollider(Vector.build(0, 0), boundaryLeft, boundaryLeft.getSize()));
        Component.addComponent(boundaryLeft);
        boundaryLeft.setMass(-1);
        TestComponent boundaryRight = new TestComponent(Vector.build(FrameSetting.DEFAULT_WIDTH + 10, 0), Vector.build(10, FrameSetting.DEFAULT_HEIGHT), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0,0, CollisionDefaultConstValue.noCollisionChecking);
        boundaryRight.addCollider(new RectCollider(Vector.build(0, 0), boundaryRight, boundaryRight.getSize()));
        Component.addComponent(boundaryRight);
        boundaryRight.setMass(-1);
        TestComponent boundaryTop = new TestComponent(Vector.build(0, -10), Vector.build(FrameSetting.DEFAULT_WIDTH, 10), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 0, CollisionDefaultConstValue.noCollisionChecking);
        boundaryTop.addCollider(new RectCollider(Vector.build(0, 0), boundaryTop, boundaryTop.getSize()));
        Component.addComponent(boundaryTop);
        boundaryTop.setMass(-1);
        TestComponent boundaryBottom = new TestComponent(Vector.build(0, FrameSetting.DEFAULT_HEIGHT + 10), Vector.build(FrameSetting.DEFAULT_WIDTH, 10), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 0, CollisionDefaultConstValue.noCollisionChecking);
        boundaryBottom.addCollider(new RectCollider(Vector.build(0, 0), boundaryBottom, boundaryBottom.getSize()));
        Component.addComponent(boundaryBottom);
        boundaryBottom.setMass(-1);
        TestCharacter testCharacter = new TestCharacter(Vector.build(300, 300), Vector.build(100, 100));
        Component.addComponent(testCharacter);
        Component.addComponent(new TestCard(Vector.build(100, 400)));
        Component.addComponent(Chess.createChess(5, 9, Vector.build((int) (747 * 2.3), (int) (320 * 2.3)), Vector.build(100, 125), Vector.build(300, 88), "/Applications/程序/项目文件/javaProject/project01/src/game/game07/Resources/res/images/bg5.jpg"));
        Component.addComponent(new CardBar(5), false);
        SceneManager.sceneManagerIns.addScene("game", new GameScene());
        SceneManager.sceneManagerIns.setCurrentScene("game");
    }
}
