package com.example.jimec.javascriptshell;

/**
 * Represents the cursor position within the code text.
 */
class Cursor {
    int mLine = 0;
    int mCol = 0;

    Cursor() {
    }

    Cursor(int line, int col) {
        mLine = line;
        mCol = col;
    }

    void nextCol() {
        mCol++;
    }

    void nextLine() {
        mCol = 0;
        mLine++;
    }

    @Override
    public String toString() {
        return "" + mLine + ":" + mCol;
    }
}
