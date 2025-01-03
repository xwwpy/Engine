package com.xww.NewEngine.core.Component;

import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;

public abstract class RelativeComponent extends Component {
    // 相对附属对象的位置
    protected Vector relative_position = Vector.Zero();

    /**
     * 是否固定 如果固定则速度和父类保持一致 且 始终维持创建时指定的相对位置
     * 并且当本身具有碰撞体时 会影响父类的移动
     * 若不固定则其与父类的运动是独立的不会相互影响
     */
    protected boolean WhetherPinned = false;

    /**
     * 指定父类
     */
    public RelativeComponent(Component parent,
                             Vector size,
                             AnchorMode anchorMode,
                             Vector velocity,
                             Vector acceleration,
                             Vector relative_position,
                             boolean WhetherPinned,
                             int order){
        if (parent == null){
            throw new RuntimeException("应该使用自由节点作为顶层节点");
        }
        this.parent = parent;
        this.size = size;
        this.anchorMode = anchorMode;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.WhetherPinned = WhetherPinned;
        this.order = order;
        this.relative_position = relative_position;
        this.worldPosition = parent.getWorldPosition().add(relative_position);
        this.on_create();
    }

    @Override
    protected void checkMove() {
        // 不是固定时才执行检查移动的逻辑
        if (!this.WhetherPinned){
            super.checkMove();
        } else {
            this.worldPosition = this.parent.getWorldPosition().add(this.relative_position);
        }
    }

    @Override
    protected void pre_move() {
        super.pre_move();
        this.relative_position.add_to_self(GameFrame.getFrameVelocity(this.velocity)).add_to_self(GameFrame.getFrameVelocity(this.parent.getVelocity()));
    }
}
