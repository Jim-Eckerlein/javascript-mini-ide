package io.jimeckerlein.jsshell.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import io.jimeckerlein.jsshell.R;

public class SpaceKeyView extends FrameLayout implements KeyboardKeyConnection {
    
    private KeyboardView mKeyboard;
    private View mSpaceKeyView;
    
    public SpaceKeyView(Context context) {
        super(context);
        init();
    }
    
    public SpaceKeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public SpaceKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_key_space, this);
        mSpaceKeyView = findViewById(R.id.space_key_view);
        setOnTouchListener(new SpaceKeyView.TouchListener());
        mSpaceKeyView.setAlpha(KeyboardView.ALPHA_INACTIVE);
    }

    @Override
    public void setKeyboard(KeyboardView keyboard) {
        mKeyboard = keyboard;
    }
    
    private class TouchListener extends TapListenerRepeatable {

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
