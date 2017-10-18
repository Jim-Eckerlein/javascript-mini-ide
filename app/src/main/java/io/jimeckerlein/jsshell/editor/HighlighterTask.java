package io.jimeckerlein.jsshell.editor;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Spannable;
import android.widget.EditText;

public class HighlighterTask extends AsyncTask<HighlighterTask.Params, Integer, HighlighterTask.Params> {
    
    private final Highlighter mHighlighter;
    private final EditText mEditText;
    
    HighlighterTask(Context context, EditText editText) {
        mHighlighter = new Highlighter(context);
        mEditText = editText;
    }
    
    @Override
    protected Params doInBackground(Params... params) {
        String code = params[0].mCode;
        params[0].mHighlightedCode = mHighlighter.highlight(code);
        return params[0];
    }
    
    @Override
    protected void onPostExecute(Params result) {
        mEditText.setText(result.mHighlightedCode);
        mEditText.setSelection(result.mSelection);
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
