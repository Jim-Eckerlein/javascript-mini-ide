package com.example.jimec.javascriptshell;

import java.util.ArrayList;

/**
 * Encapsulate editing of multiple code lines.
 * Lines are accessed with a cursor, which can be freely moved.
 * In addition, the class is able to load a line list from an existing JS string,
 * preserving indentation.
 */

public class LineList {

    private Cursor mCursor = new Cursor();
    private ArrayList<Line> mLines = new ArrayList<>();

    /**
     * Construct an empty line list.
     */
    LineList() {
        Line line = new Line();
        line.setCode("");
        mLines.add(line);
    }

    public static void main(String[] args) {
        LineList lines = new LineList();
        lines.write("AAAA(){\nfirst\n}");
        lines.moveCursorToStart();
        lines.write("BBBB(){\nsecond\n}");
        lines.writeEnter();
        System.out.println(lines);
    }

    // TODO: add appendix to writeEnter(), so you can break within a line
    void writeEnter() {
        Line line = new Line();
        line.setCode("");
        if (mLines.size() > 0) {
            line.detectIndent(mLines.get(mCursor.mLine));
        }
        mLines.add(mCursor.mLine, line);
        mCursor.mCol++;
        mCursor.mLine = 0;
    }

    /**
     * Inserts simple code.
     * Can be multiline text, which is than automatically split and indented correctly.
     *
     * @param text Text code to be added
     */
    void write(String text) {
        String[] textLines = text.split("\\n");
        String appendix = "";

        for (int i = 0; i < textLines.length; i++) {
            // Trim text
            String textLine = textLines[i];
            textLine = textLine.replaceAll("^\\s*", "").replaceAll("\\s*$", "");

            Line line;

            if (i == 0) {
                // Append to last line
                line = mLines.get(mCursor.mLine);
                String oldLine = line.getCode();
                if (mCursor.mCol < oldLine.length()) {
                    appendix = oldLine.substring(mCursor.mCol, oldLine.length());
                }
                line.setCode(oldLine.substring(0, mCursor.mCol) + textLine);
            } else {
                // Create new line
                mCursor.mCol = 0;
                mCursor.mLine++;

                line = new Line();
                line.setCode(textLine);
                line.detectIndent(mLines.get(mCursor.mLine - 1));
                mLines.add(mCursor.mLine, line);
            }

            mCursor.mCol += textLine.length();
        }

        mLines.get(mCursor.mLine).setCode(mLines.get(mCursor.mLine).getCode() + appendix);
    }

    void moveCursorToLineStart() {
        mCursor.mDesiredCol = mCursor.mCol = 0;
    }

    void moveCursorToLineEnding() {
        mCursor.mDesiredCol = mCursor.mCol = mLines.get(mCursor.mLine).getCode().length();
    }

    void moveCursorToStart() {
        mCursor.mLine = 0;
        mCursor.mDesiredCol = mCursor.mCol = 0;
    }

    void moveCursorToEnding() {
        mCursor.mLine = mLines.size() - 1;
        moveCursorToLineEnding();
    }

    void moveCursorLeft() {
        if (mCursor.mCol == 0) {
            // Cursor is already at line start
            if (mCursor.mLine > 0) {
                mCursor.mLine--;
                moveCursorToLineEnding();
            }
        } else {
            mCursor.mDesiredCol = --mCursor.mCol;
        }
    }

    void moveCursorRight() {
        if (mCursor.mCol == mLines.get(mCursor.mLine).getCode().length()) {
            // Cursor is already at line end
            if (mCursor.mLine < mLines.size() - 1) {
                mCursor.mLine++;
                moveCursorToLineStart();
            }
        } else {
            mCursor.mDesiredCol = ++mCursor.mCol;
        }
    }

    void moveCursorUp() {
        if (mCursor.mLine > 0) {
            mCursor.mLine--;
            mCursor.mCol = Math.min(mLines.get(mCursor.mLine).getCode().length(), mCursor.mDesiredCol);
        }
    }

    void moveCursorDown() {
        if (mCursor.mLine < mLines.size() - 1) {
            mCursor.mLine++;
            mCursor.mCol = Math.min(mLines.get(mCursor.mLine).getCode().length(), mCursor.mDesiredCol);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("LineList, ").append(mLines.size()).append(" lines, cursor at ").append(mCursor.toString()).append('\n');
        int i = 0;
        for (Line line : mLines) {
            sb.append('[').append(i).append("]");
            for (int indent = 0; indent < line.getIndent(); indent++) {
                sb.append("    ");
            }
            sb.append(line.getCode()).append("\n");
            i++;
        }

        return sb.toString();
    }

    /**
     * Represents the cursor position within the code text.
     */
    static class Cursor {

        int mLine;
        int mCol;
        int mDesiredCol;

        public void nextCol() {
            mCol++;
        }

        public void nextLine() {
            mCol = 0;
            mLine++;
        }

        @Override
        public String toString() {
            return "" + mLine + ":" + mCol;
        }
    }
}
