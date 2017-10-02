package com.example.jimec.javascriptshell.editor;

public class CodeFormatter {
    
    private final SelectableText mSelectableText = new SelectableText();
    private String mFormatted;
    private int mFormattedCursorPos;
    private SelectableText.CursorPosition mLastLineStart = mSelectableText.new CursorPosition(0);
    private SelectableText.CursorPosition mI = mSelectableText.new CursorPosition(0);
    private SelectableText.CursorPosition mCursorPosition = mSelectableText.new CursorPosition(0);
    private int mCurrentIndent = 0;
    private int mNextIndent = 0;
    private boolean mAtLineStart = true;
    
    public static void main(String[] args) {
        CodeFormatter codeFormatter = new CodeFormatter();
        String code = "{\nf";
    
        codeFormatter.format(code, 11);
        String formatted = codeFormatter.getFormatted();
        int pos = codeFormatter.getFormattedCursorPos();
        System.out.println("@" + pos);
        System.out.println(formatted);
    
        codeFormatter.format(formatted, pos);
        formatted = codeFormatter.getFormatted();
        pos = codeFormatter.getFormattedCursorPos();
        System.out.println("@" + pos);
        System.out.println(formatted);
    }
    
    public void format(String input, int cursorPos) {
        mSelectableText.initialize(input);
        mCursorPosition.setPos(cursorPos);
        mLastLineStart.setPos(0);
        mI.setPos(0);
        mSelectableText.addCursorPosition(mCursorPosition);
        mSelectableText.addCursorPosition(mLastLineStart);
        mSelectableText.addCursorPosition(mI);
        mCurrentIndent = 0;
        mNextIndent = 0;
        mAtLineStart = true;
        
        for (; mI.hasNext(); mI.inc()) {
            
            char c = mI.getChar();
            
            if(mAtLineStart) {
                while (' ' == c) {
                    mSelectableText.delete(mI.getPos(), 1);
                    c = mI.getChar();
                }
                mAtLineStart = false;
            }
            
            if ('{' == c) {
                mCurrentIndent++;
            }
            
            if ('}' == c) {
                mCurrentIndent--;
            }
            
            if ('\n' == c) {
                writeIndent();
            }
        }
    
        if ((input.length() > 0) && (input.charAt(input.length() - 1) != '\n')) {
            writeIndent();
        }
        
        mFormatted = mSelectableText.toString();
        mFormattedCursorPos = mCursorPosition.getPos();
    }
    
    private void writeIndent() {
        if(mCurrentIndent > 0) {
            for (int indent = 0; indent < mNextIndent; indent++) {
                mSelectableText.write("    ", mLastLineStart.getPos());
            }
        
            mNextIndent += mCurrentIndent;
        }
        else {
            mNextIndent += mCurrentIndent;
        
            for (int indent = 0; indent < mNextIndent; indent++) {
                mSelectableText.write("    ", mLastLineStart.getPos());
            }
        }
    
        mCurrentIndent = 0;
        mLastLineStart.setPos(mI.getPos() + 1);
        mAtLineStart = true;
    }
    
    public String getFormatted() {
        return mFormatted;
    }
    
    public int getFormattedCursorPos() {
        return mFormattedCursorPos;
    }
}
