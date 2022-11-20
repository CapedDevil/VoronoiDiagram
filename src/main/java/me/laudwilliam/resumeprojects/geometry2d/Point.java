package me.laudwilliam.resumeprojects.geometry2d;

public class Point {
    private double x;
    private double y;

    // Constructors
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Getters and Setters
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }


    // Overrides

    @Override
    public String toString() {
        return "(" +
                "" + x +
                ", " + y +
                ')';
    }
}
