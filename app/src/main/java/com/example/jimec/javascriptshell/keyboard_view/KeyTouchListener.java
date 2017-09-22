package com.example.jimec.javascriptshell.keyboard_view;

import android.view.MotionEvent;
import android.view.View;

public class KeyTouchListener implements View.OnTouchListener {

    public static final long LONG_TOUCH_MILLIS = 500;
    public static final int TOUCH_TAP_RADIUS = 3;

    private boolean mTouchEnded = false;
    private long mStartTime;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Key key = (Key) view;
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mStartTime = System.currentTimeMillis();
            mTouchEnded = false;
            return true;
        }

        if (!mTouchEnded && action == MotionEvent.ACTION_MOVE
                && System.currentTimeMillis() - mStartTime >= LONG_TOUCH_MILLIS) {
            // Initiate long touch event
            key.writeSecondaryText();
            mTouchEnded = true;
            return true;
        }

        if (!mTouchEnded && action == MotionEvent.ACTION_UP) {
            // Initiate short touch event
            key.writePrimaryText();
            mTouchEnded = true;
            return true;
        }

        return false;
    }

}
