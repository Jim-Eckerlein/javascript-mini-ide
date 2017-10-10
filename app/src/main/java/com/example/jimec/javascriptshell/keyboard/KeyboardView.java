package com.example.jimec.javascriptshell.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import com.example.jimec.javascriptshell.R;
import com.example.jimec.javascriptshell.Util;
import com.example.jimec.javascriptshell.editor.HighlighterEditText;

public class KeyboardView extends FrameLayout {
    
    public static float ALPHA_INACTIVE = 0.4f;
    public static float ALPHA_ACTIVE = 0.8f;
    private ShiftKeyView mShiftKey;
    private HighlighterEditText mEditor;
    private ViewGroup mInputKeyboard;
    private KeyIndicatorView mKeyIndicatorView;
    private int mOldKeyboardHeight;
    
    public KeyboardView(Context context) {
        super(context);
        init();
    }
    
    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_keyboard, this);
    
        mInputKeyboard = findViewById(R.id.input_keyboard);
        mKeyIndicatorView = findViewById(R.id.key_indicator);
    
        initKeys(this);
    
        // Hide keyboard animation:
        findViewById(R.id.hide_keyboard).setOnClickListener(v -> {
            mOldKeyboardHeight = mInputKeyboard.getHeight();
            ResizeHeightAnimation anim = new ResizeHeightAnimation(mInputKeyboard, 0);
            anim.setDuration(Util.ANIMATION_DURATION);
            mInputKeyboard.startAnimation(anim);
        });
    
        // Show keyboard animation:
        findViewById(R.id.keyboard_show_marker).setOnClickListener(v -> {
            ResizeHeightAnimation anim = new ResizeHeightAnimation(mInputKeyboard, mOldKeyboardHeight);
            anim.setDuration(Util.ANIMATION_DURATION);
            mInputKeyboard.startAnimation(anim);
        });
    }
    
    private void initKeys(View view) {
        if (view instanceof ShiftKeyView) {
            mShiftKey = ((ShiftKeyView) view);
        }
        if (view instanceof KeyboardKeyConnection) {
            ((KeyboardKeyConnection) view).setKeyboard(this);
    
            if (view instanceof KeyView) {
                ((KeyView) view).setKeyIndicatorView(mKeyIndicatorView);
            }
        }
        else if (view instanceof ViewGroup) {
            ViewGroup group = ((ViewGroup) view);
            for (int i = 0; i < group.getChildCount(); i++) {
                initKeys(group.getChildAt(i));
            }
        }
    }
    
    public void write(String code) {
        mEditor.write(mShiftKey.process(code));
    }
    
    public void writeEnter() {
        mEditor.write("\n");
    }
    
    public void backspace() {
        mEditor.backspace();
    }
    
    public void setEditor(HighlighterEditText editor) {
        mEditor = editor;
    }
    
    public class ResizeHeightAnimation extends Animation {
        final View mView;
        final int mStartHeight;
        final float mEndHeight;
        
        public ResizeHeightAnimation(View view, float endHeight) {
            this.mView = view;
            this.mEndHeight = endHeight;
            this.mStartHeight = view.getHeight();
        }
        
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mView.getLayoutParams().height = (int) (mStartHeight + (mEndHeight - mStartHeight) * interpolatedTime);
            mView.requestLayout();
        }
        
        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }
        
        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
