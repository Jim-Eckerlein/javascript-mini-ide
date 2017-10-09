package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.example.jimec.javascriptshell.R;

public class BackspaceKey extends android.support.v7.widget.AppCompatImageView implements KeyboardKeyConnection {
    
    private KeyboardView mKeyboard;

    public BackspaceKey(Context context) {
        super(context);
        init();
    }

    public BackspaceKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackspaceKey(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(new BackspaceKey.TouchListener());
        setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.backspace));
        setAlpha(KeyboardView.ALPHA_INACTIVE);
    }

    @Override
    public void setKeyboard(KeyboardView keyboard) {
        mKeyboard = keyboard;
    }
    
    private class TouchListener extends RepeatedTapListener {

        @Override
        public void onTap() {
            mKeyboard.backspace();
        }

        @Override
        public void onDown() {
            super.onDown();
            setAlpha(KeyboardView.ALPHA_ACTIVE);
        }

        @Override
        public void onUp() {
            super.onUp();
            setAlpha(KeyboardView.ALPHA_INACTIVE);
        }

    }
}
