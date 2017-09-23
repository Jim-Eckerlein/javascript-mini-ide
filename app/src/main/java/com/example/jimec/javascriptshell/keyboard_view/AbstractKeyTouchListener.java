package com.example.jimec.javascriptshell.keyboard_view;

import android.view.MotionEvent;
import android.view.View;

public abstract class AbstractKeyTouchListener implements View.OnTouchListener {

    private static final long LONG_TOUCH_MILLIS = 200;
    private static final int TOUCH_TAP_RADIUS = 3;

    private boolean mTouchEnded = false;
    private long mStartTime;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mStartTime = System.currentTimeMillis();
            mTouchEnded = false;
            onDown();
            return true;
        } else if (!mTouchEnded && action == MotionEvent.ACTION_UP) {
            // Initiate short touch event
            onTap();
            onUp();
            mTouchEnded = true;
            return true;
        } else if (!mTouchEnded && action == MotionEvent.ACTION_MOVE
                && System.currentTimeMillis() - mStartTime >= LONG_TOUCH_MILLIS) {
            // Initiate long touch event
            onLongTap();
            onUp();
            mTouchEnded = true;
            return true;
        }

        return false;
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
