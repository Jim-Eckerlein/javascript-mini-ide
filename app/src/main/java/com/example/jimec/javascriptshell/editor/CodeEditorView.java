package com.example.jimec.javascriptshell.editor;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.jimec.javascriptshell.R;

import java.util.ArrayDeque;

import static com.example.jimec.javascriptshell.Util.clamp;

public class CodeEditorView extends FrameLayout {
    
    public static final int MAX_UNDO_HISTORY = 50;
    
    private final Highlighter mHighlighter = new Highlighter(getContext());
    private final CodeFormatter mCodeFormatter = new CodeFormatter();
    private final ArrayDeque<String> mHistory = new ArrayDeque<>();
    private final ArrayDeque<Integer> mCursorHistory = new ArrayDeque<>();
    private final TextChangeListener mTextChangeListener = new TextChangeListener();
    private EditText mEditText;
    private boolean mSkipHistoryBackup = false;
    private int mCursorHistoryCurrentPosition;
    private StringBuilder mNewCode = new StringBuilder();
    
    public CodeEditorView(Context context) {
        super(context);
        init();
    }
    
    public CodeEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public CodeEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_editor, this);
        mEditText = findViewById(R.id.edit_text);
        mEditText.requestFocus();
        mEditText.addTextChangedListener(mTextChangeListener);
    }
    
    public void write(String code) {
        int start = getCursorStart();
        int end = getCursorEnd();
        int cursorPos = start + code.length();
    
        mNewCode.replace(0, mNewCode.length(), mEditText.getText().replace(start, end, code, 0, code.length()).toString());
    
        if (code.equals("}") || code.equals("\n")) {
            cursorPos = RuntimeFormatter.format(mNewCode, cursorPos - 1) + 1;
        }
    
        mCursorHistoryCurrentPosition = start;
        mTextChangeListener.mInternalChange = true;
        mEditText.setSelection(0);
        highlight(mNewCode.toString());
        mEditText.setSelection(clamp(cursorPos, 0, mNewCode.toString().length()));
    }
    
    public void backspace() {
        int start = getCursorStart();
        int end = getCursorEnd();
        Editable text = mEditText.getText();
        mTextChangeListener.mInternalChange = true;
        mCursorHistoryCurrentPosition = start;
        
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
            spaceIndex = Math.max(spaceIndex, 0);
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
    
    public void format() {
        mCodeFormatter.format(mEditText.getText().toString(), getCursorStart());
        mEditText.setText(mHighlighter.highlight(mCodeFormatter.getFormatted()));
        mEditText.setSelection(mCodeFormatter.getFormattedCursorPos());
    }
    
    public void clear() {
        mEditText.getText().clear();
        mEditText.getText().clearSpans();
        mEditText.setSelection(0);
        if (!mSkipHistoryBackup) {
            // We are not currently inside an undo operation => trash undo history as well
            mHistory.clear();
            mCursorHistory.clear();
        }
    }
    
    public void undo() {
        if (mHistory.size() > 1) {
            mSkipHistoryBackup = true;
            clear();
            mHistory.removeLast();
            mCursorHistory.removeLast();
            write(mHistory.getLast());
            mEditText.setSelection(mCursorHistory.getLast());
            mSkipHistoryBackup = false;
        }
    }
    
    private void backupToHistory() {
        if (mSkipHistoryBackup) {
            return;
        }
        String code = mEditText.getText().toString();
        if (mHistory.size() > 0 && mHistory.getLast().equals(code)) {
            return;
        }
        mHistory.addLast(code);
        mCursorHistory.addLast(mCursorHistoryCurrentPosition);
    
        if (mHistory.size() > MAX_UNDO_HISTORY) {
            mHistory.removeFirst();
            mCursorHistory.removeFirst();
        }
    }
    
    @Override
    public String toString() {
        return mEditText.getText().toString();
    }
    
    public void moveCursorToStart() {
        mEditText.setSelection(0);
    }
    
    private class TextChangeListener implements TextWatcher {
        
        private boolean mUpdated = false;
        private boolean mInternalChange = false;
        
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        
        @Override
        public void afterTextChanged(Editable editable) {
            if (mInternalChange) {
                mInternalChange = false;
                
                backupToHistory();
            }
            else if (!mUpdated) {
                mUpdated = true;
                write("");
                
                backupToHistory();
                
                mUpdated = false;
            }
        }
        
    }
}
