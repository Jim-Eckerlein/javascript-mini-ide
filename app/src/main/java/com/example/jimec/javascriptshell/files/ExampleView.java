package com.example.jimec.javascriptshell.files;

import android.content.Context;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.jimec.javascriptshell.R;
import com.example.jimec.javascriptshell.TabManager;

public class ExampleView extends FrameLayout implements View.OnClickListener {
    
    private TabManager mTabManager;
    private
    @RawRes
    int mRes;
    private TextView mText;
    
    public ExampleView(Context context) {
        super(context);
        init();
    }
    
    public ExampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public ExampleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_example, this);
        setOnClickListener(this);
        mText = findViewById(R.id.example_list_item_title);
    }
    
    @Override
    public void onClick(View v) {
        mTabManager.loadExample(mRes);
    }
    
    public void setTabManager(TabManager tabManager) {
        mTabManager = tabManager;
    }
    
    public void setRes(@RawRes int res) {
        mRes = res;
    }
    
    public void setText(String text) {
        mText.setText(text);
    }
}
