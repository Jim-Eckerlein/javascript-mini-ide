package com.example.jimec.javascriptshell.keyboard_view;

class Point {
    
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int NONE = 4;
    public double mX, mY;
    
    public void set(double x, double y) {
        mX = x;
        mY = y;
    }
    
    public void set(Point other) {
        mX = other.mX;
        mY = other.mY;
    }
    
    public double distance(Point other) {
        return Math.sqrt(Math.pow(other.mX - mX, 2) + Math.pow(other.mY - mY, 2));
    }
    
    public int getRoughDirection(Point origin) {
        double xDiff = mX - origin.mX;
        double yDiff = mY - origin.mY;
        
        if (xDiff > Math.abs(yDiff)) {
            return RIGHT;
        }
        if (-xDiff > Math.abs(yDiff)) {
            return LEFT;
        }
        if (yDiff > Math.abs(xDiff)) {
            return DOWN;
        }
        if (-yDiff > Math.abs(xDiff)) {
            return UP;
        }
        
        return NONE;
    }
    
}
