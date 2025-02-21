package com.xww.projects.game01.object.Plant.impl;

import com.xww.Engine.core.Vector.Vector;
import com.xww.projects.game01.object.ObjectType;
import com.xww.projects.game01.object.Plant.BasePlant;

public class Sunflower extends BasePlant {
    public Sunflower(Vector worldPosition,
                     Vector size,
                     int mass,
                     int life,
                     int atk_interval,
                     ObjectType objectType) {
        super(worldPosition, size, mass, life, atk_interval, objectType);
    }

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

    @Override
    protected boolean tryAtk() {
        return false;
    }

    @Override
    protected void onInvulnerableHit() {

    }

    @Override
    protected void onHit() {

    }

    @Override
    public BasePlant tryFusion(BasePlant otherPlant) {
        return null;
    }
}
