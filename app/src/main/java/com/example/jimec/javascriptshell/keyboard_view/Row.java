package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.jimec.javascriptshell.R;

public class Row extends LinearLayout {

    public Row(Context context) {
        super(context);
    }

    public Row(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Row(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public Row(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.keyboard_row, this);
        setOrientation(HORIZONTAL);
    }
}
