package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jimec.javascriptshell.R;

public class Key extends LinearLayout implements KeyboardKeyConnection {

    private TextView mPrimary;
    private TextView mSecondary;
    private Keyboard mKeyboard;

    public Key(Context context) {
        super(context);
    }

    public Key(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Key(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public Key(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.keyboard_key, this);
        mPrimary = findViewById(R.id.primary);
        mSecondary = findViewById(R.id.secondary);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Key, 0, 0);
        try {
            setPrimaryText(a.getString(R.styleable.Key_primary));
            setSecondaryText(a.getString(R.styleable.Key_secondary));
        } finally {
            a.recycle();
        }

        setOnTouchListener(new TouchListener());
    }

    public String getPrimaryText() {
        return mPrimary.getText().toString();
    }

    public void setPrimaryText(String text) {
        mPrimary.setText(text);
        invalidate();
        requestLayout();
    }

    public String getSecondaryText() {
        return mSecondary.getText().toString();
    }

    public void setSecondaryText(String text) {
        mSecondary.setText(text);
        invalidate();
        requestLayout();
    }

    public void writePrimaryText() {
        mKeyboard.write(getPrimaryText());
    }

    public void writeSecondaryText() {
        mKeyboard.write(getSecondaryText());
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
        mKeyboard = keyboard;
    }
    
    private class TouchListener extends TapListener {

        @Override
        public void onTap() {
            writePrimaryText();
        }

        @Override
        public void onLongTap() {
            if (getSecondaryText() != null && getSecondaryText().length() > 0) {
                writeSecondaryText();
            }
            else {
                writePrimaryText();
            }
        }
    }

}
