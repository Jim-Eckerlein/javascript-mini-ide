package com.example.jimec.javascriptshell.keyboard_view;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractKeyTouchListener implements View.OnTouchListener {
    
    // Time of a continuous tap interpreted as a long tap
    private static final long LONG_TOUCH_MILLIS = 170;
    
    // Becomes true as soon as tap is recognized as long tap and therefore processed already
    private boolean mLongTapped = false;
    
    private Timer mLongTapTimer;
    
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean processed = false;
        int action = event.getAction();
    
        if (MotionEvent.ACTION_DOWN == action) {
            // Put listener into initial state:
            onDown();
            processed = true;
            mLongTapped = false;
            mLongTapTimer = new Timer();
            mLongTapTimer.schedule(new LongTapTimer(), LONG_TOUCH_MILLIS);
        }
    
        else if (MotionEvent.ACTION_UP == action) {
            if (!mLongTapped) {
                // Initiate short touch event
                onTap();
                mLongTapTimer.cancel();
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
            synchronized (AbstractKeyTouchListener.this) {
                mLongTapped = true;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onLongTap();
                    }
                });
            }
        }
    }
    
}
