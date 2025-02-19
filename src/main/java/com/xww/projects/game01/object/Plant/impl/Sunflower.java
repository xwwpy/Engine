package com.xww.projects.game01.object.Plant.impl;

import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game01.object.Plant.BasePlant;

public class Sunflower extends BasePlant {
    private enum AnimationType {
        NORMAL("normal"),
        LOW_LIFE("lowLife"),
        GENERATE_SUN("generateSun");
        private final String name;
        AnimationType(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }
    public Sunflower(Vector worldPosition, Vector size, Vector velocity, Vector acceleration, int order, int activeCollisionZone, int hitCollisionZone, int mass, boolean whetherShowDebugInfo, boolean is_drag_on, int life, int atk, int atk_interval) {
        super(worldPosition, size, velocity, acceleration, order, activeCollisionZone, hitCollisionZone, mass, whetherShowDebugInfo, is_drag_on, life, atk, atk_interval);
        // 添加idle动画
        // 添加产生阳光动画
    }

    @Override
    public void selectCurrentAnimation() {
    }

    @Override
    protected boolean tryAtk() {
        return false;
    }

    @Override
    public BasePlant tryFusion(BasePlant otherPlant) {
        return null;
    }
}
