package com.xww.Engine.Vector;


import com.xww.Engine.Utils.StringUtils;

public class Vector {
    double x;
    double y;

    public static Vector getZero(){
        return new Vector(0, 0);
    }


    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public static double getDistance(Vector pos1, Vector pos2) {
        return Math.sqrt(Math.pow(pos1.getFullX() - pos2.getFullX(), 2) + Math.pow(pos1.getFullY() - pos2.getFullY(), 2));
    }

    public double getFullX()
    {
        return x;
    }
    public double getFullY()
    {
        return y;
    }

    public int getX(){
        return (int) x;
    }
    public int getY(){
        return (int) y;
    }
    public Vector add(Vector v){
        return new Vector(this.x + v.getFullX(), this.y + v.getFullY());
    }

    public void add_to_self(Vector v){
        this.x += v.getFullX();
        this.y += v.getFullY();
    }

    public Vector sub(Vector position) {
        return new Vector(this.x - position.x, this.y - position.y);
    }

    public void sub_to_self(Vector position) {
        this.x += position.getFullX();
        this.y += position.getFullY();
    }

    public Vector multiply(double d){
        return new Vector(this.x * d, this.y * d);
    }
    public void multiply_to_self(double d){
        this.x *= d;
        this.y *= d;
    }
    public Vector divide(double d){
        if (d == 0) throw new ArithmeticException();

        return new Vector(this.x / d, this.y / d);
    }
    public void divide_to_self(double d){
        this.x /= d;
        this.y /= d;
    }
    public double getLength(){
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    public void self_to_zero(){
        this.x = 0;
        this.y = 0;
    }

    public String toString(){
        return "(" + StringUtils.getTargetAccuracy(this.x, 3) + ", " + StringUtils.getTargetAccuracy(this.y, 3) + ")";
    }

    public Vector clone_self(){
        return new Vector(this.x, this.y);
    }

    public boolean LessThan(Vector v) {
        return Math.abs(this.getFullX()) < Math.abs(v.getFullX()) && Math.abs(this.getFullY()) < Math.abs(v.getFullY());
    }

    public boolean BiggerThan(Vector vector) {
        return Math.abs(this.getFullX()) > Math.abs(vector.getFullX()) || Math.abs(this.getFullY()) > Math.abs(vector.getFullY());
    }

    public boolean EqualTo(Vector tar) {
        return Math.abs(this.getFullX() - tar.getFullX()) < 0.1e-6 && Math.abs(this.getFullY() - tar.getFullY()) < 0.1e-6;
    }

    public double getAngle(){
        return Math.atan2(y, x) * 180 / Math.PI;
    }
    public Vector RotateSelf(double angle, boolean dir){
        double rad = angle * Math.PI / 180; // 转换为弧度
        double curRad = getAngle();
        double len = getLength();
        if (dir) curRad -= rad;
        else curRad += rad;
        x = len * Math.cos(curRad);
        y = len * Math.sin(curRad);
        return this;
    }
}
