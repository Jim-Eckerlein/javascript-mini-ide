package com.example.jimec.javascriptshell.run;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class Console extends android.support.v7.widget.AppCompatTextView {
    
    public Console(Context context) {
        super(context);
    }
    
    public Console(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public Console(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public void println(Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                append(" ");
            }
            append(args[i].toString());
        }
        append("\n");
    }
    
}
