package com.example.jimec.javascriptshell.files;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.jimec.javascriptshell.FilesTab;
import com.example.jimec.javascriptshell.R;

public class FileView extends FrameLayout {
    
    private FilesTab mFilesTab;
    private TextView mFilenameText;
    private ViewGroup mFileViewSettings;
    private ViewGroup mFileViewRoot;
    private ViewGroup mFileViewRow;
    private ObjectAnimator mFilenameTextAnimation;
    
    public FileView(Context context) {
        super(context);
        init();
    }
    
    public FileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public FileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    public static FileView create(Context context, FilesTab filesTab, String filenameTitle) {
        FileView view = new FileView(context);
        view.mFilenameText.setText(filenameTitle);
        view.mFilesTab = filesTab;
        return view;
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_file, this);
        setOnClickListener(new FileOpener());
        mFileViewRoot = findViewById(R.id.file_root);
        mFilenameText = findViewById(R.id.file_name);
        mFileViewRow = findViewById(R.id.file_row);
        mFileViewSettings = findViewById(R.id.file_settings);
    
        findViewById(R.id.file_settings_opener).setOnClickListener(new FileSettingsOpener());
        findViewById(R.id.file_settings_closer).setOnClickListener(new FileSettingsCloser());
        mFileViewRow.setOnLongClickListener(new FileSettingsLongClickOpener());
    
        mFilenameTextAnimation = ObjectAnimator.ofInt(mFilenameText, "textColor",
                mFilenameText.getCurrentTextColor(),
                ContextCompat.getColor(getContext(), R.color.keyInverseColor));
        mFilenameTextAnimation.setEvaluator(new ArgbEvaluator());
    
        post(new Runnable() {
            @Override
            public void run() {
                // Hide file settings initially:
                mFileViewSettings.setX(mFileViewRoot.getWidth());
            }
        });
    }
    
    /**
     * Listen when user clicks the file name to open that file.
     */
    private class FileOpener implements OnClickListener {
        
        @Override
        public void onClick(View v) {
            mFilesTab.openFile(mFilenameText.getText().toString());
        }
        
    }
    
    /**
     * Listen when user opens the file settings.
     */
    private class FileSettingsOpener implements OnClickListener {
        @Override
        public void onClick(View v) {
            mFileViewSettings.animate().setDuration(200).x(mFileViewRoot.getWidth() - mFileViewSettings.getWidth());
            ((TransitionDrawable) mFileViewRow.getBackground()).startTransition(200);
            mFilenameTextAnimation.start();
        }
    }
    
    /**
     * Listen when user closes the file settings.
     */
    private class FileSettingsCloser implements OnClickListener {
        @Override
        public void onClick(View v) {
            mFileViewSettings.animate().setDuration(200).x(mFileViewRoot.getWidth());
            ((TransitionDrawable) mFileViewRow.getBackground()).reverseTransition(200);
            mFilenameTextAnimation.reverse();
        }
    }
    
    /**
     * User opens file settings using long click.
     */
    private class FileSettingsLongClickOpener implements OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            mFileViewSettings.animate().setDuration(200).x(mFileViewRoot.getWidth() - mFileViewSettings.getWidth());
            ((TransitionDrawable) mFileViewRow.getBackground()).startTransition(200);
            mFilenameTextAnimation.start();
            return true;
        }
    }
}
