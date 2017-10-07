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
    
    private static final int OPEN_FILE_SETTINGS_ANIMATION_DURATION = 140;
    
    private FilesTab mFilesTab;
    private TextView mFilenameText;
    private ViewGroup mFileViewSettings;
    private ViewGroup mFileViewRow;
    private ObjectAnimator mFilenameTextAnimation;
    private boolean mFileSettingsOpened = false;
    
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
        mFilenameText = findViewById(R.id.file_name);
        mFileViewRow = findViewById(R.id.file_row);
        mFileViewSettings = findViewById(R.id.file_settings);
    
        findViewById(R.id.file_settings_opener).setOnClickListener(new FileSettingsOpener());
        findViewById(R.id.file_settings_closer).setOnClickListener(new FileSettingsCloser());
        mFileViewRow.setOnClickListener(new FileOpener());
        mFileViewRow.setOnLongClickListener(new FileSettingsLongClickOpener());
    
        findViewById(R.id.file_delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete file
                mFilesTab.deleteFile(mFilenameText.getText().toString());
            }
        });
    
        mFilenameTextAnimation = ObjectAnimator.ofInt(mFilenameText, "textColor",
                mFilenameText.getCurrentTextColor(),
                ContextCompat.getColor(getContext(), R.color.keyInverseColor));
        mFilenameTextAnimation.setEvaluator(new ArgbEvaluator());
    
        post(new Runnable() {
            @Override
            public void run() {
                // Hide file settings initially:
                mFileViewSettings.setX(mFileViewRow.getWidth());
            }
        });
    }
    
    private void openFileSettings() {
        if (!mFileSettingsOpened) {
            mFileViewSettings.animate().setDuration(OPEN_FILE_SETTINGS_ANIMATION_DURATION).x(mFileViewRow.getWidth() - mFileViewSettings.getWidth());
            ((TransitionDrawable) mFileViewRow.getBackground()).startTransition(OPEN_FILE_SETTINGS_ANIMATION_DURATION);
            mFilenameTextAnimation.start();
            mFileSettingsOpened = true;
        }
    }
    
    public void closeFileSettings() {
        if (mFileSettingsOpened) {
            mFileViewSettings.animate().setDuration(OPEN_FILE_SETTINGS_ANIMATION_DURATION).x(mFileViewRow.getWidth());
            ((TransitionDrawable) mFileViewRow.getBackground()).reverseTransition(OPEN_FILE_SETTINGS_ANIMATION_DURATION);
            mFilenameTextAnimation.reverse();
            mFileSettingsOpened = false;
        }
    }
    
    /**
     * Listen when user clicks the file name to open that file.
     */
    private class FileOpener implements OnClickListener {
        
        @Override
        public void onClick(View v) {
            if (!mFileSettingsOpened) {
                // File settings are not opened currently, so open file:
                mFilesTab.openFile(mFilenameText.getText().toString());
            }
            else {
                // File settings are opened currently, so close the settings:
                closeFileSettings();
            }
        }
        
    }
    
    /**
     * Listen when user opens the file settings.
     */
    private class FileSettingsOpener implements OnClickListener {
        @Override
        public void onClick(View v) {
            openFileSettings();
        }
    }
    
    /**
     * Listen when user closes the file settings.
     */
    private class FileSettingsCloser implements OnClickListener {
        @Override
        public void onClick(View v) {
            closeFileSettings();
        }
    }
    
    /**
     * User opens file settings using long click.
     */
    private class FileSettingsLongClickOpener implements OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            openFileSettings();
            return true;
        }
    }
}
