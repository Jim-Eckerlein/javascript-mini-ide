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

        String html = "<!doctype html><html><head><style>" +
                HtmlGenerator.CSS +
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

        System.out.println("CODE:");
        System.out.println(html);

        mConsole.loadData(html, "text/html; charset=UTF-8", null);
    }

    public void quit(View view) {
        finish();
    }
}
