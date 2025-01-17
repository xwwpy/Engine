package com.xww.Engine.core.Vector;

import com.xww.Engine.Utils.StringUtils;

import java.util.Random;

public class Vector {
    public static Random random = new Random();
    public double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector build(double x, double y) {
        return new Vector(x, y);
    }

    public static Vector Zero() {
        return Vector.build(0, 0);
    }

    public static Vector random(int i, int i1, int i2, int i3) {
        return Vector.build(random.nextInt(i, i1), random.nextInt(i2, i3));
    }

    public Vector add(Vector v) {
        return new Vector(this.x + v.getFullX(), this.y + v.getFullY());
    }

    public Vector add_to_self(Vector v) {
        this.x += v.getFullX();
        this.y += v.getFullY();
        return this;
    }
    public Vector sub(Vector v) {
        return new Vector(this.x - v.getFullX(), this.y - v.getFullY());
    }

    public Vector sub_to_self(Vector v) {
        this.x -= v.getFullX();
        this.y -= v.getFullY();
        return this;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector normalize() {
        double len = length();
        return new Vector(x / len, y / len);
    }

    public Vector normalize_self() {
        double len = length();
        this.x /= len;
        this.y /= len;
        return this;
    }

    public Vector scale(double s) {
        return new Vector(x * s, y * s);
    }

    public Vector scale_self(double s) {
        this.x *= s;
        this.y *= s;
        return this;
    }

    public Vector rotate(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector(x * cos - y * sin, x * sin + y * cos);
    }

    public Vector rotate_self(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        x = x * cos - y * sin;
        y = x * sin + y * cos;
        return this;
    }
    public Vector copy() {
        return new Vector(x, y);
    }

    public Vector dot(Vector other) {
        return new Vector(x * other.getFullX() + y * other.getFullY(), x * other.getFullY() - y * other.getFullX());
    }


    public double getFullX() {
        return x;
    }

    public int getX(){
        return (int) x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getFullY() {
        return y;
    }
    public int getY(){
        return (int) y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector divide(double d) {
        return Vector.build(x / d, y / d);
    }

    public Vector divide_self(double d) {
        x /= d;
        y /= d;
        return this;
    }

    public double distance(Vector vector) {
        return Math.sqrt(Math.pow(vector.x - this.x, 2) + Math.pow(vector.y - this.y, 2));
    }

    @Override
    public String toString() {
        return "{ x: " + StringUtils.getTargetAccuracy(x, 3) + ", y: " + StringUtils.getTargetAccuracy(y, 3) + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector v){
            // 1*10e-6的允许误差
            return !(Math.abs(v.x - x) > 1 * 10e-6) && !(Math.abs(v.y - y) > 1 * 10e-6);
        }
        return super.equals(obj);
    }

    public Vector mul(double mulNum) {
        return Vector.build(this.x * mulNum, this.y * mulNum);
    }

    public Vector mul_to_self(double mulNum) {
        this.x *=  mulNum;
        this.y *= mulNum;
        return this;
    }
}
