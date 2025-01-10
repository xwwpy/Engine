package com.xww.projects.game01;

import com.xww.Engine.Test.TestCharacter;
import com.xww.Engine.Test.TestComponent;
import com.xww.Engine.Test.TestRelativeComponent;
import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Collision.CircleCollider;
import com.xww.Engine.core.Collision.RectCollider;
import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Component.impl.CameraComponent;
import com.xww.Engine.core.Component.impl.CursorComponent;
import com.xww.Engine.core.Component.impl.FpsComponent;
import com.xww.Engine.core.Component.impl.TimeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.FrameSetting;

public class Main {
    public static void main(String[] args) {
        GameFrame.init();
        initGame();
        GameFrame.start();
    }

    public static void initGame(){
        TestComponent component = new TestComponent(Vector.build(100, 100), Vector.build(50, 50), AnchorMode.LeftTop, Vector.build(100, 200), Vector.Zero(), 0, 1);
        TestRelativeComponent child1 = new TestRelativeComponent(component, Vector.build(100, 100), AnchorMode.LeftTop, Vector.build(30, 0), Vector.Zero(),   Vector.build(50, 200), false, 1, 2);
        child1.addCollider(new RectCollider(Vector.build(0, 0), child1, child1.getSize()));
        component.addChild(child1);
        TestRelativeComponent child2 = new TestRelativeComponent(child1, Vector.build(50, 50), AnchorMode.LeftTop, Vector.build(10, 0), Vector.Zero(),  Vector.build(50, 200), false, 1, 3);
        child1.addChild(child2);
        child2.addCollider(new RectCollider(Vector.Zero(), child2, child2.getSize()));
        Component.addComponent(component);
        Component.addComponent(new TestComponent(Vector.build(200, 200), Vector.build(100, 100), AnchorMode.LeftTop, Vector.build(0, 50), Vector.Zero(), 0, 4));
        Component.addComponent(new TestComponent(Vector.build(300, 300), Vector.build(100, 100), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 5));
        Component.addComponent(new TestComponent(Vector.build(400, 400), Vector.build(100, 100), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 6));
        Component.addComponent(new TestComponent(Vector.build(600, 200), Vector.build(200, 100), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 7));
        Component.addComponent(new TestComponent(Vector.build(200, 600), Vector.build(100, 100), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 8));

        TestComponent testComponent = new TestComponent(Vector.build(600, 550), Vector.build(100, 150), AnchorMode.RightTop, Vector.build(200, 0), Vector.Zero(), 0, 9);
        testComponent.addCollider(new CircleCollider(Vector.build(-100, 0), testComponent, 30));
        testComponent.addCollider(new CircleCollider(Vector.build(200, 0), testComponent, 40));
        Component.addComponent(testComponent);

        TestRelativeComponent testRelativeComponent = new TestRelativeComponent(testComponent, Vector.build(50, 60), AnchorMode.LeftTop, Vector.build(500, 0), Vector.build(50, 20), Vector.build(300, 0), true, 0, 11);
        testComponent.addChild(testRelativeComponent);
        testRelativeComponent.addCollider(new RectCollider(Vector.build(0, 0), testRelativeComponent, testRelativeComponent.getSize()));
        TestRelativeComponent testRelativeComponent1 = new TestRelativeComponent(testRelativeComponent, Vector.build(30, 30), AnchorMode.LeftTop, Vector.build(20, 0), Vector.build(0, 0), Vector.build(1, 0), true, 0, 20);
        testRelativeComponent1.addCollider(new CircleCollider(Vector.Zero(), testRelativeComponent1, 15));
        testRelativeComponent.addChild(testRelativeComponent1);

        TestComponent testComponent1 = new TestComponent(Vector.build(1100, 600), Vector.build(100, 100), AnchorMode.LeftBottom, Vector.build(100, 200), Vector.Zero(), 0, 10);

        Component.addComponent(testComponent1);
        Component.addComponent(new FpsComponent());
        Component.addComponent(new TimeComponent());
        Component.addComponent(new CursorComponent());
        Component.addComponent(new CameraComponent());

        TestComponent boundaryLeft = new TestComponent(Vector.build(-10, 0), Vector.build(10, FrameSetting.DEFAULT_HEIGHT), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 100);
        boundaryLeft.addCollider(new RectCollider(Vector.build(0, 0), boundaryLeft, boundaryLeft.getSize()));
        Component.addComponent(boundaryLeft);
        TestComponent boundaryRight = new TestComponent(Vector.build(FrameSetting.DEFAULT_WIDTH + 10, 0), Vector.build(10, FrameSetting.DEFAULT_HEIGHT), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 101);
        boundaryRight.addCollider(new RectCollider(Vector.build(0, 0), boundaryRight, boundaryRight.getSize()));
        Component.addComponent(boundaryRight);
        TestComponent boundaryTop = new TestComponent(Vector.build(0, -10), Vector.build(FrameSetting.DEFAULT_WIDTH, 10), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 102);
        boundaryTop.addCollider(new RectCollider(Vector.build(0, 0), boundaryTop, boundaryTop.getSize()));
        Component.addComponent(boundaryTop);
        TestComponent boundaryBottom = new TestComponent(Vector.build(0, FrameSetting.DEFAULT_HEIGHT + 10), Vector.build(FrameSetting.DEFAULT_WIDTH, 10), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), 0, 103);
        boundaryBottom.addCollider(new RectCollider(Vector.build(0, 0), boundaryBottom, boundaryBottom.getSize()));
        Component.addComponent(boundaryBottom);

        TestCharacter testCharacter = new TestCharacter(Vector.build(300, 300), Vector.build(100, 100));
        Component.addComponent(testCharacter);
    }
}
