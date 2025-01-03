package com.xww.NewEngine.core.Collision;

import com.xww.NewEngine.core.Base;

public class ActionAfterCollision {
    public static interface CollisionCallBack{
        public abstract ActionAfterCollisionType callBack(Base obj);
    }

    public static enum ActionAfterCollisionType{
        stop, // 停止
        rebound, // 反弹
        die, // 消失
    }
}
