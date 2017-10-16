package io.jim_eckerlein.jsshell.keyboard;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;

import io.jim_eckerlein.jsshell.R;

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
        setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.enter));
        post(() -> ((ViewGroup) getParent()).setOnTouchListener(new EnterKeyView.TouchListener()));
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
