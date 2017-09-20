package com.example.jimec.javascriptshell;

public class Line {

    private int mIndent;
    private String mCode = "";

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public int getIndent() {
        return mIndent;
    }

    public void setIndent(int indent) {
        mIndent = indent;
    }

    /**
     * Instead of manual indention, this method detects this line's indentation level based on
     * it previous line.
     * Generally, the indentation simply stays same, but an opening block will increase it, for
     * example an <code>{</code>, <code>(</code> or <code>[</code>.
     *
     * @param previousLine Line with is preceding this one
     */
    public void detectIndent(Line previousLine) {
        mIndent = previousLine.mIndent;
        mIndent += countBrace(previousLine.mCode, '{', '}');
        mIndent += Math.min(countBrace(mCode, '{', '}'), 0); // The current line cannot lift it's indention by itself

        // Level can be lower than 0
        mIndent = mIndent < 0 ? 0 : mIndent;
    }

    private int countBrace(String code, char opening, char closing) {
        int indent = 0;
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == opening) indent++;
            else if (code.charAt(i) == closing) indent--;
        }
        return indent;
    }

    @Override
    public String toString() {
        return "(@" + mIndent + ") " + mCode;
    }
}
