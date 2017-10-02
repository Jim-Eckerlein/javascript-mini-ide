package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.example.jimec.javascriptshell.R;

public class SpaceKey extends FrameLayout implements KeyboardKeyConnection {

    private Keyboard mKeyboard;
    private View mSpaceKeyView;
    private Drawable mSpaceKeyViewBackground;

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
        inflate(getContext(), R.layout.space_key, this);
        mSpaceKeyView = findViewById(R.id.space_key_view);
        mSpaceKeyViewBackground = DrawableCompat.wrap(mSpaceKeyView.getBackground());
        
        setOnTouchListener(new SpaceKey.TouchListener());
        DrawableCompat.setTint(mSpaceKeyViewBackground, ContextCompat.getColor(getContext(), R.color.keyInactive));
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
        System.out.println("SET KEYBOARD");
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
            DrawableCompat.setTint(mSpaceKeyViewBackground, ContextCompat.getColor(getContext(), R.color.keyActive));
        }

        @Override
        public void onUp() {
            super.onUp();
            DrawableCompat.setTint(mSpaceKeyViewBackground, ContextCompat.getColor(getContext(), R.color.keyInactive));
        }
    }
}
