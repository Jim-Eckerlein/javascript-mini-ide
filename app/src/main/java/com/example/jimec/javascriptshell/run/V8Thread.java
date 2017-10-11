package com.example.jimec.javascriptshell.run;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
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
    
            new Handler(Looper.getMainLooper()).post(() -> {
                mConsole.append(log.toString());
            });
        
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
    }
    
}
