package com.example.jimec.javascriptshell.keyboard;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.example.jimec.javascriptshell.R;

public class EnterKeyView extends android.support.v7.widget.AppCompatImageView implements KeyboardKeyConnection {
    
    private KeyboardView mKeyboard;
    
    public EnterKeyView(Context context) {
        super(context);
        init();
    }
    
    public EnterKeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public EnterKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(new EnterKeyView.TouchListener());
        setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.enter));
    }

    @Override
    public void setKeyboard(KeyboardView keyboard) {
        mKeyboard = keyboard;
    }
    
    private class TouchListener extends TapListener {

        @Override
        public void onDown() {
            mKeyboard.writeEnter();
        }
    
    }
}
