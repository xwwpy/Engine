package com.xww.Engine.Core;

import com.xww.Engine.Vector.Vector;

import java.awt.*;

public class SceneRelativeComponent extends SceneComponent{

    protected Vector relativePosition; // 相对父类的位置

    public SceneRelativeComponent(Vector relativePosition,
                                  Vector size,
                                  SceneComponent parent,
                                  Anchor anchorMode,
                                  double one_frame_gap,
                                  int frame_threshold){
        parent.addChild(this);
        this.relativePosition = relativePosition;
        this.size = size;
        this.anchorMode = anchorMode;
        this.one_frame_gap = one_frame_gap;
        this.frame_threshold = frame_threshold;
    }
    @Override
    protected void logicUpdate() {

    }

    @Override
    protected void draw(Graphics g) {

    }

    @Override
    public Vector getWorldPosition() {
        if (parent != null) return parent.getWorldPosition().add(this.relativePosition);
        return relativePosition;
    }
}
