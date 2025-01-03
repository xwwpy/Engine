package com.xww.NewEngine.core.Collision;

import com.xww.NewEngine.core.Component.Component;
import com.xww.NewEngine.core.Vector.Vector;

public class RectCollider extends BaseCollider{
    private Vector size;
    public RectCollider(Vector relativePosition, Component owner, Vector size) {
        super(relativePosition, owner);
        this.size = size;
    }
}
