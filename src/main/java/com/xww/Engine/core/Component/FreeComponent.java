package com.xww.Engine.core.Component;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

// 自由组件
public abstract class FreeComponent extends Component{

    public FreeComponent(Vector worldPosition,
                         GameFrame.PositionType positionType,
                         Vector size,
                         AnchorMode anchorMode,
                         Vector velocity,
                         Vector acceleration,
                         int order,
                         int CollisionRegion){
        this.parent = null;
        this.worldPosition = worldPosition;
        this.positionType = positionType;
        this.size = size;
        this.anchorMode = anchorMode;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.order = order;
        this.CollisionRegion = CollisionRegion;
        this.on_create();
    }
}
