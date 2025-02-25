package com.xww.Engine.core.Barrier;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.Camera;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.DebugSetting;

import java.awt.*;
import java.util.Set;

public abstract class BaseBarrier {

    public static boolean enableDebug = true;
    public static final Set<BaseBarrier> barriers = new java.util.HashSet<>();
    public static final Set<BaseBarrier> barriers_to_add = new java.util.HashSet<>();
    public static final Set<BaseBarrier> barriers_to_remove = new java.util.HashSet<>();
    protected GameFrame.PositionType positionType = GameFrame.PositionType.World;

    protected Vector worldPosition; // 障碍物位置

    protected Vector size; // 障碍物大小
    protected boolean whether_enable = true;
    protected boolean whether_alive = true;

    public BaseBarrier(Vector worldPosition, Vector size){
        this.worldPosition = worldPosition;
        this.size = size;
    }

    /*
     * 判断是否在障碍物上
     */
    public static void whetherOnGround(Vector lastPosition, Vector nowPosition, Character character) {
        Vector size = character.getLogicSize();
        for (BaseBarrier barrier : barriers) {
            if (barrier.whether_enable && barrier.whether_alive) {
                Vector barrierPos = barrier.getWorldPosition();
                Vector barrierSize = barrier.getSize();
                if (!(nowPosition.getFullX() >= barrierPos.getFullX() + barrierSize.getFullX() || nowPosition.getFullX() + size.getFullX() <= barrierPos.getFullX())){
                    if (lastPosition.getFullY() + size.getFullY() < barrierPos.getFullY() && nowPosition.getFullY() + size.getFullY() >= barrierPos.getFullY()){
                        character.on_ground(barrier);
                        return;
                    } else if (character.getLastOnGround() == barrier && nowPosition.getFullY() - lastPosition.getFullY() < 1 * 10e-6  && character.isWhetherOnGround()){
                        character.on_ground(barrier);
                        return;
                    }
                } else if (character.getLastOnGround() == barrier){
                    character.setWhetherOnGround(false);
                    character.setLastOnGround(null);
                    return;
                }
            }
        }
        character.setWhetherOnGround(false);
    }

    public static void updateBarriers(Graphics g) {
        barriers.addAll(barriers_to_add);
        barriers.forEach((barrier) -> {
            if (!barrier.whether_alive) {
                barrier.whether_enable = false;
                barriers_to_remove.add(barrier);
            } else if(barrier.whether_enable){
                if (enableDebug && DebugSetting.IS_DEBUG_ON) barrier.onRender(g);
            }
        });
        barriers.removeAll(barriers_to_remove);
        barriers_to_add.clear();
        barriers_to_remove.clear();
    }
    public static void registerBarrier(BaseBarrier barrier) {
        barriers_to_add.add(barrier);
    }

    protected abstract void onRender(Graphics g);

    public Vector getWorldPosition() {
        switch (this.positionType) {
            case World -> {
                return worldPosition;
            }
            case Screen -> {
                return Camera.camera_position.add(worldPosition);
            }
            default -> {
                throw new RuntimeException("Component 组件目前不支持 获取世界坐标的坐标类型: " + this.positionType);
            }
        }
    }

    public Vector getDrawPosition() {
        return GameFrame.getRealDrawPosition(this.getWorldPosition());
    }

    public GameFrame.PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(GameFrame.PositionType positionType) {
        this.positionType = positionType;
    }

    public void setWorldPosition(Vector worldPosition) {
        this.worldPosition = worldPosition;
    }

    public Vector getSize() {
        return size;
    }

    public void setSize(Vector size) {
        this.size = size;
    }

    public boolean isWhether_enable() {
        return whether_enable;
    }

    public void setWhether_enable(boolean whether_enable) {
        this.whether_enable = whether_enable;
    }

    public boolean isWhether_alive() {
        return whether_alive;
    }

    public void setWhether_alive(boolean whether_alive) {
        this.whether_alive = whether_alive;
    }
}
