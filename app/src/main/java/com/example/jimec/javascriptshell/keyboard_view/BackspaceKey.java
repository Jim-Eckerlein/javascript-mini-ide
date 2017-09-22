package com.example.jimec.javascriptshell.keyboard_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.jimec.javascriptshell.R;

public class BackspaceKey extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener {

    private Keyboard mKeyboard;

    public BackspaceKey(Context context) {
        super(context);
        init();
    }

    public BackspaceKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackspaceKey(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setImageDrawable(getResources().getDrawable(R.drawable.ic_backspace, getContext().getTheme()));
        setOnClickListener(this);
    }

    public void setKeyboard(Keyboard keyboard) {
        mKeyboard = keyboard;
    }

    @Override
    public void onClick(View view) {
        mKeyboard.backspace();
    }
}
