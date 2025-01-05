package com.xww.NewEngine.gui;

import com.xww.NewEngine.core.Component.Component;
import com.xww.NewEngine.core.Component.impl.ScreenInfoComponent;
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
    public Vector camera_position = new Vector(100, 0);

    private static boolean initFlag = false;
    private GameFrame(String title) {
        super(title);
    }

    /**
     * 调用开始之前必须线初始化
     */
    public static void init() {
        context = new GameFrame(FrameSetting.DEFAULT_TITLE);
        gamePanel = new GamePanel();
        context.setSize(FrameSetting.DEFAULT_WIDTH, FrameSetting.DEFAULT_HEIGHT);
        context.setCenterPosition();
        context.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        context.setResizable(false);
        context.add(gamePanel);
        game_on_start();
        initFlag = true;
    }


    public static void start() {
        if (!initFlag){
            System.out.println("游戏主循环开始之前必须调用初始化逻辑");
        }
        // 初始化
        context.setVisible(true);
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
        // 添加更新屏幕位置的组件
        Component.addComponent(new ScreenInfoComponent());
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
        Component.components.addAll(Component.components_to_add);
        Component.components_to_add.clear();
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
                return position.sub(GameFrame.context.camera_position);
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
        GameFrame.on_update(graphics);
        g.drawImage(image, 0, 0, null);
    }

}
