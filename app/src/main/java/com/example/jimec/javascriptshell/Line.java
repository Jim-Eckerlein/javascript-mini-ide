package com.example.jimec.javascriptshell;

public class Line {

    private int mIndent;
    private String mCode;

    public String getCode() {
        return mCode;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public int getIndent() {
        return mIndent;
    }

    public void setIndent(int mIndent) {
        this.mIndent = mIndent;
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

        if (previousLine.mCode.matches(".*\\{\\s*$")        // { block started
                || previousLine.mCode.matches(".*\\(\\s*$") // ( block started
                || previousLine.mCode.matches(".*\\[\\s*$") // [ block started
                ) {
            // Increase indent
            mIndent++;
        }

        if (previousLine.mCode.matches(".*\\}\\s*$")        // { block ended
                || previousLine.mCode.matches(".*\\)\\s*$") // ( block ended
                || previousLine.mCode.matches(".*\\]\\s*$") // [ block ended
                ) {
            // Decrease indent if greater than 0
            mIndent = mIndent > 0 ? mIndent - 1 : 0;
        }
    }

}
