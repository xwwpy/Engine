package com.xww.projects.game01.object.Plant;

import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game01.object.BaseObject;
import com.xww.projects.game01.object.ObjectType;

public abstract class BasePlant extends BaseObject {


    public BasePlant(Vector worldPosition,
                     Vector size,
                     int mass,
                     int life,
                     int atk_interval,
                     ObjectType objectType) {
        super(worldPosition, size, mass, life, atk_interval, objectType);
    }

    /**
     *
     * @param otherPlant 与之融合的植物
     * @return 融合后的植物 如果不能融合则返回null
     */
    public BasePlant tryFusion(BasePlant otherPlant){
        return FusionUtils.tryFusion(this, otherPlant);
    }

}
