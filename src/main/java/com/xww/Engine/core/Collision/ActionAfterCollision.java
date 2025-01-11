package com.xww.Engine.core.Collision;

import com.xww.Engine.core.Component.Component;

public class ActionAfterCollision {
    public static interface CollisionCallBack{
        public abstract ActionAfterCollisionType callBack(CollisionInfo collisionInfo);
    }

    public static enum ActionAfterCollisionType{
        stop, // 停止
        rebound, // 反弹
        die, // 消失
        nullAction, // 不做处理
        physics // 模拟物理
    }

    public static class CollisionInfo {
        private final boolean WhetherCollider;  // 是否发生了碰撞

        private final collisionDirection collisionDirection; // 碰撞的方向
        private final Component colliderComponent; // 发生碰撞的组件

        private final BaseCollider selfCollider; // 发生碰撞的自身碰撞体

        private final BaseCollider otherCollider; // 发生碰撞的其它组件的碰撞体

        public CollisionInfo(boolean WhetherCollider, collisionDirection collisionDirection, Component colliderComponent, BaseCollider selfCollider, BaseCollider otherCollider){
            this.WhetherCollider = WhetherCollider;
            this.collisionDirection = collisionDirection;
            this.colliderComponent = colliderComponent;
            this.selfCollider = selfCollider;
            this.otherCollider = otherCollider;
        }

        public boolean isWhetherCollider() {
            return WhetherCollider;
        }

        public ActionAfterCollision.collisionDirection getCollisionDirection() {
            return collisionDirection;
        }

        public Component getColliderComponent() {
            return colliderComponent;
        }

        public BaseCollider getSelfCollider() {
            return selfCollider;
        }

        public BaseCollider getOtherCollider() {
            return otherCollider;
        }

        public static CollisionInfo NoCollisionInfo(){
            return new CollisionInfo(false, null, null, null, null);
        }
    }

    public static enum collisionDirection{
        // TODO

        // 相对矩形
        RectLeft,
        RectRight,
        RectTop,
        RectBottom;
    }

}
