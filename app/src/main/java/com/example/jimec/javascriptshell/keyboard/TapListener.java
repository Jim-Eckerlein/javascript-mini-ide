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
    
    /**
     * Time of a continuous tap interpreted as a long tap
     */
    private static final long LONG_TOUCH_MILLIS = 150;
    
    /**
     * Radius of movement acceptance
     */
    private static final int MAX_DISTANCE = 50;
    
    /**
     * Becomes true as soon as tap is recognized as long tap and therefore processed already
     */
    private boolean mLongTapped = false;
    
    /**
     * Timer is started as soon as the tap occurred.
     * If the touch event is finished before the timer runs out, the gesture is interpreted as a tap,
     * otherwise as a long tap.
     */
    private Timer mLongTapTimer;
    
    /**
     * Start X coordinate
     */
    private float mStartX;
    
    /**
     * Start Y coordinate
     */
    private float mStartY;
    
    /**
     * True as soon as the touch event was canceled from outside
     * or the acceptance radius was exceeded.
     */
    private boolean mCanceled = false;
    
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean processed = false;
        int action = event.getActionMasked();
        
        // Init class members and be ready for a new tap:
        if (MotionEvent.ACTION_DOWN == action) {
            mLongTapped = false;
            mLongTapTimer = new Timer();
            mLongTapTimer.schedule(new LongTapTimer(), LONG_TOUCH_MILLIS);
            mStartX = event.getX();
            mStartY = event.getY();
            mCanceled = false;
            onDown();
            processed = true;
        }

        // Check for cancelling:
        else if ((MotionEvent.ACTION_MOVE == action && !mCanceled && Math.hypot(event.getX() - mStartX, event.getY() - mStartY) > MAX_DISTANCE)
                || (MotionEvent.ACTION_CANCEL == action || MotionEvent.ACTION_OUTSIDE == action)) {
            // Touch move exceeded distance
            mLongTapTimer.cancel();
            onUp();
            mCanceled = true;
            processed = true;
        }

        // Tap ended:
        else if (MotionEvent.ACTION_UP == action && !mCanceled) {
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
    
    /**
     * Called when touch event was interpreted as a tap.
     */
    public void onTap() {
    }
    
    /**
     * Called when touch event was interpreted as a long tap.
     */
    public void onLongTap() {
    }
    
    /**
     * Called every time a touch down event is received.
     */
    public void onDown() {
    }
    
    /**
     * Called when touch finished or cancelled.
     */
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
