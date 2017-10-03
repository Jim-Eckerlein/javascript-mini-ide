package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

public class RunActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
    
        // Toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // Retrieve code and generate the HTML, which is than displayed:
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
                "        eval(`" + getIntent().getStringExtra(MainActivity.EXTRA_CODE) + "`);" +
                "    } catch(err) {" +
                "        print(err);" +
                "    }" +
                "</script></body></html>";
        
        WebView console = (WebView) findViewById(R.id.console);
        console.getSettings().setJavaScriptEnabled(true);
        console.loadData(html, "text/html; charset=UTF-8", null);
    }
    
    public void returnToEditor(View view) {
        finish();
    }
    
}
