package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

public class RunActivity extends AppCompatActivity {

    WebView mConsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        mConsole = (WebView) findViewById(R.id.console);
        mConsole.getSettings().setJavaScriptEnabled(true);
    
        // Toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String html = "<!doctype html><html><head><style>" +
                Util.readTextFile(getApplication(), R.raw.html_style) +
                "</style></head><body>" +
                "<script>\n" +
                "  function print(message) {\n" +
                "    var p = document.createElement('p');\n" +
                "    p.appendChild(document.createTextNode(message));\n" +
                "    document.getElementById('console').appendChild(p);\n" +
                "  }\n" +
                "\n" +
                "</script>\n" +
                "\n" +
                "<div id=\"console\"></div>" +
                "<script>" +
                "    'use strict';" +
                "    try {" +
                "        eval(`" + getIntent().getStringExtra(EditActivity.EXTRA_CODE) + "`);" +
                "    } catch(err) {" +
                "        print(err);" +
                "    }" +
                "</script></body></html>";

        mConsole.loadData(html, "text/html; charset=UTF-8", null);
    }
    
}
