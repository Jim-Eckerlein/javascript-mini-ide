package com.example.jimec.javascriptshell;

import android.content.Context;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;

public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_CODE = "com.example.jimec.javascriptshell.CODE";
    private static final int INDENTATION_LENGTH = 4;
    private WebView mEditor;
    private FrameLayout mKeyboardContainer;
    private boolean mShift = false;
    private StringBuilder mInternalText = new StringBuilder();
    private LineList.Cursor mCursor = new LineList.Cursor();
    private int mDesiredCursorCol = 0;

    public static String readTextFile(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder text = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return text.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final EditActivity activity = this;
        setContentView(R.layout.activity_edit);
        mEditor = (WebView) findViewById(R.id.editor);
        mKeyboardContainer = (FrameLayout) findViewById(R.id.keyboard_container);

        ((Spinner) findViewById(R.id.examples_spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mInternalText.delete(0, mInternalText.length());
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
                mInternalText.append(readTextFile(activity, script));
                mCursor.mCol = mCursor.mLine = 0;
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
        if (mCursor.mLine > 0) {
            mCursor.mLine--;
            mCursor.mCol = Math.min(getCurrentLineLength(), mDesiredCursorCol);
            syncText();
        }
    }

    public void cursorDown(View view) {
        if (mCursor.mLine < getLineCount() - 1) {
            mCursor.mLine++;
            mCursor.mCol = Math.min(getCurrentLineLength(), mDesiredCursorCol);
            syncText();
        }
    }

    public void cursorLeft(View view) {
        if (mCursor.mCol == 0) {
            if (mCursor.mLine > 0) {
                mCursor.mLine--;
                mCursor.mCol = getCurrentLineLength();
            }
        } else {
            mCursor.mCol--;
        }
        mDesiredCursorCol = mCursor.mCol;
        syncText();
    }

    public void cursorRight(View view) {
        if (mCursor.mCol == getCurrentLineLength()) {
            if (mCursor.mLine < getLineCount() - 1) {
                mCursor.mLine++;
                mCursor.mCol = 0;
            }
        } else {
            mCursor.mCol++;
        }
        mDesiredCursorCol = mCursor.mCol;
        syncText();
    }

    private int getCurrentLineLength() {
        String[] lines = mInternalText.toString().split("\n");
        return lines[mCursor.mLine].length();
    }

    private int getLineCount() {
        return mInternalText.toString().split("\n").length;
    }

    /**
     * Take the text of <code>mInterText</code> transforming it into an HTML representation,
     * considering syntax highlighting.
     */
    private void syncText() {
        String fixed = HtmlGenerator.toHtml(mInternalText.toString(), mCursor);
        String html = "<pre style=\"font-size:12pt\"><code>" + fixed + "</code></pre>";
        mEditor.loadData(html, "text/html; charset=UTF-8", null);
    }

    public void clearCode(View view) {
        mInternalText.delete(0, mInternalText.length());
        mInternalText.append('\n');
        mCursor.mLine = mCursor.mCol = 0;
        syncText();
    }

    public void run(View view) {
        Intent intent = new Intent(this, RunActivity.class);
        String code = "try { eval(`" + mInternalText.toString() + "`); } catch(err) { print(err); }";
        intent.putExtra(EXTRA_CODE, code);
        startActivity(intent);
    }

    private int getCursorTextOffset() {
        int offset = mCursor.mCol;
        String[] line = mInternalText.toString().split("\n");
        for (int i = 0; i < mCursor.mLine; i++) {
            offset += line[i].length() + 1;
        }
        return offset;
    }

    private void write(String text) {
        mInternalText.insert(getCursorTextOffset(), text);
        mCursor.mCol += text.length();
    }

    private void writeIndentation(int indentation) {
        for (int i = 0; i < indentation * INDENTATION_LENGTH; i++) {
            mInternalText.append(' ');
        }
    }

    private int getCurrentLineIndentation() {
        String line = getCurrentLine();
        int indentation = 0;
        while(line.charAt(indentation) == ' ' && indentation < line.length()) {
            indentation++;
        }
        return indentation / INDENTATION_LENGTH;
    }

    private String getCurrentLine() {
        return mInternalText.toString().split("\n")[mCursor.mLine];
    }

    @SuppressWarnings("unused")
    public void writeText(View view) {
        Button button = (Button) view;
        String text = button.getText().toString();
        if (mShift) text = text.toUpperCase();
        else text = text.toLowerCase();

        switch (text) {
            case "{":
                int indentation = getCurrentLineIndentation();
                write(" {\n");
                writeIndentation(indentation + 1);
                break;
            case "}":
                write("}\n");
                break;
            case ",":
                write(", ");
                break;
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
                write(" " + text + " ");
                break;
            default:
                write(text);
                break;
        }

        syncText();
    }

    public void writeSpace(View view) {
        write(" ");
        syncText();
    }

    public void writeEnter(View view) {
        write("\n");
        mCursor.mCol = 0;
        mCursor.mLine++;
        syncText();
    }

    public void backspace(View view) {
        if (mInternalText.length() == 1) {
            clearCode(null);
        }
        int offset = getCursorTextOffset();
        int length = getCurrentLineLength();
        if (0 == offset) return;
        mInternalText.delete(offset - 1, offset);
        if (mCursor.mCol > 0) {
            mCursor.mCol--;
        } else if (mCursor.mLine > 0) {
            mCursor.mLine--;
            mCursor.mCol = getCurrentLineLength() - length;
        }
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
