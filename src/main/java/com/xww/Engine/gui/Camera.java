package com.xww.Engine.gui;

import com.xww.Engine.core.Vector.Vector;

public class Camera {
    public static Vector velocity = Vector.Zero();
    public static Vector acceleration = Vector.Zero();
    public static Vector camera_position = new Vector(0, 0);

    public static void updateCamera() {
        velocity = velocity.add(GameFrame.getFrameVelocity(acceleration));
        camera_position = camera_position.add(GameFrame.getFrameVelocity(velocity));
    }

    public static void setPosition(Vector position){
        camera_position = position;
    }
}
