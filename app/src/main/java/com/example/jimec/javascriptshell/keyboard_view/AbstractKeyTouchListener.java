package com.example.jimec.javascriptshell.keyboard_view;

import android.view.MotionEvent;
import android.view.View;

public abstract class AbstractKeyTouchListener implements View.OnTouchListener {

    public static final long LONG_TOUCH_MILLIS = 100;
    public static final int TOUCH_TAP_RADIUS = 3;

    private boolean mTouchEnded = false;
    private long mStartTime;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mStartTime = System.currentTimeMillis();
            mTouchEnded = false;
            return true;
        } else if (!mTouchEnded && action == MotionEvent.ACTION_UP) {
            // Initiate short touch event
            onTap();
            mTouchEnded = true;
            return true;
        } else if (!mTouchEnded && action == MotionEvent.ACTION_MOVE
                && System.currentTimeMillis() - mStartTime >= LONG_TOUCH_MILLIS) {
            // Initiate long touch event
            onLongTap();
            mTouchEnded = true;
            return true;
        }

        return false;
    }

    public abstract void onTap();

    public abstract void onLongTap();

}
