package io.jimeckerlein.jsshell.run;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import io.jimeckerlein.jsshell.R;

/**
 * The view containing everything a script execution needs.
 * When launched, a new V8 thread is emitted.
 */
public class V8View extends FrameLayout {

    private TextView mConsole, mErrorViewer, mStatsViewer;
    private V8Thread mV8Thread;
    private V8Joiner mV8Joiner;

    public V8View(@NonNull Context context) {
        super(context);
        init();
    }

    public V8View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public V8View(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_v8, this);
        mConsole = findViewById(R.id.console);
        mErrorViewer = findViewById(R.id.error_viewer);
        mStatsViewer = findViewById(R.id.stats_viewer);

        // Console shall always scroll to bottom when something is written to it:
        ScrollView scrollView = ((ScrollView) mConsole.getParent().getParent());
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(() -> scrollView.post(() -> (scrollView).fullScroll(View.FOCUS_DOWN)));
    }

    public void launchV8(String code) {
        stopV8();
        mV8Thread = new V8Thread(mConsole, code);
        mV8Thread.start();
        mV8Joiner = new V8Joiner(mV8Thread, mErrorViewer, mStatsViewer, getContext());
        mV8Joiner.start();
    }

    /**
     * Terminates V8 engine.
     */
    public void stopV8() {
        if (mV8Joiner != null) {
            mV8Joiner.interrupt();
        }
        if (mV8Thread != null) {
            mV8Thread.forceTermination();
            mV8Thread.interrupt();
        }
    }

    /**
     * Clears any output.
     */
    public void clearOutput() {
        mConsole.setText("");
        mErrorViewer.setText("");
        mStatsViewer.setText("");
    }
}
