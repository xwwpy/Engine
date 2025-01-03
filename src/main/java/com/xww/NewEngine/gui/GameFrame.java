package com.xww.NewEngine.gui;

import com.xww.NewEngine.Test.TestComponent;
import com.xww.NewEngine.Test.TestRelativeComponent;
import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Collision.CircleCollider;
import com.xww.NewEngine.core.Component.Component;
import com.xww.NewEngine.core.Component.impl.FpsComponent;
import com.xww.NewEngine.core.Component.impl.TimeComponent;
import com.xww.NewEngine.core.Event.TimeEventManager;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.setting.FrameSetting;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameFrame extends JFrame{
    public static enum PositionType {
        World, // 世界坐标
        Screen, // 在当前屏幕的坐标
    }
    public static GameFrame context;
    public static GamePanel gamePanel;
    // 每一帧指定耗时 单位nanos
    private int each_frame_target_time = 1000_000_000 / FrameSetting.DEFAULT_FPS;
    public int current_fps = FrameSetting.DEFAULT_FPS;
    public Vector screen_position;
    public Vector camera_position = new Vector(0, 0);
    private GameFrame(String title) {
        super(title);
    }
    private static void init() {
        context = new GameFrame(FrameSetting.DEFAULT_TITLE);
        gamePanel = new GamePanel();
        context.setSize(FrameSetting.DEFAULT_WIDTH, FrameSetting.DEFAULT_HEIGHT);
        context.setCenterPosition();
        context.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        context.setResizable(false);
        context.add(gamePanel);
        game_on_start();
        init_test();
        context.setVisible(true);
    }

    private static void init_test() {
        TestComponent component = new TestComponent(Vector.Zero(), Vector.build(50, 50), AnchorMode.LeftTop, Vector.build(10, 0), Vector.Zero(), 0, 1);
        TestRelativeComponent child1 = new TestRelativeComponent(component, Vector.build(100, 100), AnchorMode.LeftTop, Vector.build(-100, 0), Vector.Zero(),   Vector.build(100, 200), false, 1, 2);
        component.addChild(child1);
        TestRelativeComponent child2 = new TestRelativeComponent(child1, Vector.build(50, 50), AnchorMode.LeftTop, Vector.build(10, 0), Vector.Zero(),  Vector.build(100, 200), false, 1, 3);
        child1.addChild(child2);
        Component.addComponent(component);
        Component.addComponent(new TestComponent(Vector.build(200, 200), Vector.build(100, 100), AnchorMode.Center, Vector.build(0, 50), Vector.Zero(), 0, 4));
        Component.addComponent(new TestComponent(Vector.build(300, 300), Vector.build(100, 100), AnchorMode.CenterTop, Vector.Zero(), Vector.Zero(), 0, 5));
        Component.addComponent(new TestComponent(Vector.build(400, 400), Vector.build(100, 100), AnchorMode.RightTop, Vector.Zero(), Vector.Zero(), 0, 6));
        Component.addComponent(new TestComponent(Vector.build(600, 200), Vector.build(200, 100), AnchorMode.Center, Vector.Zero(), Vector.Zero(), 0, 7));

        Component.addComponent(new TestComponent(Vector.build(200, 600), Vector.build(100, 100), AnchorMode.RightTop, Vector.Zero(), Vector.Zero(), 0, 8));
        TestComponent testComponent = new TestComponent(Vector.build(600, 600), Vector.build(100, 100), AnchorMode.RightTop, Vector.build(200, 0), Vector.Zero(), 0, 9);
        testComponent.addCollider(new CircleCollider(Vector.build(-100, 0), testComponent, 30));
        testComponent.addCollider(new CircleCollider(Vector.build(200, 0), testComponent, 40));
        Component.addComponent(testComponent);
        TestComponent testComponent1 = new TestComponent(Vector.build(1100, 600), Vector.build(100, 100), AnchorMode.RightTop, Vector.Zero(), Vector.Zero(), 0, 10);
        testComponent1.addCollider(new CircleCollider(Vector.build(-100, 0), testComponent1, 50));
        Component.addComponent(testComponent1);
        Component.addComponent(new FpsComponent());
        Component.addComponent(new TimeComponent());
    }


    public static void start() {
        // 初始化
        init();
        // 主循环
        while (true) {
            double start_time = System.nanoTime();
//            context.paint(context.getGraphics());
//            gamePanel.paint(gamePanel.getGraphics());
            gamePanel.repaint();
            double end_time = System.nanoTime();
            double each_frame_time = end_time - start_time;
            if (each_frame_time < context.each_frame_target_time){
                try {
                    Thread.sleep((long) (context.each_frame_target_time - each_frame_time) / 1000_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            double whole_time = System.nanoTime() - start_time;
            context.current_fps = (int) (1_000_000_000 / whole_time);
        }
    }

    private static void game_on_start() {
        TimeEventManager.start();
    }

    static void on_update(Graphics g) {
        // 更新定时器以及时间事件管理器
        update_time();
        // 更新组件
        updateComponent(g);
    }

    private static void update_time() {
        TimeEventManager.tick();
    }

    private static void updateComponent(Graphics g) {
        Component.components_to_remove.forEach(component -> {
            component.on_destroy();
            Component.components.remove(component);
        });
        Component.components_to_remove.clear();
        Component.components.stream().sorted().forEach(component -> {
            component.on_update(g);
            if (!component.isAlive()){
                Component.components_to_remove.add(component);
            }
        });
    }

    // 将窗口置于屏幕中心
    public void setCenterPosition() {
        this.setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().width - FrameSetting.DEFAULT_WIDTH) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - FrameSetting.DEFAULT_HEIGHT) / 2
        );
    }
    public void changeFps(int fps) {
        FrameSetting.DEFAULT_FPS = fps;
        this.each_frame_target_time = 1_000_000_000 / FrameSetting.DEFAULT_FPS;
    }
    // 得到相对屏幕坐标
    public Vector getRealDrawPosition(Vector position, PositionType type) {
        switch(type) {
            case World:
                return position.sub_to_self(GameFrame.context.camera_position);
            case Screen:
                return position;
            default:
                return Vector.Zero();
        }
    }

    public static Vector getLeftTopWorldPosition(Component component) {
        Vector position = component.getWorldPosition();
        switch(component.getAnchorMode()) {
            case LeftTop:
                return position;
            case LeftBottom:
                return position.sub(Vector.build(0, component.getSize().getFullY()));
            case CenterTop:
                return position.sub(Vector.build(component.getSize().getFullX() / 2 , 0));
            case CenterBottom:
                return position.sub(Vector.build(component.getSize().getFullX() / 2, component.getSize().getFullY()));
            case RightTop:
                return position.sub(Vector.build(component.getSize().getFullX(), 0));
            case RightBottom:
                return position.sub(Vector.build(component.getSize().getFullX(), component.getSize().getFullY()));
            case Center:
                return position.sub(Vector.build(component.getSize().getFullX() / 2, component.getSize().getFullY() / 2));
            default:
                System.out.println("未支持的锚点模式");
                return position;
        }
    }

    /**
     * 得到速度或加速度每一帧应该变化的值
     */
    public static Vector getFrameVelocity(Vector velocity) {
        return velocity.divide(GameFrame.context.current_fps);
    }
}


class GamePanel extends JPanel {

    // 当前所显示区域全部画在image上 图片的(0, 0)点为当前窗口显示位置的左上角
    public static BufferedImage image = new BufferedImage(FrameSetting.DEFAULT_WIDTH, FrameSetting.DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics graphics = image.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.clearRect(0, 0, FrameSetting.DEFAULT_WIDTH, FrameSetting.DEFAULT_HEIGHT);
        // 更新屏幕坐标
        Point screen_position = this.getLocationOnScreen();
        GameFrame.context.screen_position = new Vector(screen_position.x, screen_position.y);
        GameFrame.on_update(graphics);
        g.drawImage(image, 0, 0, null);
    }

}
