package io.jimeckerlein.jsshell.editor;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Spannable;
import android.widget.EditText;

/**
 * The asynchronous task launching the highlighter, so that the highlighting process
 * runs independently from the UI thread, avoid UI performance issues.
 */
public class HighlighterTask extends AsyncTask<HighlighterTask.Params, Integer, HighlighterTask.Params> {

    private final Highlighter mHighlighter;
    private final OnFinishListener mOnFinish;

    HighlighterTask(Context context, EditText editText, OnFinishListener onFinish) {
        mHighlighter = new Highlighter(context);
        mOnFinish = onFinish;
    }

    @Override
    protected Params doInBackground(Params... params) {
        String code = params[0].mCode;
        params[0].mHighlightedCode = mHighlighter.highlight(code);
        return params[0];
    }

    @Override
    protected void onPostExecute(Params result) {
        /*mEditText.setText(result.mHighlightedCode);
        mEditText.setSelection(result.mSelection);*/
        mOnFinish.run(result.mHighlightedCode, result.mSelection);
    }

    public interface OnFinishListener {
        void run(Spannable highlightedCode, int selection);
    }

    public static class Params {
        public String mCode;
        public int mSelection;
        public Spannable mHighlightedCode;

        public Params set(String code, int selection) {
            mCode = code;
            mSelection = selection;
            return this;
        }
    }

}
