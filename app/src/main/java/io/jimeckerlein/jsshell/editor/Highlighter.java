package io.jimeckerlein.jsshell.editor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SparseArrayCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import io.jimeckerlein.jsshell.R;

/**
 * The class generating text spans (http://flavienlaurent.com/blog/2014/01/31/spans/) to colorized code.
 * The actual parser is *not* written in Java but in C++,
 * so this class is more like an interface exposing the results generated in C++ to Java.
 */
public class Highlighter {

    private static final int KEYWORD_SPAN = 0;
    private static final int STRING_SPAN = 1;
    private static final int OPERATOR_SPAN = 2;
    private static final int COMMENT_SPAN = 3;
    private static final int NUMBER_SPAN = 4;

    private static final String TAG = "JavaHighlighter";

    private final SpannableStringBuilder mSpanBuilder = new SpannableStringBuilder();
    private final Context mContext;
    private final SparseArrayCompat<SpanCache<ForegroundColorSpan>> mSpanCaches = new SparseArrayCompat<>();

    public Highlighter(Context context) {
        mContext = context;
        mSpanCaches.append(KEYWORD_SPAN, new SpanCache<>(() -> new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightKeyword))));
        mSpanCaches.append(STRING_SPAN, new SpanCache<>(() -> new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightString))));
        mSpanCaches.append(OPERATOR_SPAN, new SpanCache<>(() -> new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightOperator))));
        mSpanCaches.append(COMMENT_SPAN, new SpanCache<>(() -> new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightComment))));
        mSpanCaches.append(NUMBER_SPAN, new SpanCache<>(() -> new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.highlightNumber))));
        initHighlighter();
    }

    /**
     * Ran once to initialize the native highlighter
     */
    private native void initHighlighter();

    /**
     * Emits the native highlighter
     *
     * @param code Code to be highlighted
     * @return Highlighted code
     */
    public Spannable highlight(String code) {
        // Re-initialize span builder:
        mSpanBuilder.clear();
        mSpanBuilder.clearSpans();
        mSpanBuilder.append(code);

        // Reset span buffer:
        mSpanCaches.get(KEYWORD_SPAN).reset();
        mSpanCaches.get(STRING_SPAN).reset();
        mSpanCaches.get(OPERATOR_SPAN).reset();
        mSpanCaches.get(COMMENT_SPAN).reset();
        mSpanCaches.get(NUMBER_SPAN).reset();

        if (!findHighlights(code)) {
            // Something went wrong
            Log.e(TAG, "ERROR on finding Highlights");
        }

        return mSpanBuilder;
    }

    private native boolean findHighlights(String code);

    /**
     * Called from C with span data array, which aligned behind each other.
     * @param length Length of each part array, therefore: {@code data.length() == 3 * length}
     * @param data Span data array laid out in that configuration: types, starts, ends (not interleaving but sequentially)
     */
    @SuppressWarnings("unused")
    public void setSpans(int length, int[] data) {
        for(int i = 0; i < length; i++) {
            int type = data[i];
            int start = data[length + i];
            int end = data[2 * length + i];
            mSpanBuilder.setSpan(createSpan(type), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    private ForegroundColorSpan createSpan(int spanType) {
        return mSpanCaches.get(spanType).generate();
    }
}
