package com.example.jimec.javascriptshell.keyboard_view;

import android.view.MotionEvent;
import android.view.View;

public abstract class AbstractKeyTouchListener implements View.OnTouchListener {
    
    // Time of a continuous tap interpreted as a long tap
    private static final long LONG_TOUCH_MILLIS = 150;
    
    // Then the touch move  exceeds this radius, it is interpreted as a drag
    private static final int TOUCH_TAP_RADIUS = 5;
    
    // The move distance interpreted as a single horizontal drag step
    private static final int DRAG_STEP_HORIZONTAL = 50;
    
    // The move distance interpreted as a single vertical drag step
    private static final int DRAG_STEP_VERTICAL = 100;
    
    // Becomes true as soon as tap is recognized as long tap and therefore processed already
    private boolean mLongTapped = false;
    
    // Time point of touch down event
    private long mStartTime;
    
    // Becomes  true as soon as tap is recognized as a drag
    private boolean mDragging = false;
    
    // Last touch point
    private Point mLastPoint = new Point();
    
    // Current touch point
    private Point mCurrentPoint = new Point();
    
    // Last touch point which was interpreted as a drag step
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
            
            if (!mLongTapped && !mDragging && mCurrentPoint.distance(mLastPoint) > TOUCH_TAP_RADIUS) {
                // Initiate dragging
                mDragging = true;
                processed = true;
                mLastDragPoint.set(mCurrentPoint);
            }

            else if (mDragging) {
                
                int direction = mCurrentPoint.getRoughDirection(mLastDragPoint);
                int distance = (int) mCurrentPoint.distance(mLastDragPoint);
                boolean consumed = false;
    
                if (Point.RIGHT == direction && distance > DRAG_STEP_HORIZONTAL) {
                    onDragRight();
                    consumed = true;
                }
    
                else if (Point.LEFT == direction && distance > DRAG_STEP_HORIZONTAL) {
                    onDragLeft();
                    consumed = true;
                }
    
                else if (Point.UP == direction && distance > DRAG_STEP_VERTICAL) {
                    onDragUp();
                    consumed = true;
                }
    
                else if (Point.DOWN == direction && distance > DRAG_STEP_VERTICAL) {
                    onDragDown();
                    consumed = true;
                }
    
                if (consumed) {
                    mLastDragPoint.set(mCurrentPoint);
                    processed = true;
                }
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
