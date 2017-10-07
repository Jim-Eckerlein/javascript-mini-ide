package com.example.jimec.javascriptshell.run;

import android.widget.TextView;

import com.example.jimec.javascriptshell.Util;

public class V8Joiner extends Thread {
    
    private final V8Thread mV8Thread;
    private final TextView mErrorViewer;
    
    public V8Joiner(V8Thread v8Thread, TextView errorViewer) {
        super("V8-JOINER");
        mV8Thread = v8Thread;
        mErrorViewer = errorViewer;
    }
    
    @Override
    public void run() {
        super.run();
        try {
            mV8Thread.join();
        } catch (InterruptedException e) {
            // Will be interrupted if js  execution is interrupted
        }
        if (mV8Thread.hasException()) {
            Util.runOnUiThread(() -> mErrorViewer.setText("Error on running JavaScript:\n" + mV8Thread.getException().toString()));
        }
    }
}
