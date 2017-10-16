package io.jim_eckerlein.jsshell.keyboard;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import io.jim_eckerlein.jsshell.R;

public class BackspaceKeyView extends android.support.v7.widget.AppCompatImageView implements KeyboardKeyConnection {
    
    private KeyboardView mKeyboard;
    
    public BackspaceKeyView(Context context) {
        super(context);
        init();
    }
    
    public BackspaceKeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public BackspaceKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(new BackspaceKeyView.TouchListener());
        setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.backspace));
        setAlpha(KeyboardView.ALPHA_INACTIVE);
    }

    @Override
    public void setKeyboard(KeyboardView keyboard) {
        mKeyboard = keyboard;
    }
    
    private class TouchListener extends TapListenerRepeatable {

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
