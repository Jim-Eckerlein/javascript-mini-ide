package com.example.jimec.javascriptshell.editor;

import android.content.Context;
import android.text.Spannable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.jimec.javascriptshell.R;

import java.util.regex.Pattern;

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
        inflate(getContext(), R.layout.editor_view, this);
        mEditText = findViewById(R.id.edit_text);
    
        // Hide usual virtual keyboard, I use my own one:
        mEditText.setShowSoftInputOnFocus(false);
        mEditText.requestFocus();
        
        /*mEditText.addTextChangedListener(new TextWatcher() {
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
        });*/
    }
    
    public void write(String code) {
        int start = getCursorStart();
        int end = getCursorEnd();
        int cursorPos = start + code.length();
    
        StringBuilder newCode = new StringBuilder(mEditText.getText().replace(start, end, code, 0, code.length()).toString());
        boolean beyondText = cursorPos == newCode.length();
        cursorPos = RuntimeFormatter.format(newCode, beyondText ? cursorPos - 1 : cursorPos);
        if (beyondText) {
            cursorPos++;
        }
        Spannable spannable = mHighlighter.highlight(newCode.toString());
        
        mEditText.setText(spannable);
        mEditText.setSelection(cursorPos);
    }
    
    public void backspace() {
        int start = getCursorStart();
        int end = getCursorEnd();
        
        if(start == end && start > 0) {
            // Delete single char
            mEditText.getText().delete(start - 1, start);
        }
        
        else if(end > start) {
            // Delete range
            mEditText.getText().delete(start, end);
        }
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
    
    public String getCode(boolean removeComments) {
        final Pattern mMultiLineCommentPatter = Pattern.compile("/\\*.*\\*/", Pattern.DOTALL);
        final Pattern mSingleLineCommentPatter = Pattern.compile("//.*$", Pattern.MULTILINE);
        String code = mEditText.getText().toString();
        if(removeComments) {
            code = mMultiLineCommentPatter.matcher(code).replaceAll("");
            code = mSingleLineCommentPatter.matcher(code).replaceAll("");
        }
        return code;
    }
}
