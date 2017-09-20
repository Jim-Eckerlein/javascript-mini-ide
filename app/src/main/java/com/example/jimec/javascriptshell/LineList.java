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
        clear();
    }

    public static void main(String[] args) {
        LineList lines = new LineList();
        //lines.write("function {\nprint\n}");
        lines.write("function {");
        lines.writeEnter();
        lines.write("print");
        lines.writeEnter();
        lines.write("in}");
        lines.moveCursorLeft();
        lines.backspace();
        lines.backspace();
        lines.backspace();
        lines.writeEnter();
        System.out.println(lines);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public ArrayList<Line> getLines() {
        return mLines;
    }

    void clear() {
        mLines.clear();

        Line line = new Line();
        line.setCode("");
        mLines.add(line);

        mCursor.reset();
    }

    void backspace() {
        if (mCursor.mCol > 0) {
            // Delete char under cursor
            mCursor.mCol--;
            StringBuilder codeLine = new StringBuilder(mLines.get(mCursor.mLine).getCode());
            codeLine.deleteCharAt(mCursor.mCol);
            mLines.get(mCursor.mLine).setCode(codeLine.toString());

            if (mCursor.mCol == 0 && mCursor.mLine > 0) {
                // Deleted first character, indentation could change:
                mLines.get(mCursor.mLine).detectIndent(mLines.get(mCursor.mLine - 1));
            }
        } else if (mCursor.mLine > 0) {
            // Delete line break by merging lines, only possible if not first line
            int newCol = mLines.get(mCursor.mLine - 1).getCode().length();
            mLines.get(mCursor.mLine - 1).setCode(mLines.get(mCursor.mLine - 1).getCode()
                    + mLines.get(mCursor.mLine).getCode());
            mLines.remove(mCursor.mLine--);
            mCursor.mCol = newCol;
        }
    }

    void writeEnter() {
        /*
        Line line = new Line();
        String oldCodeLine = mLines.get(mCursor.mLine).getCode();
        line.setCode(oldCodeLine.substring(0, mCursor.mCol));
        if (mLines.size() > 0) {
            line.detectIndent(mLines.get(mCursor.mLine));
        }
        mLines.get(mCursor.mLine).setCode(oldCodeLine.substring(mCursor.mCol));
        mLines.add(mCursor.mLine, line);
        mCursor.mLine++;
        moveCursorToLineStart();*/

        Line currentLine = mLines.get(mCursor.mLine);
        Line newLine = new Line();
        if (mCursor.mCol < currentLine.getCode().length()) {
            // Cursor is not at the end of the current line, move the rest into the new line:
            newLine.setCode(currentLine.getCode().substring(mCursor.mCol));
            currentLine.setCode(currentLine.getCode().substring(0, mCursor.mCol));
        }
        newLine.detectIndent(currentLine);
        mLines.add(mCursor.mLine + 1, newLine);
        mCursor.mLine++;
        moveCursorToLineStart();
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
            String textLine = textLines[i];

            // Remove leading and following white spaces, but only if the code contains
            // characters other than white spaces:
            if (!textLine.matches("^\\s*$")) {
                textLine = textLine.replaceAll("^\\s*", "").replaceAll("\\s*$", "");
            }

            Line line;

            if (i == 0) {
                // Append to last line
                line = mLines.get(mCursor.mLine);
                String oldLine = line.getCode();
                if (mCursor.mCol < oldLine.length()) {
                    appendix = oldLine.substring(mCursor.mCol);
                }
                line.setCode(oldLine.substring(0, mCursor.mCol) + textLine);

                if (mCursor.mCol == 0 && mCursor.mLine > 0) {
                    line.detectIndent(mLines.get(mCursor.mLine - 1));
                }
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

    boolean moveCursorToLineStart() {
        if (mCursor.mCol != 0) {
            mCursor.mDesiredCol = mCursor.mCol = 0;
            return true;
        } else return false;
    }

    boolean moveCursorToLineEnding() {
        if (mCursor.mCol != mLines.get(mCursor.mLine).getCode().length()) {
            mCursor.mDesiredCol = mCursor.mCol = mLines.get(mCursor.mLine).getCode().length();
            return true;
        } else return false;
    }

    boolean moveCursorToStart() {
        if (mCursor.mLine != 0 || mCursor.mCol != 0) {
            mCursor.mLine = 0;
            mCursor.mDesiredCol = mCursor.mCol = 0;
            return true;
        } else return false;
    }

    boolean moveCursorLeft() {
        if (mCursor.mCol == 0) {
            // Cursor is already at line start
            if (mCursor.mLine > 0) {
                // Cursor moves up
                mCursor.mLine--;
                moveCursorToLineEnding();
                return true;
            } else return false;
        } else {
            mCursor.mDesiredCol = --mCursor.mCol;
            return true;
        }
    }

    boolean moveCursorRight() {
        if (mCursor.mCol == mLines.get(mCursor.mLine).getCode().length()) {
            // Cursor is already at line end
            if (mCursor.mLine < mLines.size() - 1) {
                // Cursor moves down
                mCursor.mLine++;
                moveCursorToLineStart();
                return true;
            } else return false;
        } else {
            mCursor.mDesiredCol = ++mCursor.mCol;
            return true;
        }
    }

    boolean moveCursorUp() {
        if (mCursor.mLine > 0) {
            mCursor.mLine--;
            mCursor.mCol = Math.min(mLines.get(mCursor.mLine).getCode().length(), mCursor.mDesiredCol);
            return true;
        }
        return false;
    }

    boolean moveCursorDown() {
        if (mCursor.mLine < mLines.size() - 1) {
            mCursor.mLine++;
            mCursor.mCol = Math.min(mLines.get(mCursor.mLine).getCode().length(), mCursor.mDesiredCol);
            return true;
        }
        return false;
    }

    public void dump() {
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

        System.out.println(sb.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Line line : mLines) {
            for (int indent = 0; indent < line.getIndent(); indent++) {
                sb.append("    ");
            }
            sb.append(line.getCode()).append("\n");
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

        @Override
        public String toString() {
            return "" + mLine + ":" + mCol;
        }

        public void reset() {
            mCol = mLine = mDesiredCol = 0;
        }
    }
}
