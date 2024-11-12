package com.xww.Engine.Core;

public abstract class Component extends Base{
    protected GameObject owner;
    public abstract void update();

    public void setOwner(GameObject owner) {
        this.owner = owner;
    }

    public abstract void destroy();
}
