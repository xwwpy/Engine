package com.xww.Engine.core.Animation;

import com.xww.Engine.core.Vector.Vector;

public class Rect {
    private final Vector position; // 矩形左上角坐标

    private final Vector size; // 矩形大小

    public Rect(Vector position, Vector size) {
        this.position = position;
        this.size = size;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSize() {
        return size;
    }
}
