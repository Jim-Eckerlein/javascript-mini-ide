package io.jim_eckerlein.jsshell.keyboard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.jim_eckerlein.jsshell.R;

public class KeyIndicatorView extends LinearLayout {
    
    private TextView mPrimary;
    private ViewGroup mRoot;
    
    public KeyIndicatorView(Context context) {
        super(context);
        init();
    }
    
    public KeyIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public KeyIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_key_indicator, this);
        mPrimary = findViewById(R.id.key_indicator_primary);
        mRoot = (ViewGroup) getChildAt(0);
    }
    
    public void show(KeyView target, String primaryText) {
        mRoot.setVisibility(VISIBLE);
        mPrimary.setText(primaryText);
        setX(target.getX());
        setY(((LinearLayout) target.getParent()).getY() + target.getHeight() - mRoot.getHeight());
        getLayoutParams().width = target.getWidth() + target.getPaddingLeft() + target.getPaddingRight();
        requestLayout();
    }
    
    public void hide() {
        mRoot.setVisibility(GONE);
    }
}
