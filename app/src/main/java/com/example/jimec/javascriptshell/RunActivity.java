package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jimec.javascriptshell.run.Console;
import com.example.jimec.javascriptshell.run.V8Joiner;
import com.example.jimec.javascriptshell.run.V8Thread;

public class RunActivity extends AppCompatActivity {
    
    private Console mConsole;
    private String mCode;
    private V8Thread mV8Thread;
    private V8Joiner mV8Joiner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
    
        mCode = getIntent().getStringExtra(MainActivity.EXTRA_CODE);
        mConsole = (Console) findViewById(R.id.console);
        
        // Toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    
        // V8 Engine:
        mV8Thread = new V8Thread(mConsole, mCode);
        mV8Thread.start();
        mV8Joiner = new V8Joiner(mV8Thread, (TextView) findViewById(R.id.error_viewer));
        mV8Joiner.start();
    }
    
    public void returnToEditor(View view) {
        mV8Thread.forceTermination();
        finish();
    }
    
}
