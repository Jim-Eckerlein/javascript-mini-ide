package com.example.jimec.javascriptshell;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.jimec.javascriptshell.keyboard_view.Keyboard;

import java.security.InvalidParameterException;

public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_CODE = "com.example.jimec.javascriptshell.CODE";
    private final LineList mCodeLines = new LineList();
    private final HtmlGenerator mHtmlGenerator = new HtmlGenerator();
    private WebView mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final EditActivity activity = this;
        setContentView(R.layout.activity_edit);

        mEditor = (WebView) findViewById(R.id.editor);
        mEditor.getSettings().setDomStorageEnabled(true);
        mEditor.getSettings().setJavaScriptEnabled(true);

        Keyboard keyboard = (Keyboard) findViewById(R.id.keyboard);
        keyboard.setLineList(mCodeLines);

        ((Spinner) findViewById(R.id.examples_spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mCodeLines.clear();
                int script;
                switch (pos) {
                    case 0:
                        script = R.raw.demo_example;
                        break;
                    case 1:
                        script = R.raw.print_example;
                        break;
                    case 2:
                        script = R.raw.array_example;
                        break;
                    case 3:
                        script = R.raw.object_example;
                        break;
                    case 4:
                        script = R.raw.function_example;
                        break;
                    case 5:
                        script = R.raw.time_example;
                        break;
                    case 6:
                        script = R.raw.typeof_example;
                        break;
                    default:
                        throw new InvalidParameterException("Unknown example at position " + pos);
                }
                mCodeLines.write(Util.readTextFile(activity, script));
                mCodeLines.getCursor().reset();
                syncText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Take the text of <code>mInterText</code> transforming it into an HTML representation,
     * considering syntax highlighting.
     */
    private void syncText() {
        String html = mHtmlGenerator.generateHtml(mCodeLines);
        System.out.println(html);
        mEditor.loadData(html, "text/html; charset=UTF-8", null);
        mEditor.reload();
    }

    public void clearCode(View view) {
        mCodeLines.clear();
        syncText();
    }

    public void run(View view) {
        Intent intent = new Intent(this, RunActivity.class);
        intent.putExtra(EXTRA_CODE, mCodeLines.toStringWithoutComments());
        startActivity(intent);
    }

}
