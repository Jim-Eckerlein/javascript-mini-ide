package com.example.jimec.javascriptshell.run;

import android.content.Context;
import android.widget.TextView;

import com.example.jimec.javascriptshell.R;
import com.example.jimec.javascriptshell.Util;

public class V8Joiner extends Thread {
    
    private final V8Thread mV8Thread;
    private final TextView mErrorViewer, mStatsViewer;
    private final Context mContext;
    
    public V8Joiner(V8Thread v8Thread, TextView errorViewer, TextView statsViewer, Context context) {
        super("V8-JOINER");
        mV8Thread = v8Thread;
        mErrorViewer = errorViewer;
        mStatsViewer = statsViewer;
        mContext = context;
    }
    
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        
        try {
            // Wait till V8 thread is finished:
            mV8Thread.join();
    
            // Print execution stats:
            long time = System.currentTimeMillis() - start;
            Util.runOnUiThread(() -> mStatsViewer.setText(mContext.getString(R.string.run_stats, ((double) time) / 1000.0)));
            
        } catch (InterruptedException e) {
            // Will be interrupted if js  execution is interrupted
        }
        if (mV8Thread.hasException()) {
            Util.runOnUiThread(() -> mErrorViewer.setText(mContext.getString(R.string.run_error, mV8Thread.getException().toString())));
        }
    }
}
