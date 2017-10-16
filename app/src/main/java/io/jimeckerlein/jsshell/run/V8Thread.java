package io.jimeckerlein.jsshell.run;

import android.widget.TextView;

import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.utils.V8Executor;
import io.jimeckerlein.jsshell.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A V8 engine with all predefined functions (print, sleep, ...)
 */
public class V8Thread extends V8Executor {
    
    private static final long FLUSH_DELAY_MILLIS = 100;
    private final StringBuilder mConsoleBuffer = new StringBuilder();
    private TextView mConsole;
    private boolean mExited = false;
    private String mExitCause;
    
    public V8Thread(TextView console, String code) {
        super(code);
        mConsole = console;
    
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mConsoleBuffer.length() == 0) {
                    return;
                }
                synchronized (mConsoleBuffer) {
                    String log = mConsoleBuffer.toString();
                    Util.runOnUiThread(() -> mConsole.append(log));
                    mConsoleBuffer.delete(0, mConsoleBuffer.length());
                }
            }
        }, FLUSH_DELAY_MILLIS, FLUSH_DELAY_MILLIS);
    }
    
    @Override
    public void setup(V8 v8) {
        // JavaScript print() function.
        // Prints argument to a TextView.
        v8.registerJavaMethod((receiver, parameters) -> {
            final StringBuilder log = new StringBuilder();
        
            for (int i = 0; i < parameters.length(); i++) {
                final Object param = parameters.get(i);
                if (i > 0) {
                    log.append(' ');
                }
                log.append(param.toString());
                if (param instanceof Releasable) {
                    ((Releasable) param).release();
                }
            }
        
            log.append('\n');
    
            synchronized (mConsoleBuffer) {
                mConsoleBuffer.append(log);
            }
        
        }, "print");
    
        // JavaScript sleep() function.
        // Pauses JavaScript execution, in milliseconds.
        v8.registerJavaMethod((receiver, parameters) -> {
            if (parameters.length() == 1) {
                try {
                    Thread.sleep(parameters.getInteger(0));
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        }, "sleep");
    
        // JavaScript exit() function.
        // Immediately stops script execution with an optional reason.
        v8.registerJavaMethod((JavaVoidCallback) (receiver, parameters) -> {
            final StringBuilder errorReason = new StringBuilder();
        
            for (int i = 0; i < parameters.length(); i++) {
                final Object param = parameters.get(i);
                if (i > 0) {
                    errorReason.append(' ');
                }
                errorReason.append(param.toString());
                if (param instanceof Releasable) {
                    ((Releasable) param).release();
                }
            }
        
            System.err.println("exited");
            mExited = true;
            mExitCause = errorReason.toString();
            throw new RuntimeException(errorReason.toString());
        }, "exit");
    }
    
    public boolean isExited() {
        return mExited;
    }
    
    public String getExitCause() {
        return mExitCause;
    }
}
