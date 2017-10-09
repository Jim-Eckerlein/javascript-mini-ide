package com.example.jimec.javascriptshell.keyboard;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class implements the logic to listen to touch events and interpreting them.
 */
public abstract class TapListener implements View.OnTouchListener {
    
    // Time of a continuous tap interpreted as a long tap
    private static final long LONG_TOUCH_MILLIS = 150;
    
    private static final int MAX_DISTANCE = 50;
    
    // Becomes true as soon as tap is recognized as long tap and therefore processed already
    private boolean mLongTapped = false;
    
    private Timer mLongTapTimer;
    
    private float mStartX;
    private float mStartY;
    private boolean mExceededDistance = false;
    
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean processed = false;
        int action = event.getAction();
    
        if (MotionEvent.ACTION_DOWN == action) {
            mLongTapped = false;
            mLongTapTimer = new Timer();
            mLongTapTimer.schedule(new LongTapTimer(), LONG_TOUCH_MILLIS);
            mStartX = event.getX();
            mStartY = event.getY();
            mExceededDistance = false;
            onDown();
            processed = true;
        }

        else if (MotionEvent.ACTION_MOVE == action && !mExceededDistance) {
            if (Math.hypot(event.getX() - mStartX, event.getY() - mStartY) > MAX_DISTANCE) {
                // Touch move exceeded distance
                mExceededDistance = true;
                mLongTapTimer.cancel();
            }
        }

        else if (MotionEvent.ACTION_UP == action && !mExceededDistance) {
            mLongTapTimer.cancel();
            if (!mLongTapped) {
                // Initiate short touch event
                onTap();
            }
            onUp();
            processed = true;
        }
    
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
    
    private class LongTapTimer extends TimerTask {
        
        @Override
        public void run() {
            synchronized (TapListener.this) {
                mLongTapped = true;
                new Handler(Looper.getMainLooper()).post(TapListener.this::onLongTap);
            }
        }
    }
    
}
