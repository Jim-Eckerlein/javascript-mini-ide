package io.jimeckerlein.jsshell.editor;

import android.text.Editable;
import android.text.Selection;
import android.text.Spanned;

/**
 * Todo: Editable should be final
 */
public class Formatter {

    private static final char[] CONTINUATION_OPERATOR = new char[]{
            '+', '*', '/', '|', '!', '?', '%', '^',
            '.', '~', '=', '-', '&', '<', '>', ':', '\\'
    };

    private Editable mEditable;
    private final Cursor mI = new Cursor();
    private final Cursor mLineStart = new Cursor();
    private int mIndent;
    private int mLeadingSpaceCount;
    private boolean mInContinuation;

    public Formatter(Editable editable) {
        mEditable = editable;
    }

    /**
     * Pretty print the given editable string
     * @param editable String to be formatted
     * @param restrictToCursor If true, only line up to cursor line will be formatted
     */
    public void format(Editable editable, boolean restrictToCursor) {
        mEditable = editable;
        mI.reset();
        mLineStart.reset();
        mIndent = 0;
        mLeadingSpaceCount = 0;
        mInContinuation = false;
        int indentChange = 0;
        boolean enableContinuation = false;
        boolean inTemplateString = false;
        boolean atLineStart = true;

        int userSelection = 0;

        if(restrictToCursor) {
            userSelection = Selection.getSelectionEnd(editable);
        }

        for (; mI.hasNext(); mI.inc()) {

            char c = mI.getChar();

            if (atLineStart && !inTemplateString) {
                for (; ' ' == mI.getChar(); mI.inc()) {
                    mLeadingSpaceCount++;
                }
                c = mI.getChar();
            }

            if ('{' == c) {
                indentChange++;
            }

            else if ('}' == c) {
                mIndent--;
            }

            else if (!inTemplateString && c == '`') {
                inTemplateString = true;
            }

            else if (!enableContinuation && !inTemplateString && checkContinuationChar(c)) {
                enableContinuation = true;
            }

            if (enableContinuation && !Character.isWhitespace(c)) {
                enableContinuation = false;
            }

            atLineStart = false;

            if ('\n' == c) {

                // Write indent into editable:
                if (!inTemplateString) {
                    writeIndent();
                }

                // Check whether user selected line was reached:
                if(restrictToCursor && cp(mI) >= userSelection) {
                    break;
                }

                if (enableContinuation) {
                    mInContinuation = true;
                    enableContinuation = false;
                }
                else {
                    mInContinuation = false;
                }

                // Remember line start, but only after editable was eventually altered by writing indentation
                mEditable.setSpan(mLineStart, cp(mI) + 1, cp(mI) + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                // Add indent change onto indentation:
                mIndent += indentChange;

                // Reset variables:
                atLineStart = true;
                indentChange = 0;
                mLeadingSpaceCount = 0;
            }
        }

        if ((mEditable.length() > 0) && (mEditable.charAt(mEditable.length() - 1) != '\n')) {
            writeIndent();
        }
    }

    private void writeIndent() {
        int spaceIndentDiff = Math.max(mIndent, 0) * 4 - mLeadingSpaceCount;

        if (mInContinuation) {
            spaceIndentDiff += 2;
        }

        if (0 > spaceIndentDiff) {
            // We have to delete some tabs
            mEditable.delete(cp(mLineStart), cp(mLineStart) - spaceIndentDiff);
        }

        else if (0 < spaceIndentDiff) {
            // We have to add some tabs
            boolean moveIManually = cp(mLineStart) == cp(mI);
            for (int i = 0; i < spaceIndentDiff; i++) {
                mEditable.insert(cp(mLineStart), " ");
                if(moveIManually) {
                    mI.inc();
                    moveIManually = false;
                }
            }
        }
    }

    private boolean checkContinuationChar(char c) {
        for (char operator : CONTINUATION_OPERATOR) {
            if (operator == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the text of the editable.
     * All line leading spaces are replaced by that dot: ⋅
     * {@code mI} is visualized by: ^I^
     * {@code mLineStart} is visualized by: ^LS^
     *
     * @return Current formatter state as string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean atLineStart = true;
        for (int i = 0; i < mEditable.length(); i++) {
            char c = mEditable.charAt(i);
            if (cp(mI) == i) {
                sb.append("^I^");
            }
            if (cp(mLineStart) == i) {
                sb.append("^LS^");
            }
            if (' ' == c && atLineStart) {
                sb.append('\u22C5');
            }
            else {
                sb.append(c);
                atLineStart = '\n' == c;
            }
        }
        return sb.toString();
    }

    /**
     * Return the position of the specified cursor object.
     *
     * @param what Cursor object
     * @return Position of that cursor within <code>mEditable</code>
     */
    private int cp(Cursor what) {
        return mEditable.getSpanStart(what);
    }

    private class Cursor {

        public void reset() {
            mEditable.setSpan(this, 0, 0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        public void inc() {
            int cp = cp(this) + 1;
            mEditable.setSpan(this, cp, cp, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        public char getChar() {
            return mEditable.charAt(cp(this));
        }

        public boolean hasNext() {
            return cp(this) < mEditable.length();
        }

        @Override
        public String toString() {
            return "@" + cp(this);
        }
    }

}
