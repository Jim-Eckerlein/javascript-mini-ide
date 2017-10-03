package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jimec.javascriptshell.run.V8Joiner;
import com.example.jimec.javascriptshell.run.V8Thread;

public class RunTab extends Fragment {
    
    public static final String TITLE = "Run";
    private TextView mConsole;
    private V8Thread mV8Thread;
    private V8Joiner mV8Joiner;
    private TextView mErrorViewer;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        
        mConsole = view.findViewById(R.id.console);
        mErrorViewer = view.findViewById(R.id.error_viewer);
        
        return view;
    }
    
    @Override
    public void onStop() {
        super.onStop();
        stopV8();
    }
    
    public void launchV8(String code) {
        stopV8();
        mV8Thread = new V8Thread(mConsole, code);
        mV8Thread.start();
        mV8Joiner = new V8Joiner(mV8Thread, mErrorViewer);
        mV8Joiner.start();
    }
    
    // Terminate V8 engine:
    public void stopV8() {
        if (mV8Joiner != null) {
            mV8Joiner.interrupt();
        }
        if (mV8Thread != null) {
            mV8Thread.forceTermination();
            mV8Thread.interrupt();
        }
    }
    
    
    public void clearOutput() {
        mConsole.setText("");
        mErrorViewer.setText("");
    }
}
