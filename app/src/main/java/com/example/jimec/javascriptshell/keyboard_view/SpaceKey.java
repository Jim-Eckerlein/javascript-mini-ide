package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.example.jimec.javascriptshell.R;

public class SpaceKey extends FrameLayout implements KeyboardKeyConnection {
    
    private KeyboardView mKeyboard;
    private View mSpaceKeyView;

    public SpaceKey(Context context) {
        super(context);
        init();
    }

    public SpaceKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpaceKey(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_key_space, this);
        mSpaceKeyView = findViewById(R.id.space_key_view);
        setOnTouchListener(new SpaceKey.TouchListener());
        mSpaceKeyView.setAlpha(KeyboardView.ALPHA_INACTIVE);
    }

    @Override
    public void setKeyboard(KeyboardView keyboard) {
        mKeyboard = keyboard;
    }
    
    private class TouchListener extends RepeatedTapListener {

        @Override
        public void onTap() {
            mKeyboard.write(" ");
        }

        @Override
        public void onDown() {
            super.onDown();
            mSpaceKeyView.setAlpha(KeyboardView.ALPHA_ACTIVE);
        }

        @Override
        public void onUp() {
            super.onUp();
            mSpaceKeyView.setAlpha(KeyboardView.ALPHA_INACTIVE);
        }
    }
}
