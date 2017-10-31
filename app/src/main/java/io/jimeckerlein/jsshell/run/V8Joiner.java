package io.jimeckerlein.jsshell.run;

import android.content.Context;
import android.widget.TextView;

import io.jimeckerlein.jsshell.R;
import io.jimeckerlein.jsshell.Util;

/**
 * A thread waiting for the V8 engine to finish.
 * Prints errors to console.
 */
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
        try {
            // Wait till V8 thread is finished:
            mV8Thread.join();

        } catch (InterruptedException ignored) {
            // Will be interrupted if js execution is interrupted
        }
        mV8Thread.flushConsole();
        if (mV8Thread.isExited()) {
            // Script called exit():
            Util.runOnUiThread(() -> mErrorViewer.setText(mContext.getString(R.string.run_exited, mV8Thread.getExitCause())));
        }
        else if (mV8Thread.hasException()) {
            // Error occurred during execution -> print to error-view:
            Util.runOnUiThread(() -> mErrorViewer.setText(mContext.getString(R.string.run_error, mV8Thread.getException().toString())));
        }
        else {
            // Print execution stats:
            Util.runOnUiThread(() -> mStatsViewer.setText(mContext.getString(R.string.run_stats)));
        }
    }
}
