package com.example.jimec.javascriptshell.keyboard_view;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public abstract class RepeatedTapListener implements View.OnTouchListener {
    
    private static final long INITIAL_DELAY_MILLIS = 450;
    private static final long REPEAT_DELAY_MILLIS = 100;
    
    private Timer mRepeatTimer;
    
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean processed = false;
        int action = event.getAction();
        
        if (MotionEvent.ACTION_DOWN == action) {
            mRepeatTimer = new Timer();
            mRepeatTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            onTap();
                        }
                    });
                }
            }, INITIAL_DELAY_MILLIS, REPEAT_DELAY_MILLIS);
            onDown();
            onTap();
            processed = true;
        }
        
        else if (MotionEvent.ACTION_UP == action) {
            mRepeatTimer.cancel();
            onUp();
            processed = true;
        }
        
        return processed;
    }
    
    public void onTap() {
    }
    
    public void onDown() {
    }
    
    public void onUp() {
    }
    
}
