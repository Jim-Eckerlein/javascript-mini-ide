package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jimec.javascriptshell.LineList;
import com.example.jimec.javascriptshell.R;

public class Keyboard extends LinearLayout {

    private LineList mLineList;

    public Keyboard(Context context) {
        super(context);
    }

    public Keyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.keyboard_keyboard, this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setKeyboardToChildKeys(this);
    }

    private void setKeyboardToChildKeys(View view) {
        if (view instanceof ViewGroup && !(view instanceof Key)) {
            ViewGroup group = ((ViewGroup) view);
            for (int i = 0; i < group.getChildCount(); i++) {
                setKeyboardToChildKeys(group.getChildAt(i));
            }
        } else {
            if (view instanceof Key) {
                Key key = ((Key) view);
                key.setKeyboard(this);
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
        mLineList.write(code);
    }

    public void writeEnter() {
        mLineList.writeEnter();
    }
}
