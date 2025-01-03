package com.xww.NewEngine.core.Component;

import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;

// 自由组件
public abstract class FreeComponent extends Component{

    public FreeComponent(Vector worldPosition,
                         GameFrame.PositionType positionType,
                         Vector size,
                         AnchorMode anchorMode,
                         Vector velocity,
                         Vector acceleration,
                         int order){
        this.parent = null;
        this.worldPosition = worldPosition;
        this.positionType = positionType;
        this.size = size;
        this.anchorMode = anchorMode;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.order = order;
        this.on_create();
    }
}
