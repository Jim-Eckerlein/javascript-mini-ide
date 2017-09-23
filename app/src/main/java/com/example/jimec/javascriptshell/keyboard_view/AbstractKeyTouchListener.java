package com.example.jimec.javascriptshell.keyboard_view;

import android.view.MotionEvent;
import android.view.View;

public abstract class AbstractKeyTouchListener implements View.OnTouchListener {
    
    private static final long LONG_TOUCH_MILLIS = 150;
    private static final int TOUCH_TAP_RADIUS = 4;
    private static final int DRAG_STEP = 60;
    
    private boolean mLongTapped = false;
    private long mStartTime;
    private boolean mDragging = false;
    private Point mLastPoint = new Point();
    private Point mCurrentPoint = new Point();
    private Point mLastDragPoint = new Point();
    
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean processed = false;
        int action = event.getAction();
        mCurrentPoint.set(event.getX(), event.getY());
    
        if (MotionEvent.ACTION_DOWN == action) {
            mStartTime = System.currentTimeMillis();
            mLongTapped = false;
            mDragging = false;
            mLastPoint.set(mCurrentPoint);
            onDown();
            processed = true;
        }
    
        else if (MotionEvent.ACTION_UP == action) {
            if (!mLongTapped && !mDragging) {
                // Initiate short touch event
                onTap();
                onUp();
                mLongTapped = true;
                processed = true;
            }
            else {
                // Touch up after long touch event was initiated
                onUp();
                processed = true;
            }
        }
    
        else if (MotionEvent.ACTION_MOVE == action) {
            if (!mDragging && mCurrentPoint.distance(mLastPoint) > TOUCH_TAP_RADIUS) {
                // Initiate dragging
                mDragging = true;
                processed = true;
                mLastDragPoint.set(mCurrentPoint);
            }
            else if (mDragging && mCurrentPoint.distance(mLastDragPoint) > DRAG_STEP) {
                switch (mCurrentPoint.getRoughDirection(mLastPoint)) {
                    case Point.UP:
                        onDragUp();
                        break;
                    case Point.RIGHT:
                        onDragRight();
                        break;
                    case Point.DOWN:
                        onDragDown();
                        break;
                    case Point.LEFT:
                        onDragLeft();
                        break;
                }
                mLastDragPoint.set(mCurrentPoint);
                processed = true;
            }
            else if (!mDragging && !mLongTapped && System.currentTimeMillis() - mStartTime >= LONG_TOUCH_MILLIS) {
                // Initiate long touch event
                onLongTap();
                mLongTapped = true;
                processed = true;
            }
        }
    
        mLastPoint.set(mCurrentPoint);
        return processed;
    }
    
    public void onTap() {
    }
    
    public void onLongTap() {
    }
    
    public void onDown() {
    }
    
    public void onUp() {
    }
    
    public void onDragUp() {
    }
    
    public void onDragRight() {
    }
    
    public void onDragDown() {
    }
    
    public void onDragLeft() {
    }
    
}
