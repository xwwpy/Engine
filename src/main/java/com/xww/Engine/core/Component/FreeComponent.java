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
                         int CollisionRegion,
                         int mass,
                         boolean whetherShowDebugInfo,
                         boolean is_drag_on){
        this.parent = null;
        this.worldPosition = worldPosition;
        this.positionType = positionType;
        this.size = size;
        this.anchorMode = anchorMode;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.order = order;
        this.CollisionRegion = CollisionRegion;
        this.mass = mass;
        this.whetherShowDebugInfo = whetherShowDebugInfo;
        this.is_drag_on = is_drag_on;
        this.on_create();
    }
}
