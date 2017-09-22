package com.example.jimec.javascriptshell.code_lines;

/**
 * Represents the cursor position within the code text.
 */
public class Cursor {

    private final LineList mLineList;
    int mLine;
    int mCol;
    int mDesiredCol;

    public Cursor(LineList lineList) {
        mLineList = lineList;
    }

    public boolean moveToLineStart() {
        if (mCol != 0) {
            mDesiredCol = mCol = 0;
            return true;
        } else return false;
    }

    public boolean moveToLineEnding() {
        if (mCol != mLineList.getLine(mLine).getCode().length()) {
            mDesiredCol = mCol = mLineList.getLine(mLine).getCode().length();
            return true;
        } else return false;
    }

    public boolean moveToStart() {
        if (mLine != 0 || mCol != 0) {
            mLine = 0;
            mDesiredCol = mCol = 0;
            return true;
        } else return false;
    }

    public boolean moveLeft() {
        if (mCol == 0) {
            // Cursor is already at line start
            if (mLine > 0) {
                // Cursor moves up
                mLine--;
                moveToLineEnding();
                return true;
            } else return false;
        } else {
            mDesiredCol = --mCol;
            return true;
        }
    }

    public boolean moveRight() {
        if (mCol == mLineList.getLine(mLine).getCode().length()) {
            // Cursor is already at line end
            if (mLine < mLineList.getLineCount() - 1) {
                // Cursor moves down
                mLine++;
                moveToLineStart();
                return true;
            } else return false;
        } else {
            mDesiredCol = ++mCol;
            return true;
        }
    }

    public boolean moveUp() {
        if (mLine > 0) {
            mLine--;
            mCol = Math.min(mLineList.getLine(mLine).getCode().length(), mDesiredCol);
            return true;
        }
        return false;
    }

    public boolean moveDown() {
        if (mLine < mLineList.getLineCount() - 1) {
            mLine++;
            mCol = Math.min(mLineList.getLine(mLine).getCode().length(), mDesiredCol);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "" + mLine + ":" + mCol;
    }

    public void reset() {
        mCol = mLine = mDesiredCol = 0;
    }

}
