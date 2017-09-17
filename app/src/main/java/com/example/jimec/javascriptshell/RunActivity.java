package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

public class RunActivity extends AppCompatActivity {

    WebView mConsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        mConsole = (WebView) findViewById(R.id.console);
        mConsole.getSettings().setJavaScriptEnabled(true);

        String script = "<html><head></head><body>"
                + Util.readTextFile(this, R.raw.run)
                + "<script>"
                + getIntent().getStringExtra(EditActivity.EXTRA_CODE)
                + "</script></body></html>";

        mConsole.loadData(script, "text/html; charset=UTF-8", null);
    }

    public void quit(View view) {
        finish();
    }
}
