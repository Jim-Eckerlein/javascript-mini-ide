package com.example.jimec.javascriptshell.editor;

import java.util.ArrayList;

class SelectableText {
    
    private final StringBuilder mText = new StringBuilder();
    private ArrayList<CursorPosition> mCursorPos = new ArrayList<>();
    
    /**
     * Prepare the class to operate on a new text
     *
     * @param text      Text.
     */
    public void initialize(String text) {
        mText.replace(0, mText.length(), text);
        mCursorPos.clear();
    }
    
    public void write(String s, int pos) {
        mCursorPos.stream().filter(cursorPosition -> pos <= cursorPosition.getPos()).forEach(cursorPosition -> cursorPosition.mPos += s.length());
        mText.insert(pos, s);
    }
    
    public void addCursorPosition(CursorPosition cursorPosition) {
        mCursorPos.add(cursorPosition);
    }
    
    public void delete(int pos, int len) {
        for(CursorPosition cursorPosition : mCursorPos) {
            if (pos + len < cursorPosition.getPos()) {
                cursorPosition.mPos -= len;
            }
            if(pos < cursorPosition.getPos() && pos + len >= cursorPosition.getPos()) {
                cursorPosition.setPos(pos);
            }
        }
        mText.delete(pos, pos + len);
    }
    
    @Override
    public String toString() {
        return mText.toString();
    }
    
    public class CursorPosition {
        
        private int mPos;
        
        public CursorPosition(int pos) {
            mPos = pos;
        }
        
        public int getPos() {
            return mPos;
        }
        
        public void setPos(int pos) {
            mPos = pos;
        }
        
        public void inc() {
            mPos++;
        }
        
        public boolean hasNext() {
            return mPos < SelectableText.this.mText.length();
        }
        
        public char getChar() {
            return SelectableText.this.mText.charAt(mPos);
        }
    }
}
