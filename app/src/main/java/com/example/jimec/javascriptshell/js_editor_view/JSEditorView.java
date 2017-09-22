package com.example.jimec.javascriptshell.js_editor_view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.example.jimec.javascriptshell.HtmlGenerator;
import com.example.jimec.javascriptshell.LineList;

public class JSEditorView extends WebView implements LineList.OnEditListener {

    private HtmlGenerator mHtmlGenerator = new HtmlGenerator();

    public JSEditorView(Context context) {
        super(context);
        init();
    }

    public JSEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JSEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public JSEditorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            getSettings().setDomStorageEnabled(true);
            getSettings().setJavaScriptEnabled(true);
        }
    }

    @Override
    public void onEdit(LineList lines) {
        String html = mHtmlGenerator.generateHtml(lines);
        loadData(html, "text/html; charset=UTF-8", null);
        reload();
    }
}
