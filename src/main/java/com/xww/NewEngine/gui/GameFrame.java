package com.xww.NewEngine.gui;

import com.xww.NewEngine.core.Component.Component;
import com.xww.NewEngine.core.Component.impl.ScreenInfoComponent;
import com.xww.NewEngine.core.Event.Message.Impl.KeyBoardMessageHandler;
import com.xww.NewEngine.core.Event.Message.Impl.MouseMessageHandler;
import com.xww.NewEngine.core.Event.Message.Message;
import com.xww.NewEngine.core.Event.Message.MessageHandler;
import com.xww.NewEngine.core.Event.TimeEventManager;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.setting.FrameSetting;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class GameFrame extends JFrame{
    // 游戏画板
    public static class GamePanel extends JPanel {

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
    // 坐标类型
    public enum PositionType {
        World, // 世界坐标
        Screen, // 在当前屏幕的坐标
    }

    public static GameFrame context;
    public static GamePanel gamePanel;

    // 消息处理器 主要处理 键盘和鼠标输入
    public static Set<MessageHandler> messageHandlers = new HashSet<>();
    public static Set<MessageHandler> messageHandlers_to_remove = new HashSet<>();

    // 每一帧指定耗时 单位nanos
    private long each_frame_target_time = 1000_000_000 / FrameSetting.DEFAULT_FPS;
    public long current_fps = FrameSetting.DEFAULT_FPS;

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
        initEventHandler();
        game_on_start();
        initFlag = true;
    }

    // 初始化事件处理器
    private static void initEventHandler() {
        context.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }
            @Override
            public void keyPressed(KeyEvent e) {
                process_message(new Message(e, Message.MessageType.KeyBoard));
            }
            @Override
            public void keyReleased(KeyEvent e) {
                process_message(new Message(e, Message.MessageType.KeyBoard));
            }
        });
        context.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                process_message(new Message(e, Message.MessageType.MouseClicked));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                process_message(new Message(e, Message.MessageType.MousePressed));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                process_message(new Message(e, Message.MessageType.MouseReleased));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                process_message(new Message(e, Message.MessageType.MouseEnter));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                process_message(new Message(e, Message.MessageType.MouseExit));
            }
        });
        messageHandlers.add(new MouseMessageHandler());
        messageHandlers.add(new KeyBoardMessageHandler());
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
        // 更新相机位置
        Camera.updateCamera();

        // 更新鼠标位置
        messageHandlers.forEach((handler)->{
            if (handler.checkValid() && handler instanceof MouseMessageHandler){
                Point point = MouseInfo.getPointerInfo().getLocation();
                Vector screen_position = ScreenInfoComponent.screen_position;
                ((MouseMessageHandler) handler).processMouseMoved(new MouseEvent(context, 0, 0, 0, point.x - screen_position.getX(), point.y - screen_position.getY(), 0, false));
            }
        });

    }

    private static void update_time() {
        TimeEventManager.tick();
    }

    private static void updateComponent(Graphics g) {
        Component.components.addAll(Component.components_to_add);
        Component.allComponents_to_add.addAll(Component.components_to_add);

        Component.components_to_add.clear();

        Component.allComponents_to_remove.addAll(Component.components_to_remove);

        Component.components_to_remove.forEach(component -> {
            component.on_destroy();
            Component.components.remove(component);
        });
        Component.components_to_remove.clear();
        Component.components.stream().sorted().forEach(component -> {
            component.on_update(g);
        });
        Component.allComponents.addAll(Component.allComponents_to_add);
        Component.allComponents_to_add.clear();
        Component.allComponents.removeAll(Component.allComponents_to_remove);
        Component.allComponents_to_remove.clear();
        // 更新拖拽组件
        Component.updateDragComponents();
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
        return switch (type) {
            case World -> position.sub(Camera.camera_position);
            case Screen -> position;
            default -> Vector.Zero();
        };
    }

    public static Vector getLeftTopWorldPosition(Component component) {
        Vector position = component.getWorldPosition();
        return switch (component.getAnchorMode()) {
            case LeftTop -> position;
            case LeftBottom -> position.sub(Vector.build(0, component.getSize().getFullY()));
            case CenterTop -> position.sub(Vector.build(component.getSize().getFullX() / 2, 0));
            case CenterBottom ->
                    position.sub(Vector.build(component.getSize().getFullX() / 2, component.getSize().getFullY()));
            case RightTop -> position.sub(Vector.build(component.getSize().getFullX(), 0));
            case RightBottom ->
                    position.sub(Vector.build(component.getSize().getFullX(), component.getSize().getFullY()));
            case Center ->
                    position.sub(Vector.build(component.getSize().getFullX() / 2, component.getSize().getFullY() / 2));
            default -> {
                System.out.println("未支持的锚点模式");
                yield position;
            }
        };
    }

    /**
     * 得到速度或加速度每一帧应该变化的值
     */
    public static Vector getFrameVelocity(Vector velocity) {
        return velocity.divide(GameFrame.context.current_fps);
    }

    public static void process_message(Message message) {
        for (MessageHandler messageHandler : messageHandlers) {
            messageHandler.handle(message);
            if (!messageHandler.checkValid()){
                messageHandlers_to_remove.add(messageHandler);
            }
        }
        messageHandlers.removeAll(messageHandlers_to_remove);
        messageHandlers_to_remove.clear();
    }
}


