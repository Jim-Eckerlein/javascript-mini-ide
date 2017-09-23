package com.example.jimec.javascriptshell.code_lines;

/**
 * Represents the cursor position within the code text.
 */
public class Cursor {
    
    private final LineList mLineList;
    int mLine;
    int mCol;
    
    public Cursor(LineList lineList) {
        mLineList = lineList;
    }
    
    /**
     * Move the cursor to line-list's start
     *
     * @return True if cursor position was changed
     */
    public boolean moveToLineStart() {
        if (mCol != 0) {
            mCol = 0;
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Move the cursor to line-list's end
     *
     * @return True if cursor position was changed
     */
    public boolean moveToLineEnding() {
        if (mCol != mLineList.getLine(mLine).getCode().length()) {
            mCol = mLineList.getLine(mLine).getCode().length();
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Move the cursor to the current line's start
     *
     * @return True if cursor position was changed
     */
    public boolean moveToStart() {
        if (mLine != 0 || mCol != 0) {
            mLine = 0;
            mCol = 0;
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Move the cursor one position to the left,
     * or to the previous line if cursor is at line start already.
     *
     * @return True if cursor position was changed
     */
    public boolean moveLeft() {
        if (mCol == 0) {
            // Cursor is already at line start
            if (mLine > 0) {
                // Cursor moves up into previous line
                mLine--;
                mCol = mLineList.getLine(mLine).getCode().length();
                return true;
            }
            else {
                return false;
            }
        }
        else {
            mCol--;
            return true;
        }
    }
    
    /**
     * Move the cursor one position to the right,
     * or to the next line if cursor is at line end already.
     *
     * @return True if cursor position was changed
     */
    public boolean moveRight() {
        if (mCol == mLineList.getLine(mLine).getCode().length()) {
            // Cursor is already at line end
            if (mLine < mLineList.getLineCount() - 1) {
                // Cursor moves down into next line
                mLine++;
                mCol = 0;
                return true;
            }
            else {
                return false;
            }
        }
        else {
            mCol++;
            return true;
        }
    }
    
    public boolean moveUp() {
        if (mLine > 0) {
            mLine--;
    
            // Fix col when indent changes
            // If indentDiff > 0, then the indentation level has increased:
            int indentDiff = mLineList.getLine(mLine).getIndent() - mLineList.getLine(mLine + 1).getIndent();
            mCol -= indentDiff * 4;
    
            // Clamp col between valid ranges: [0; end]
            mCol = Math.max(mCol, 0);
            mCol = Math.min(mCol, mLineList.getLine(mLine).getCode().length());
            
            return true;
        }
        return false;
    }
    
    public boolean moveDown() {
        if (mLine < mLineList.getLineCount() - 1) {
            mLine++;
    
            // Fix col when indent changes
            // If indentDiff > 0, then the indentation level has increased:
            int indentDiff = mLineList.getLine(mLine).getIndent() - mLineList.getLine(mLine - 1).getIndent();
            mCol -= indentDiff * 4;
    
            // Clamp col between valid ranges: [0; end]
            mCol = Math.max(mCol, 0);
            mCol = Math.min(mCol, mLineList.getLine(mLine).getCode().length());
            
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "" + mLine + ":" + mCol;
    }
    
    public void reset() {
        mCol = mLine = 0;
    }
    
}
