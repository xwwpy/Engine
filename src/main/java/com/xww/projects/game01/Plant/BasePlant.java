package com.xww.projects.game01.Plant;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Vector.Vector;

public abstract class BasePlant extends Character {

    public BasePlant(Vector worldPosition,
                     Vector size,
                     Vector velocity,
                     Vector acceleration,
                     int order,
                     int CollisionRegion,
                     int mass,
                     boolean whetherShowDebugInfo,
                     boolean is_drag_on,
                     int life,
                     int atk,
                     int atk_interval) {
        super(worldPosition, size, velocity, acceleration, order, CollisionRegion, mass, whetherShowDebugInfo, is_drag_on, life, atk, atk_interval);
    }

    /**
     *
     * @param otherPlant 与之融合的植物
     * @return 融合后的植物 如果不能融合则返回null
     */
    public abstract BasePlant tryFusion(BasePlant otherPlant);
}
