package com.example.jimec.javascriptshell.run;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.V8Executor;

/**
 * A V8 engine with all predefined functions (print, sleep, ...)
 */
public class V8Thread extends V8Executor {
    
    private TextView mConsole;
    
    public V8Thread(TextView console, String code) {
        super(code);
        mConsole = console;
    }
    
    @Override
    public void setup(V8 v8) {
        v8.registerJavaMethod(new JsPrintCallback(), "print");
        v8.registerJavaMethod(new JsSleepCallback(), "sleep");
    }
    
    /**
     * JavaScript print() function.
     * Prints argument to a TextView.
     */
    private class JsPrintCallback implements JavaVoidCallback {
        
        @Override
        public void invoke(V8Object receiver, final V8Array parameters) {
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
            
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mConsole.append(log.toString());
                }
            });
        }
    }
    
    /**
     * JavaScript sleep() function.
     * Pauses JavaScript execution, in milliseconds.
     */
    private class JsSleepCallback implements JavaVoidCallback {
        
        @Override
        public void invoke(V8Object receiver, V8Array parameters) {
            if (parameters.length() == 1) {
                try {
                    Thread.sleep(parameters.getInteger(0));
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        }
    }
    
}
