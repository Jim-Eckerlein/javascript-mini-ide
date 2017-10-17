package io.jimeckerlein.jsshell.editor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import io.jimeckerlein.jsshell.R;

import static io.jimeckerlein.jsshell.Util.strbackcmp;
import static io.jimeckerlein.jsshell.Util.strcmp;

public class Highlighter {
    
    private static final int SPACE = 100;
    private static final int NEUTRAL = 0;
    private static final int STRING = 1;
    private static final int COMMENT_SINGE_LINE = 2;
    private static final int COMMENT_MULTI_LINE = 3;
    private static final int KEYWORD = 4;
    private static final int NUMBER = 5;
    private static final int HEX_NUMBER = 6;
    private static final int HEX_NUMBER_PREFIX = 7;
    private static final int DECIMAL_PART_NUMBER = 8;
    
    private static final String[] KEYWORD_LIST = new String[]{
            "print", "sleep", "exit",
            "NaN", "Infinity", "arguments", "await", "break", "case", "catch",
            "class", "const", "continue", "debugger", "default", "delete", "do",
            "else", "enum", "eval", "export", "extends", "false", "",
            "finally", "for", "function", "if", "implements", "import",
            "in", "instanceof", "interface", "let", "new",
            "null", "package", "private", "protected", "public", "return", "static",
            "super", "switch", "this", "throw", "true",
            "try", "typeof", "var", "void", "while", "with", "yield"
    };
    private static final char[] OPERATOR_LIST = new char[]{
            '+', '*', '/', '{', '}', '(', ')', ',', '|', '!', '?', '%', '^',
            '.', ';', '~', '=', '[', ']', '-', '&', '<', '>', ':', '\\'
    };
    private static final String TAG = "Highlighter";
    
    private final SpannableStringBuilder mSpanBuilder = new SpannableStringBuilder();
    private final Context mContext;
    private final ReallocatingIntBuffer mHighlightSpanPositionBuffer = new ReallocatingIntBuffer(100);
    private final ReallocatingIntBuffer mHighlightSpanTypeBuffer = new ReallocatingIntBuffer(100);
    
    public Highlighter(Context context) {
        mContext = context;
    }
    
    public Spannable highlight(String code) {
        // Re-initialize span builder:
        mSpanBuilder.clear();
        mSpanBuilder.clearSpans();
        mSpanBuilder.append(code);
        
        if(!findHighlights(code, mHighlightSpanPositionBuffer, mHighlightSpanTypeBuffer)) {
            // Something went wrong
            Log.e(TAG, "ERROR on finding Highlights");
        }
    
        Log.e(TAG, "Highlight positions: " + mHighlightSpanPositionBuffer);
        
        return mSpanBuilder;
    }
    
    private native boolean findHighlights(String code, ReallocatingIntBuffer positionBuffer, ReallocatingIntBuffer typeBuffer);
    
    @Deprecated
    public Spannable highlightJAVA(String code) {
        int currentTextType = SPACE;
        
        // Re-initialize span builder:
        mSpanBuilder.clear();
        mSpanBuilder.clearSpans();
        mSpanBuilder.append(code);
        
        // Code could be empty:
        if(code.length() == 0) {
            return mSpanBuilder;
        }
        
        int spanStart = 0;
        char c = code.charAt(0);
        
        // Highlight line:
        for (int i = 0;; ) {
            
            boolean reprocessCurrentCharacter = false;
            
            // Single line comment start
            if (strcmp(code, "//", i) && (currentTextType == NEUTRAL || currentTextType == SPACE)) {
                spanStart = i;
                currentTextType = COMMENT_SINGE_LINE;
            }
            
            // Single line comment end
            else if (currentTextType == COMMENT_SINGE_LINE && c == '\n') {
                mSpanBuilder.setSpan(createCommentSpan(), spanStart, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                currentTextType = SPACE;
            }
            
            // Multi line comment start
            else if (strcmp(code, "/*", i) && (currentTextType == NEUTRAL || currentTextType == SPACE)) {
                spanStart = i;
                currentTextType = COMMENT_MULTI_LINE;
            }
            
            // Multi line comment end
            else if (strbackcmp(code, "*/", i) && currentTextType == COMMENT_MULTI_LINE) {
                mSpanBuilder.setSpan(createCommentSpan(), spanStart, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                currentTextType = SPACE;
            }
            
            // String starting
            else if (strcmp(code, "'", i) && (currentTextType == NEUTRAL || currentTextType == SPACE)) {
                spanStart = i;
                currentTextType = STRING;
            }
            
            // String ending
            else if (strbackcmp(code, "'", i) && currentTextType == STRING) {
                mSpanBuilder.setSpan(createStringSpan(), spanStart, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                currentTextType = SPACE;
            }
            
            // Keyword start
            else if (Character.isLetter(c) && currentTextType == SPACE && isKeyword(code, i)
                    && (i == 0 || !Character.isLetter(code.charAt(i - 1)))) {
                currentTextType = KEYWORD;
                spanStart = i;
            }
            
            // Keyword end
            else if (!Character.isLetter(c) && currentTextType == KEYWORD) {
                mSpanBuilder.setSpan(createKeywordSpan(), spanStart, i, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                currentTextType = SPACE;
                reprocessCurrentCharacter = true;
            }
            
            // Hex number start
            else if (strcmp(code, "0x", i) && currentTextType == SPACE && (i > 0 && Character.isWhitespace(code.charAt(i - 1)))) {
                currentTextType = HEX_NUMBER_PREFIX;
                spanStart = i;
            }
            
            // Hex number start x
            else if (currentTextType == HEX_NUMBER_PREFIX && c == 'x') {
                currentTextType = HEX_NUMBER;
            }
            
            // Hex number end
            else if (currentTextType == HEX_NUMBER && (!Character.isDigit(c)
                    && !(c >= 'a' && c <= 'f')
                    && !(c >= 'A' && c <= 'F')
            )) {
                mSpanBuilder.setSpan(createNumberSpan(), spanStart, i, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                currentTextType = SPACE;
                reprocessCurrentCharacter = true;
            }
            
            // Number start
            else if (Character.isDigit(c) && currentTextType == SPACE) {
                currentTextType = NUMBER;
                spanStart = i;
            }
            
            // Decimal part of number
            else if (c == '.' && currentTextType == NUMBER) {
                currentTextType = DECIMAL_PART_NUMBER;
            }
            
            // Number end
            else if (!Character.isDigit(c) && (currentTextType == NUMBER || currentTextType == DECIMAL_PART_NUMBER)) {
                currentTextType = NEUTRAL;
                reprocessCurrentCharacter = true;
                mSpanBuilder.setSpan(createNumberSpan(), spanStart, i, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            
            // Operator
            else if (!Character.isWhitespace(c) && isOperator(c) && (currentTextType == NEUTRAL || currentTextType == SPACE)) {
                mSpanBuilder.setSpan(createOperatorSpan(), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                currentTextType = SPACE;
            }
    
            // Neutral text
            else if (currentTextType == SPACE && Character.isLetter(c)) {
                currentTextType = NEUTRAL;
            }
    
            // Space
            else if (currentTextType == NEUTRAL && Character.isWhitespace(c)) {
                currentTextType = SPACE;
            }
            
            if (reprocessCurrentCharacter) {
                continue;
            }
            
            i++;
            if(i > code.length()) {
                break;
            }
            else if(i == code.length()) {
                // Append apparently \0 to code, so that spans have the change to end
                c = '\0';
            }
            else {
                c = code.charAt(i);
            }
        }
        
        // Submit new text to view:
        return mSpanBuilder;
    }
    
    private boolean isKeyword(String code, int position) {
        int i;
        for (i = position; i < code.length(); i++) {
            if(!Character.isLetter(code.charAt(i))) {
                break;
            }
        }
        code = code.substring(position, i);
        for (String item : KEYWORD_LIST) {
            if (code.equals(item)) return true;
        }
        return false;
    }
    
    private boolean isOperator(char character) {
        for (char c : OPERATOR_LIST) {
            if (character == c)
                return true;
        }
        return false;
    }
    
    private ForegroundColorSpan createKeywordSpan() {
        return new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightKeyword));
    }
    
    private ForegroundColorSpan createStringSpan() {
        return new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightString));
    }
    
    private ForegroundColorSpan createOperatorSpan() {
        return new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightOperator));
    }
    
    private ForegroundColorSpan createCommentSpan() {
        return new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightComment));
    }
    
    private ForegroundColorSpan createNumberSpan() {
        return new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightNumber));
    }
}
