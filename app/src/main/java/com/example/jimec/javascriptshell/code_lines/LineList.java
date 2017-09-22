package com.example.jimec.javascriptshell.code_lines;

import java.util.ArrayList;

/**
 * Encapsulate editing of multiple code lines.
 * Lines are accessed with a cursor, which can be freely moved.
 * In addition, the class is able to load a line list from an existing JS string,
 * preserving indentation.
 */

public class LineList {

    private Cursor mCursor = new Cursor(this);
    private ArrayList<Line> mLines = new ArrayList<>();
    private ArrayList<OnEditListener> mEditListeners = new ArrayList<>();

    /**
     * Construct an empty line list.
     */
    public LineList() {
        clear();
    }

    public static void main(String[] args) {
        LineList lines = new LineList();
        lines.write("print\n// this is a comment\nwhile /* another comment */ for\nin");
        System.out.println(lines.toStringWithoutComments());
    }

    public ArrayList<Line> getLines() {
        return mLines;
    }

    Line getLine(int index) {
        return mLines.get(index);
    }

    int getLineCount() {
        return mLines.size();
    }

    public void addOnEditListener(OnEditListener listener) {
        mEditListeners.add(listener);
    }

    private void notifyEditListeners() {
        for (OnEditListener listener : mEditListeners) {
            listener.onEdit(this);
        }
    }

    public void clear() {
        mLines.clear();

        Line line = new Line();
        line.setCode("");
        mLines.add(line);

        mCursor.reset();

        notifyEditListeners();
    }

    public int getCursorLine() {
        return mCursor.mLine;
    }

    public int getCursorCol() {
        return mCursor.mCol;
    }

    public void backspace() {
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

        notifyEditListeners();
    }

    public void writeEnter() {
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

        notifyEditListeners();
    }

    /**
     * Inserts simple code.
     * Can be multiline text, which is than automatically split and indented correctly.
     *
     * @param text Text code to be added
     */
    public void write(String text) {
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

        notifyEditListeners();
    }

    public void moveCursorToLineStart() {
        if (mCursor.moveToLineStart()) {
            notifyEditListeners();
        }
    }

    public void moveCursorToLineEnding() {
        if (mCursor.moveToLineEnding()) {
            notifyEditListeners();
        }
    }

    public void moveCursorToStart() {
        if (mCursor.moveToStart()) {
            notifyEditListeners();
        }
    }

    public void moveCursorLeft() {
        if (mCursor.moveLeft()) {
            notifyEditListeners();
        }
    }

    public void moveCursorRight() {
        if (mCursor.moveRight()) {
            notifyEditListeners();
        }
    }

    public void moveCursorUp() {
        if (mCursor.moveUp()) {
            notifyEditListeners();
        }
    }

    public void moveCursorDown() {
        if (mCursor.moveDown()) {
            notifyEditListeners();
        }
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

    public String toStringWithoutComments() {
        return toString()
                .replaceAll("(?m)^//.*$", "")
                .replaceAll("/\\*.*\\*/", "");
    }

    public interface OnEditListener {

        void onEdit(LineList lines);

    }

}
