package io.jimeckerlein.jsshell.editor;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import io.jimeckerlein.jsshell.R;

import static io.jimeckerlein.jsshell.Util.clamp;

public class CodeEditorView extends FrameLayout {
    
    private Highlighter mHighlighter;
    private Formatter mCodeFormatter;
    private EditText mEditText;
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
        if(!isInEditMode()) mHighlighter = new Highlighter(getContext());
        mCodeFormatter = new Formatter(mEditText.getText());
    }
    
    public void write(String code, boolean moveCursorToStart) {
        int start = getCursorStart();
        int end = getCursorEnd();
        int cursorPos = start + code.length();
    
        mNewCode.replace(0, mNewCode.length(), mEditText.getText().replace(start, end, code, 0, code.length()).toString());
    
        if (code.equals("}") || code.equals("\n")) {
            mCodeFormatter.format(mEditText.getText(), true);
            cursorPos = mEditText.getSelectionStart();
            mNewCode.replace(0, mNewCode.length(), mEditText.getText().toString());
        }
    
        highlight(mNewCode.toString(), moveCursorToStart ? 0 : clamp(cursorPos, 0, mNewCode.toString().length()));
    }
    
    public void write(String code) {
        write(code, false);
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
            spaceIndex = Math.max(spaceIndex, 0);
            if (atLineStart) {
                text.delete(spaceIndex, start);
                highlight(text.toString(), spaceIndex);
            }
            
            // Delete single char
            else {
                mEditText.getText().delete(start - 1, start);
                highlight(mEditText.getText().toString(), start - 1);
            }
        }

        else if (end > start) {
            // Delete range
            mEditText.getText().delete(start, end);
            highlight(mEditText.getText().toString(), start);
        }
    }
    
    private void highlight(String newCode, int selection) {
        mEditText.setText(newCode);
        mEditText.setSelection(selection);
        HighlighterTask.Params params = new HighlighterTask.Params();
        HighlighterTask highlighterTask = new HighlighterTask(getContext(), mEditText);
        highlighterTask.execute(params.set(newCode, selection));
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
        mCodeFormatter.format(mEditText.getText(), false);
        // todo: simplify
        int cp = mEditText.getSelectionStart();
        mEditText.setText(mHighlighter.highlight(mEditText.getText().toString()));
        mEditText.setSelection(cp);
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
    
}
