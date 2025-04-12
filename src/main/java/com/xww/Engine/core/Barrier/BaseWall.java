package com.xww.Engine.core.Barrier;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.Camera;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.DebugSetting;

import java.awt.*;
import java.util.Set;

public abstract class BaseWall {
    public static boolean enableDebug = true;
    public static final Set<BaseWall> walls = new java.util.HashSet<>();
    public static final Set<BaseWall> walls_to_add = new java.util.HashSet<>();
    public static final Set<BaseWall> walls_to_remove = new java.util.HashSet<>();
    protected static Color DebugColor = Color.WHITE;
    protected GameFrame.PositionType positionType = GameFrame.PositionType.World;

    protected Vector worldPosition; // 障碍物位置

    protected Vector size; // 障碍物大小
    protected boolean whether_enable = true;
    protected boolean whether_alive = true;

    protected boolean whetherEnableClimb = true;
    protected int f = 99;


    protected boolean whetherRenderOnlyForDebug;

    public BaseWall(Vector worldPosition, Vector size, boolean whetherRenderOnlyForDebug){
        this.worldPosition = worldPosition;
        this.size = size;
        this.whetherRenderOnlyForDebug = whetherRenderOnlyForDebug;
    }


    public static void updateWalls(Graphics g) {
        walls.addAll(walls_to_add);
        walls.forEach((barrier) -> {
            if (!barrier.whether_alive) {
                barrier.whether_enable = false;
                walls_to_remove.add(barrier);
            } else if(barrier.whether_enable){
                // 如果只是在debug模式下进行渲染
                if (barrier.whetherRenderOnlyForDebug) {
                    if (enableDebug && DebugSetting.IS_DEBUG_ON) barrier.onRender(g);
                }
                else barrier.onRender(g);
            }
        });
        walls.removeAll(walls_to_remove);
        walls_to_add.clear();
        walls_to_remove.clear();
    }

    public static void whetherCrossWall(Vector lastPosition, Vector nowPosition, Character character) {
        Vector size = character.getLogicSize();
        for (BaseWall wall : walls) {
            if (wall.whether_enable && wall.whether_alive) {
                Vector wallPos = wall.getWorldPosition();
                Vector wallSize = wall.getSize();
                // 判断角色是否在墙的判断范围内
                if (!(nowPosition.getFullY() > wallPos.getFullY() + wallSize.getFullY() || nowPosition.getFullY() + size.getFullY() < wallPos.getFullY())){
                    if (lastPosition.getFullX() + size.getFullX() <= wallPos.getFullX() && nowPosition.getFullX() + size.getFullX() >= wallPos.getFullX()){
                        double dis =  nowPosition.getFullX() + size.getFullX() - wallPos.getFullX();
                        if (wall.whetherEnableClimb && character.isWhetherCanClimbWall()) {
                            character.setCurrentClimbWall(wall);
                            character.getVelocity().y = 0;
                            character.setWhetherJumping(false);
                            character.resetJumpState();
                        }
                        character.changePosition(Vector.build(- (dis + 1), 0));
                    } else if (lastPosition.getFullX() >= wallPos.getFullX() + wallSize.getFullX() && nowPosition.getFullX() <= wallPos.getFullX() + wallSize.getFullX()){
                        double dis =  wallPos.getFullX() + wallSize.getFullX() - nowPosition.getFullX();
                        if (wall.whetherEnableClimb && character.isWhetherCanClimbWall()) {
                            character.setCurrentClimbWall(wall);
                            character.getVelocity().y = 0;
                            character.setWhetherJumping(false);
                            character.resetJumpState();
                        }
                        character.changePosition(Vector.build(dis + 1, 0));
                    }
                } else {
                    // 当角色离开墙的范围内，自动解除锁定墙体的状态
                    if (character.getCurrentClimbWall() == wall){
                        character.clearClimbState();
                    }
                }
            }
        }
    }

    public static void registerWall(BaseWall wall) {
        walls_to_add.add(wall);
    }

    public static void clear() {
        walls.clear();
        walls_to_add.clear();
        walls_to_remove.clear();
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

    public boolean isWhetherEnableClimb() {
        return whetherEnableClimb;
    }

    public void setWhetherEnableClimb(boolean whetherEnableClimb) {
        this.whetherEnableClimb = whetherEnableClimb;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }
}
