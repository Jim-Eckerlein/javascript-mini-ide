package com.example.jimec.javascriptshell;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KeyboardButton extends LinearLayout implements View.OnLongClickListener {

    private TextView mPrimary;
    private TextView mSecondary;

    public KeyboardButton(Context context) {
        super(context);
    }

    public KeyboardButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public KeyboardButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public KeyboardButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.keyboard_button, this);
        mPrimary = findViewById(R.id.primary);
        mSecondary = findViewById(R.id.secondary);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KeyboardButton, 0, 0);
        try {
            setPrimaryText(a.getString(R.styleable.KeyboardButton_primary));
            setSecondaryText(a.getString(R.styleable.KeyboardButton_secondary));
        } finally {
            a.recycle();
        }

        setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        System.out.println("SEC: " + getSecondaryText());
        return true;
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
}
