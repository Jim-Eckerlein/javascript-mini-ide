package com.example.jimec.javascriptshell.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.jimec.javascriptshell.R;
import com.example.jimec.javascriptshell.Util;
import com.example.jimec.javascriptshell.editor.HighlighterEditText;

public class KeyboardView extends FrameLayout {
    
    public static float ALPHA_INACTIVE = 0.4f;
    public static float ALPHA_ACTIVE = 0.8f;
    private final HideKey mHideKey = new HideKey();
    private final ShowKey mShowKey = new ShowKey();
    private ShiftKey mShiftKey;
    private HighlighterEditText mEditor;
    private ViewGroup mInputKeyboard;
    
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
        setKeyboardToChildKeys(this);
        findShiftKey(this);
        mInputKeyboard = findViewById(R.id.input_keyboard);
        findViewById(R.id.hide_keyboard).setOnClickListener(mHideKey);
        findViewById(R.id.keyboard_show_marker).setOnClickListener(mShowKey);
    }
    
    private void setKeyboardToChildKeys(View view) {
        if (view instanceof KeyboardKeyConnection) {
            ((KeyboardKeyConnection) view).setKeyboard(this);
        }
        else if (view instanceof ViewGroup) {
            ViewGroup group = ((ViewGroup) view);
            for (int i = 0; i < group.getChildCount(); i++) {
                setKeyboardToChildKeys(group.getChildAt(i));
            }
        }
    }
    
    private void findShiftKey(View view) {
        if (view instanceof ShiftKey) {
            mShiftKey = ((ShiftKey) view);
        }
        if (view instanceof ViewGroup && !(view instanceof Key)) {
            ViewGroup group = ((ViewGroup) view);
            for (int i = 0; i < group.getChildCount(); i++) {
                findShiftKey(group.getChildAt(i));
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
    
    private class HideKey implements OnClickListener {
        
        @Override
        public void onClick(View v) {
            if (mInputKeyboard.getAnimation() == null
                    || (mInputKeyboard.getAnimation() != null && mInputKeyboard.getAnimation().hasEnded())) {
                mInputKeyboard.animate().y(getHeight()).setDuration(Util.ANIMATION_DURATION);
            }
        }
        
    }
    
    private class ShowKey implements OnClickListener {
        
        @Override
        public void onClick(View v) {
            if (mInputKeyboard.getAnimation() == null
                    || (mInputKeyboard.getAnimation() != null && mInputKeyboard.getAnimation().hasEnded())) {
                mInputKeyboard.animate().y(0).setDuration(Util.ANIMATION_DURATION);
            }
        }
        
    }
}
