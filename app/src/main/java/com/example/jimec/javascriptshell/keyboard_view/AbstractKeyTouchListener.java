package com.example.jimec.javascriptshell.keyboard_view;

import android.view.MotionEvent;
import android.view.View;

public abstract class AbstractKeyTouchListener implements View.OnTouchListener {
    
    // Time of a continuous tap interpreted as a long tap
    private static final long LONG_TOUCH_MILLIS = 170;
    
    // Becomes true as soon as tap is recognized as long tap and therefore processed already
    private boolean mLongTapped = false;
    
    // Time point of touch down event
    private long mStartTime;
    
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean processed = false;
        int action = event.getAction();
    
        if (MotionEvent.ACTION_DOWN == action) {
            mStartTime = System.currentTimeMillis();
            onDown();
            processed = true;
            mLongTapped = false;
        }
    
        else if (MotionEvent.ACTION_UP == action) {
            if (!mLongTapped) {
                // Initiate short touch event
                onTap();
                onUp();
                processed = true;
            }
            else {
                // Touch up after long touch event was initiated
                onUp();
                processed = true;
            }
        }
    
        else if (MotionEvent.ACTION_MOVE == action) {
            if (System.currentTimeMillis() - mStartTime >= LONG_TOUCH_MILLIS) {
                // Initiate long touch event
                onLongTap();
                mLongTapped = true;
                processed = true;
            }
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
    
}
