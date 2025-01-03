package com.xww.NewEngine.core.Collision;

import com.xww.NewEngine.core.Component.Component;
import com.xww.NewEngine.core.Vector.Vector;

public class CircleCollider extends BaseCollider{
    private double radius;

    public CircleCollider(Vector relativePosition, Component owner, double radius) {
        super(relativePosition, owner);
        this.radius = radius;
    }
}
