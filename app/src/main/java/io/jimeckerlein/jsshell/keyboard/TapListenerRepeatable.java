package io.jimeckerlein.jsshell.keyboard;

import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import io.jimeckerlein.jsshell.Util;

public abstract class TapListenerRepeatable implements View.OnTouchListener {
    
    private static final long INITIAL_DELAY_MILLIS = 300;
    private static final long REPEAT_DELAY_MILLIS = 70;

    private Timer mInitiateRepeatTimer;
    private Timer mRepeatTimer;
    private float mStartX, mStartY;
    private boolean mCanceled;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean processed = false;
        int action = event.getAction();
        
        if (MotionEvent.ACTION_DOWN == action) {

            mInitiateRepeatTimer = new Timer();
            mInitiateRepeatTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Util.runOnUiThread(TapListenerRepeatable.this::onTap);
                    mRepeatTimer = new Timer();
                    mRepeatTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            Util.runOnUiThread(TapListenerRepeatable.this::onTap);
                        }
                    }, 0, REPEAT_DELAY_MILLIS);
                }
            }, INITIAL_DELAY_MILLIS);

            mRepeatTimer = null;
            onDown();
            processed = true;
            mStartX = event.getX();
            mStartY = event.getY();
            mCanceled = false;
        }

        // Check for cancelling:
        else if ((MotionEvent.ACTION_MOVE == action && !mCanceled && Math.hypot(event.getX() - mStartX, event.getY() - mStartY) > TapListener.MAX_DISTANCE)
                || (MotionEvent.ACTION_CANCEL == action || MotionEvent.ACTION_OUTSIDE == action)) {
            // Touch move exceeded distance
            mInitiateRepeatTimer.cancel();
            if(null != mRepeatTimer) {
                mRepeatTimer.cancel();
            }
            onUp();
            mCanceled = true;
            processed = true;
        }

        else if (MotionEvent.ACTION_UP == action) {
            mInitiateRepeatTimer.cancel();
            if(null != mRepeatTimer) {
                mRepeatTimer.cancel();
            }
            else {
                Util.runOnUiThread(TapListenerRepeatable.this::onTap);
            }
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
