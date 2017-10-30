package io.jimeckerlein.jsshell.editor;

import android.text.Editable;
import android.text.Spanned;

/**
 * Todo: Editable should be final
 */
public class InlineFormatter {

    private Editable mEditable;
    private final Cursor mI = new Cursor();
    private final Cursor mLineStart = new Cursor();
    private int mIndent;
    private int mLeadingSpaceCount;

    public InlineFormatter(Editable editable) {
        mEditable = editable;
    }

    public void format(Editable editable) {
        mEditable = editable;
        mI.reset();
        mLineStart.reset();
        mIndent = 0;
        mLeadingSpaceCount = 0;
        int indentChange = 0;
        boolean atLineStart = true;

        for (; mI.hasNext(); mI.inc()) {

            char c = mI.getChar();

            if (atLineStart) {
                for (; ' ' == mI.getChar(); mI.inc()) {
                    mLeadingSpaceCount++;
                }
                atLineStart = false;
                c = mI.getChar();
            }

            if ('{' == c) {
                indentChange++;
            }

            if ('}' == c) {
                mIndent--;
            }

            if ('\n' == c) {
                // Write indent into editable:
                writeIndent();

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
        int indentDiff = Math.max(mIndent, 0) * 4 - mLeadingSpaceCount;

        if(0 > indentDiff) {
            // We have to delete some tabs
            mEditable.delete(cp(mLineStart), cp(mLineStart) - indentDiff);
        }

        else if(0 < indentDiff) {
            // We have to add some tabs
            for (int i = 0; i < indentDiff; i++) {
                mEditable.insert(cp(mLineStart), " ");
            }
        }
    }

    /**
     * Return the text of the editable.
     * All line leading spaces are replaced by that dot: ⋅
     * {@code mI} is visualized by: ^I^
     * {@code mLineStart} is visualized by: ^LS^
     * @return Current formatter state as string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean atLineStart = true;
        for(int i = 0; i < mEditable.length(); i++) {
            char c = mEditable.charAt(i);
            if(cp(mI) == i) {
                sb.append("^I^");
            }
            if(cp(mLineStart) == i) {
                sb.append("^LS^");
            }
            if(' ' == c && atLineStart) {
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
