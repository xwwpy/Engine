package com.xww.projects.game01.Card;

import com.xww.Engine.Utils.ImgUtils;
import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Timer.Timer;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;


public abstract class BaseCard extends FreeComponent {
    public static final Vector card_size = new Vector(110, 120);
    protected final Image card_image;
    protected int coolDownTime; // 单位毫秒
    protected int currentCoolDownTime = 0;
    protected Vector virtual_card_position = Vector.Zero(); // 用于显示拖动后的卡片的虚拟卡片 相对原卡片位置的相对位置

    protected Timer timer = new Timer(10, (obj) -> {
        if (!this.is_cool_down){
            return;
        }
        if (this.currentCoolDownTime >= this.coolDownTime){
            this.currentCoolDownTime = 0;
            this.is_cool_down = false;
            return;
        }
        this.currentCoolDownTime += 10;
    }, this);

    public int card_cost;
    public boolean is_cool_down;
    public BaseCard(Vector worldPosition, String card_img_path, int coolDownTime, int card_cost, boolean whetherInitCoolDown) {
        super(worldPosition, GameFrame.PositionType.Screen, card_size, AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE - 10, -1, -1, true, true);
        card_image = ImgUtils.getScaledImage(ImgUtils.loadImage(card_img_path), card_size.getX(), card_size.getY());
        this.coolDownTime = coolDownTime;
        this.card_cost = card_cost;
        this.is_cool_down = whetherInitCoolDown;
        this.whetherCanDrag = true;
        init();
    }

    @Override
    public void on_update(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(this.getDrawPosition().getX(), this.getDrawPosition().getY(), this.size.getX(), this.size.getY());
        g.drawImage(card_image, this.getDrawPosition().getX(), this.getDrawPosition().getY(), card_size.getX(), card_size.getY(), null);
        if (!whetherHaveEnoughSun()){
            g.setColor(new Color(0, 0, 0, 128));
            g.fillRect(this.getDrawPosition().getX(), this.getDrawPosition().getY(), this.size.getX(), this.size.getY());
        }
        if (is_cool_down) {
            // 透明黑色矩形
            g.setColor(new Color(0, 0, 0, 128));
            g.fillRect(this.getDrawPosition().getX(), this.getDrawPosition().getY(), this.size.getX(), (int) (this.size.getFullY() * (1.0 - (float) currentCoolDownTime / coolDownTime)));
        }
        // 绘制虚拟卡片
        if (!virtual_card_position.equals(Vector.Zero())) {
            g.drawImage(card_image, this.getVirtual_card_position().getX(), this.getVirtual_card_position().getY(), card_size.getX(), card_size.getY(), null);
        }
        super.on_update(g);
    }

    /**
     *
     * @return 是否成功种植或者融合
     */
    protected abstract boolean generateCharacter(Vector position);

    protected void generateSuccess() {
        CardBar.CurrentSun -= this.card_cost;
        this.coolDown();
    }

    /**
     * 进入冷却应执行的逻辑
     */
    protected void coolDown() {
        this.is_cool_down = true;
    }

    protected abstract void generateFail();

    @Override
    public boolean whether_mouse_in(double x, double y) {
        if (this.is_cool_down || !whetherHaveEnoughSun()) return false;
        Vector absolutePosition = this.getVirtual_card_position();
        if (x >= absolutePosition.getFullX() && x <= absolutePosition.getFullX() + this.size.getFullX()
                && y >= absolutePosition.getFullY() && y <= absolutePosition.getFullY() + this.size.getFullY()) {
            this.last_mouse_check_self_position = new Vector(x, y);
            return true;
        } else {
            return false;
        }
    }

    protected boolean whetherHaveEnoughSun(){
        return CardBar.CurrentSun >= this.card_cost;
    }

    @Override
    public void process_mouse_choose_self(int x, int y) {
        double x_gap = x - this.last_mouse_check_self_position.getFullX();
        double y_gap = y - this.last_mouse_check_self_position.getFullY();
        this.virtual_card_position = this.virtual_card_position.add(new Vector(x_gap, y_gap));
        last_mouse_check_self_position = new Vector(x, y);
    }
    public Vector getVirtual_card_position() {
        return this.getDrawPosition().add(this.virtual_card_position);
    }

    @Override
    public void process_mouse_release() {
        if (this.card_cost <= CardBar.CurrentSun){
            if (generateCharacter(this.getWorldPosition().add(this.virtual_card_position))) {
                // 种植成功后执行的逻辑
                generateSuccess();
            } else {
                generateFail();
            }
        }
        this.virtual_card_position = Vector.Zero();
    }

    public void init(){
        this.timer.setRun_times(-1);
        this.timer.restart();
        this.addTimer(timer);
    }

    @Override
    public void setWhetherCanDrag(boolean whetherCanDrag) {
        this.whetherCanDrag = true;
    }

    @Override
    public void drawWhetherCanDrag(Graphics g) {
        // do nothing
    }
}
