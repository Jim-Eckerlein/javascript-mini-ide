package io.jimeckerlein.jsshell.editor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.security.InvalidParameterException;

import io.jimeckerlein.jsshell.R;

public class Highlighter {
    
    private static final int KEYWORD_SPAN = 0;
    private static final int STRING_SPAN = 1;
    private static final int OPERATOR_SPAN = 2;
    private static final int COMMENT_SPAN = 3;
    private static final int NUMBER_SPAN = 4;
    
    private static final String TAG = "Highlighter";
    
    private final SpannableStringBuilder mSpanBuilder = new SpannableStringBuilder();
    private final Context mContext;
    private final ReallocatingIntBuffer mHighlightSpanTypeBuffer = new ReallocatingIntBuffer(100);
    private final ReallocatingIntBuffer mHighlightSpanStartBuffer = new ReallocatingIntBuffer(100);
    private final ReallocatingIntBuffer mHighlightSpanEndBuffer = new ReallocatingIntBuffer(100);
    
    public Highlighter(Context context) {
        mContext = context;
    }
    
    public Spannable highlight(String code) {
        // Re-initialize span builder:
        mSpanBuilder.clear();
        mSpanBuilder.clearSpans();
        mSpanBuilder.append(code);
    
        mHighlightSpanTypeBuffer.startWriting();
        mHighlightSpanStartBuffer.startWriting();
        mHighlightSpanEndBuffer.startWriting();
        
        if(!findHighlights(code, mHighlightSpanTypeBuffer, mHighlightSpanStartBuffer, mHighlightSpanEndBuffer)) {
            // Something went wrong
            Log.e(TAG, "ERROR on finding Highlights");
        }
    
        mHighlightSpanTypeBuffer.startReading();
        mHighlightSpanStartBuffer.startReading();
        mHighlightSpanEndBuffer.startReading();
        
        while(mHighlightSpanTypeBuffer.hasRemaining()) {
            mSpanBuilder.setSpan(
                    createSpan(mHighlightSpanTypeBuffer.get()),
                    mHighlightSpanStartBuffer.get(),
                    mHighlightSpanEndBuffer.get(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    
        Log.e(TAG, "Highlight positions: " + mHighlightSpanStartBuffer);
        
        
        return mSpanBuilder;
    }
    
    private native boolean findHighlights(String code, ReallocatingIntBuffer typeBuffer, ReallocatingIntBuffer startBuffer, ReallocatingIntBuffer endBuffer);
    
    private ForegroundColorSpan createSpan(int spanType) {
        switch (spanType) {
            case KEYWORD_SPAN:
                return new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightKeyword));
            
            case STRING_SPAN:
                return new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightString));
            
            case OPERATOR_SPAN:
                return new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightOperator));
            
            case COMMENT_SPAN:
                return new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightComment));
            
            case NUMBER_SPAN:
                return new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightNumber));
            
            default:
                throw new InvalidParameterException("Unknown span type: " + spanType);
        }
    }
}
