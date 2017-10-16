package io.jim_eckerlein.jsshell.files;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import io.jim_eckerlein.jsshell.R;


public class FileNameEditText extends AppCompatEditText implements TextWatcher {
    
    private ErrorListener mErrorListener;
    
    public FileNameEditText(Context context) {
        super(context);
        init();
    }
    
    public FileNameEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public FileNameEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        addTextChangedListener(this);
    }
    
    public void setErrorListener(ErrorListener errorListener) {
        mErrorListener = errorListener;
    }
    
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
    
    @Override
    public void afterTextChanged(Editable s) {
        String filename = s.toString();
        if (!filename.matches("[a-zA-Z0-9 _-]*")) {
            mErrorListener.onFileNameContainsInvalidCharacter();
            return;
        }
        filename = filename + getContext().getString(R.string.files_extension);
        for (String existingFilename : FilesManager.getInstance().listFiles()) {
            if (existingFilename.equals(filename)) {
                mErrorListener.onFileNameAlreadyExists();
                return;
            }
        }
        mErrorListener.onFileNameIsOk();
    }
    
    public interface ErrorListener {
        
        void onFileNameIsOk();
        
        void onFileNameAlreadyExists();
        
        void onFileNameContainsInvalidCharacter();
        
    }
    
}
