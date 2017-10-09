package com.example.jimec.javascriptshell;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class JSEditorView extends LinearLayout {
    
    public JSEditorView(Context context) {
        super(context);
    }
    
    public JSEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public JSEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_js_editor, this);
    }
    
}
