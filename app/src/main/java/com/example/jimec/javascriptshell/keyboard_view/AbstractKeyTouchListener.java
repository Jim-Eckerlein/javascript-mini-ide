package com.example.jimec.javascriptshell.keyboard_view;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class implements the logic to listen to touch events and interpreting them.
 */
public abstract class AbstractKeyTouchListener implements View.OnTouchListener {
    
    // Time of a continuous tap interpreted as a long tap
    private static final long LONG_TOUCH_MILLIS = 150;
    
    // Becomes true as soon as tap is recognized as long tap and therefore processed already
    private boolean mLongTapped = false;
    
    private Timer mLongTapTimer;
    
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean processed = false;
        int action = event.getAction();
    
        if (MotionEvent.ACTION_DOWN == action) {
            init();
            onDown();
            processed = true;
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
    
    private void init() {
        mLongTapped = false;
        mLongTapTimer = new Timer();
        mLongTapTimer.schedule(new LongTapTimer(), LONG_TOUCH_MILLIS);
    }
    
    public void onTap() {
    }
    
    public void onLongTap() {
    }
    
    public void onDown() {
    }
    
    public void onUp() {
    }
    
    public static class Repeatable extends AbstractKeyTouchListener {
    
        private static final long REPEAT_DELAY_MILLIS = 100;
    
        private Timer mRepeatTimer;
    
        @Override
        public void onUp() {
            super.onUp();
            if (mRepeatTimer != null) {
                System.out.println("cancel repeater");
                mRepeatTimer.cancel();
            }
        }
    
        @Override
        public void onDown() {
            super.onDown();
            onTap();
        }
    
        @Override
        public void onLongTap() {
            super.onLongTap();
            mRepeatTimer = new Timer();
            mRepeatTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("repeated tap");
                            onTap();
                        }
                    });
                }
            }, REPEAT_DELAY_MILLIS, REPEAT_DELAY_MILLIS);
        }
    }
    
    private class LongTapTimer extends TimerTask {
        
        @Override
        public void run() {
            synchronized (AbstractKeyTouchListener.this) {
                System.out.println("start long tap");
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
