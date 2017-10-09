package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

import com.example.jimec.javascriptshell.R;

public class ShiftKey extends android.support.v7.widget.AppCompatImageView implements KeyboardKeyConnection {

    public static final int INACTIVE = 0;
    public static final int SHIFT = 1;
    public static final int ALL_CAPS = 2;
    
    private int mStatus = INACTIVE;

    public ShiftKey(Context context) {
        super(context);
        init();
    }

    public ShiftKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShiftKey(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        setOnTouchListener(new TouchListener());
        setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shift));
        setAlpha(KeyboardView.ALPHA_INACTIVE);
    }

    private void setStatus(int status) {
        mStatus = status;
        if (INACTIVE == status) {
            DrawableCompat.setTint(DrawableCompat.wrap(getDrawable()), ContextCompat.getColor(getContext(), R.color.keyColor));
            setAlpha(KeyboardView.ALPHA_INACTIVE);
        } else if (SHIFT == status) {
            DrawableCompat.setTint(DrawableCompat.wrap(getDrawable()), ContextCompat.getColor(getContext(), R.color.keyColor));
            setAlpha(KeyboardView.ALPHA_ACTIVE);
        } else if (ALL_CAPS == status) {
            setAlpha(1.0f);
            DrawableCompat.setTint(DrawableCompat.wrap(getDrawable()), ContextCompat.getColor(getContext(), R.color.secondaryColor));
        }
    }

    @Override
    public void setKeyboard(KeyboardView keyboard) {
    }

    public String process(String input) {
        if (SHIFT == mStatus || ALL_CAPS == mStatus) {
            input = input.toUpperCase();
        }
        if (SHIFT == mStatus) {
            setStatus(INACTIVE);
        }
        return input;
    }
    
    private class TouchListener extends TapListener {

        @Override
        public void onTap() {
            setStatus(mStatus != INACTIVE ? INACTIVE : SHIFT);
        }

        @Override
        public void onLongTap() {
            setStatus(mStatus != ALL_CAPS ? ALL_CAPS : INACTIVE);
        }

    }
}
