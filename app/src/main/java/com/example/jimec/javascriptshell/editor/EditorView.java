package com.example.jimec.javascriptshell.editor;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.jimec.javascriptshell.R;

import static com.example.jimec.javascriptshell.Util.clamp;

public class EditorView extends FrameLayout {
    
    private final Highlighter mHighlighter = new Highlighter();
    private final CodeFormatter mCodeFormatter = new CodeFormatter();
    private EditText mEditText;
    
    public EditorView(Context context) {
        super(context);
        init();
    }
    
    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public EditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_editor, this);
        mEditText = findViewById(R.id.edit_text);
    
        // Hide usual virtual view_keyboard since I'm using my own one:
        mEditText.setShowSoftInputOnFocus(false);
        mEditText.requestFocus();
    
        mEditText.addTextChangedListener(new TextWatcher() {
            private boolean mParsed = false;
            
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
    
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
    
            @Override
            public void afterTextChanged(Editable editable) {
                if(!mParsed) {
                    mParsed = true;
                    write("");
                    mParsed = false;
                }
            }
        });
    }
    
    public void write(String code) {
        int start = getCursorStart();
        int end = getCursorEnd();
        int cursorPos = start + code.length();
    
        StringBuilder newCode = new StringBuilder(mEditText.getText().replace(start, end, code, 0, code.length()).toString());
    
        if (code.equals("}") || code.equals("\n")) {
            cursorPos = RuntimeFormatter.format(newCode, cursorPos - 1);
            cursorPos++;
        }
    
        mEditText.setSelection(0);
        highlight(newCode.toString());
        mEditText.setSelection(clamp(cursorPos, 0, newCode.toString().length() - 1));
    }
    
    public void backspace() {
        int start = getCursorStart();
        int end = getCursorEnd();
        Editable text = mEditText.getText();
    
        if (start == end && start > 0) {
            // If at line start, delete all leading spaces as well:
            boolean atLineStart = true;
            int spaceIndex = start - 1;
            for (; spaceIndex >= 0; spaceIndex--) {
                if ('\n' == text.charAt(spaceIndex)) {
                    break;
                }
                if (' ' != text.charAt(spaceIndex)) {
                    atLineStart = false;
                }
            }
            if (atLineStart) {
                text.delete(spaceIndex, start);
                highlight(text.toString());
                mEditText.setSelection(spaceIndex);
            }
            
            // Delete single char
            else {
                mEditText.getText().delete(start - 1, start);
                highlight(mEditText.getText().toString());
                mEditText.setSelection(start - 1);
            }
        }
    
        else if (end > start) {
            // Delete range
            mEditText.getText().delete(start, end);
            highlight(mEditText.getText().toString());
            mEditText.setSelection(start);
        }
    }
    
    private void highlight(String newCode) {
        Spannable spannable = mHighlighter.highlight(newCode);
        mEditText.setText(spannable);
    }
    
    private int getCursorStart() {
        int start = Math.max(mEditText.getSelectionStart(), 0);
        int end = Math.max(mEditText.getSelectionEnd(), 0);
        return Math.min(start, end);
    }
    
    private int getCursorEnd() {
        int start = Math.max(mEditText.getSelectionStart(), 0);
        int end = Math.max(mEditText.getSelectionEnd(), 0);
        return Math.max(start, end);
    }
    
    public void formatCode() {
        mCodeFormatter.format(mEditText.getText().toString(), getCursorStart());
        mEditText.setText(mHighlighter.highlight(mCodeFormatter.getFormatted()));
        mEditText.setSelection(mCodeFormatter.getFormattedCursorPos());
    }
    
    public void clear() {
        mEditText.getText().clear();
        mEditText.getText().clearSpans();
        mEditText.setSelection(0);
    }
    
    @Override
    public String toString() {
        return mEditText.getText().toString();
    }
    
    public void setCursor(int cursorPosition) {
        mEditText.setSelection(cursorPosition);
    }
}
