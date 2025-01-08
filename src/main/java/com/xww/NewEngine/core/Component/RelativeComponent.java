package com.xww.NewEngine.core.Component;

import com.xww.NewEngine.core.Anchor.AnchorMode;
import com.xww.NewEngine.core.Collision.BaseCollider;
import com.xww.NewEngine.core.Vector.Vector;
import com.xww.NewEngine.gui.GameFrame;

public abstract class RelativeComponent extends Component {
    // 指定锚点位置相对附属对象左上角的位置
    protected Vector relative_position;

    /**
     * 是否固定 如果固定则速度和父类保持一致 且 始终维持创建时指定的相对位置
     * 并且当本身具有碰撞体时 会影响父类的移动
     * 若不固定则其与父类的运动是独立的不会相互影响
     */
    protected boolean WhetherPinned;

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
                             int order,
                             int CollisionRegion){
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
        this.CollisionRegion = CollisionRegion;
        this.worldPosition = parent.getLeftTopWorldPosition().add(relative_position);
        // 将相对位置更新为真正的相对父类左上角的相对距离
        // 需要此操作的原因是父节点不同锚点的类型 会有不同的结果
        this.relative_position = relative_position.sub_to_self(parent.getWorldPosition().sub(parent.getLeftTopWorldPosition()));
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

    @Override
    public void addCollider(BaseCollider collider) {
        if (WhetherPinned){
            collider.setRelativePosition(collider.getRelativePosition().add(this.getLeftTopWorldPosition().sub(parent.getLeftTopWorldPosition())));
            collider.setOwner(this.parent);
            this.parent.addCollider(collider);
        } else{
            super.addCollider(collider);
        }
    }

    @Override
    protected void checkDrag() {
        if (WhetherPinned){
            this.whetherCanDrag = false;
            this.whetherBeRegisteredCanDrag = false;
            return;
        }
        super.checkDrag();
    }
}
