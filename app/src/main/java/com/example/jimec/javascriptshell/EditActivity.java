package com.example.jimec.javascriptshell;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.security.InvalidParameterException;

public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_CODE = "com.example.jimec.javascriptshell.CODE";
    private final LineList mCodeLines = new LineList();
    private final HtmlGenerator mHtmlGenerator = new HtmlGenerator();
    private WebView mEditor;
    private FrameLayout mKeyboardContainer;
    private boolean mShift = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final EditActivity activity = this;
        setContentView(R.layout.activity_edit);
        mKeyboardContainer = (FrameLayout) findViewById(R.id.keyboard_container);

        mEditor = (WebView) findViewById(R.id.editor);
        mEditor.getSettings().setDomStorageEnabled(true);
        mEditor.getSettings().setJavaScriptEnabled(true);

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                cursorRight(null);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                cursorLeft(null);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                cursorUp(null);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                cursorDown(null);
                break;
            default:
                return false;
        }
        return true;
    }

    public void cursorUp(View view) {
        if (mCodeLines.moveCursorUp()) {
            syncText();
        }
    }

    public void cursorDown(View view) {
        if (mCodeLines.moveCursorDown()) {
            syncText();
        }
    }

    public void cursorLeft(View view) {
        if (mCodeLines.moveCursorLeft()) {
            syncText();
        }
    }

    public void cursorRight(View view) {
        if (mCodeLines.moveCursorRight()) {
            syncText();
        }
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

    @SuppressWarnings("unused")
    public void write(View view) {
        mCodeLines.write(((Button) view).getText().toString());
        syncText();
    }

    public void writeSpace(View view) {
        mCodeLines.write(" ");
        syncText();
    }

    public void writeEnter(View view) {
        mCodeLines.writeEnter();
        syncText();
    }

    public void backspace(View view) {
        mCodeLines.backspace();
        syncText();
    }

    public void gotoKeyboard0(View view) {
        mKeyboardContainer.getChildAt(0).setVisibility(View.VISIBLE);
        mKeyboardContainer.getChildAt(1).setVisibility(View.GONE);
        mKeyboardContainer.getChildAt(2).setVisibility(View.GONE);
    }

    public void gotoKeyboard1(View view) {
        mKeyboardContainer.getChildAt(0).setVisibility(View.GONE);
        mKeyboardContainer.getChildAt(1).setVisibility(View.VISIBLE);
        mKeyboardContainer.getChildAt(2).setVisibility(View.GONE);
    }

    public void gotoKeyboardNavigation(View view) {
        mKeyboardContainer.getChildAt(0).setVisibility(View.GONE);
        mKeyboardContainer.getChildAt(1).setVisibility(View.GONE);
        mKeyboardContainer.getChildAt(2).setVisibility(View.VISIBLE);
    }

    public void toggleShift(View view) {
        Button button = (Button) view;
        if (!mShift) {
            // Enable shift
            mShift = true;
            button.setText("UNSHIFT");
        } else {
            // Disable shift
            mShift = false;
            button.setText("SHIFT");
        }
    }

}
