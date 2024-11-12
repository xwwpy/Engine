package com.xww.Engine.Core;

import com.xww.Engine.Vector.Vector;

public class Transform {
    Vector pos;

    double rotation;
    Vector scale;

    private Transform() {
        pos = Vector.getZero();
        rotation = 0;
        scale = new Vector(1, 1);
    }

    public Transform(Vector pos, double rotation, Vector scale) {
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    public static Transform Identity(){
        return new Transform();
    }
}
