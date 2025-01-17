package com.xww.projects.game01.object.Plant;

import com.xww.Engine.core.Component.Component;
import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game01.object.BaseObject;
import com.xww.projects.game01.object.ObjectType;

public abstract class BasePlant extends BaseObject {

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
        super(worldPosition, size, velocity, acceleration, order, CollisionRegion, mass, whetherShowDebugInfo, is_drag_on, life, atk, atk_interval, ObjectType.PLANT);
    }

    /**
     *
     * @param otherPlant 与之融合的植物
     * @return 融合后的植物 如果不能融合则返回null
     */
    public BasePlant tryFusion(BasePlant otherPlant){
        return FusionUtils.tryFusion(this, otherPlant);
    }

    @Override
    public boolean whetherCheckCollision(Component other) {
        return super.whetherCheckCollision(other);
    }
}
