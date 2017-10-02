package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

import com.example.jimec.javascriptshell.R;

public class BackspaceKey extends android.support.v7.widget.AppCompatImageView implements KeyboardKeyConnection {

    private Keyboard mKeyboard;

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
        DrawableCompat.setTint(DrawableCompat.wrap(getDrawable()), ContextCompat.getColor(getContext(), R.color.keyActive));
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
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
            DrawableCompat.setTint(DrawableCompat.wrap(getDrawable()), ContextCompat.getColor(getContext(), R.color.keyActive));
        }

        @Override
        public void onUp() {
            super.onUp();
            DrawableCompat.setTint(DrawableCompat.wrap(getDrawable()), ContextCompat.getColor(getContext(), R.color.keyInactive));
        }

    }
}
