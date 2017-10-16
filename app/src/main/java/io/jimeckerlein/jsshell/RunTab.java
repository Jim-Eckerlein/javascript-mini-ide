package io.jimeckerlein.jsshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.jimeckerlein.jsshell.run.V8View;

public class RunTab extends Fragment {
    
    private V8View mV8View;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        mV8View = view.findViewById(R.id.v8);
        return view;
    }
    
    @Override
    public void onStop() {
        super.onStop();
        stopV8();
    }
    
    public void launchV8(String code) {
        mV8View.launchV8(code);
    }
    
    public void stopV8() {
        mV8View.stopV8();
    }
    
    
    public void clearOutput() {
        mV8View.clearOutput();
    }
}
