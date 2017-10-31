package io.jimeckerlein.jsshell.editor;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import io.jimeckerlein.jsshell.R;
import io.jimeckerlein.jsshell.Util;

/**
 * The view containing a fully scrollable edit text.
 */
public class CodeEditorView extends FrameLayout {

    private final Formatter mFormatter = new Formatter();
    private EditText mEditText;

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
    }

    /**
     * Write the piece of code at the current cursor position.
     *
     * @param code              Code to be written.
     * @param moveCursorToStart If true, cursor is moved to start
     */
    public void write(String code, boolean moveCursorToStart) {
        mEditText.getText().replace(getCursorStart(), getCursorEnd(), code, 0, code.length());

        if (code.equals("}") || code.equals("\n")) {
            mFormatter.format(mEditText.getText(), true);
        }

        highlight(mEditText.getText().toString(), moveCursorToStart ? 0 : mEditText.getSelectionStart());
    }

    public void write(String code) {
        write(code, false);
    }

    /**
     * Delete code at cursor position.
     * If a range of characters is selected, that range is deleted.
     */
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

    /**
     * Emit the asynchronous highlighting task.
     * @param newCode Code to be highlighted
     * @param selection Cursor position, required because whole text is replaced
     */
    private void highlight(String newCode, int selection) {
        HighlighterTask.Params params = new HighlighterTask.Params();
        HighlighterTask highlighterTask = new HighlighterTask(getContext(), mEditText, ((highlightedCode, cursorPos) -> post(() -> Util.runOnUiThread(() -> {
            mEditText.setText(highlightedCode);
            mEditText.setSelection(cursorPos);
        }))));
        highlighterTask.execute(params.set(newCode, selection));
    }

    /**
     * Get start of selected range or simply the cursor position.
     * @return Selection start.
     */
    private int getCursorStart() {
        int start = Math.max(mEditText.getSelectionStart(), 0);
        int end = Math.max(mEditText.getSelectionEnd(), 0);
        return Math.min(start, end);
    }

    /**
     * Get end of selected range or simply the cursor position.
     * @return Selection end.
     */
    private int getCursorEnd() {
        int start = Math.max(mEditText.getSelectionStart(), 0);
        int end = Math.max(mEditText.getSelectionEnd(), 0);
        return Math.max(start, end);
    }

    /**
     * Formats the whole code.
     */
    public void format() {
        mFormatter.format(mEditText.getText(), false);
    }

    /**
     * Clear the editor.
     */
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
