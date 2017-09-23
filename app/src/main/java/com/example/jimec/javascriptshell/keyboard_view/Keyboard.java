package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jimec.javascriptshell.code_lines.LineList;

public class Keyboard extends LinearLayout {

    private LineList mLineList;
    private ShiftKey mShiftKey;

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

    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        if (view instanceof ViewGroup && !(view instanceof Key)) {
            ViewGroup group = ((ViewGroup) view);
            for (int i = 0; i < group.getChildCount(); i++) {
                setKeyboardToChildKeys(group.getChildAt(i));
            }
        } else {
            if (view instanceof KeyboardKeyConnection) {
                ((KeyboardKeyConnection) view).setKeyboard(this);
            }
        }
    }

    private void findShiftKey(View view) {
        if (view instanceof ViewGroup && !(view instanceof Key)) {
            ViewGroup group = ((ViewGroup) view);
            for (int i = 0; i < group.getChildCount(); i++) {
                findShiftKey(group.getChildAt(i));
            }
        } else {
            if (view instanceof ShiftKey) {
                mShiftKey = ((ShiftKey) view);
            }
        }
    }

    public LineList getLineList() {
        return mLineList;
    }

    public void setLineList(LineList lineList) {
        mLineList = lineList;
    }

    public void write(String code) {
        mLineList.write(mShiftKey.process(code));
    }

    public void writeEnter() {
        mLineList.writeEnter();
    }

    public void backspace() {
        mLineList.backspace();
    }
    
    public void moveCursorLeft() {
        mLineList.moveCursorLeft();
    }
    
    public void moveCursorRight() {
        mLineList.moveCursorRight();
    }
    
    public void moveCursorUp() {
        mLineList.moveCursorUp();
    }
    
    public void moveCursorDown() {
        mLineList.moveCursorDown();
    }

}
