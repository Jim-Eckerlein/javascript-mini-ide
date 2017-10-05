package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jimec.javascriptshell.editor.EditorView;

public class Keyboard extends LinearLayout {
    
    private ShiftKey mShiftKey;
    private EditorView mEditor;
    
    public Keyboard(Context context) {
        super(context);
        init();
    }
    
    public Keyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setKeyboardToChildKeys(this);
        findShiftKey(this);
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
        if (view instanceof ViewGroup && !(view instanceof Key)) {
            ViewGroup group = ((ViewGroup) view);
            for (int i = 0; i < group.getChildCount(); i++) {
                findShiftKey(group.getChildAt(i));
            }
        }
        else {
            if (view instanceof ShiftKey) {
                mShiftKey = ((ShiftKey) view);
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
    
    public void setEditor(EditorView editor) {
        mEditor = editor;
    }
}
