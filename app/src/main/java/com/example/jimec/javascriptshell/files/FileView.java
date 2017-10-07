package com.example.jimec.javascriptshell.files;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
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
    
        findViewById(R.id.file_settings_opener).setOnClickListener(v -> openFileSettings());
        findViewById(R.id.file_settings_closer).setOnClickListener(v -> closeFileSettings());
    
        // Listen when user clicks the file name to open that file:
        mFileViewRow.setOnClickListener(v -> {
            if (!mFileSettingsOpened) {
                // File settings are not opened currently, so open file:
                mFilesTab.openFile(mFilenameText.getText().toString());
            }
            else {
                // File settings are opened currently, so close the settings:
                closeFileSettings();
            }
        });
    
        // Long click on file opens file settings:
        mFileViewRow.setOnLongClickListener(v -> {
            openFileSettings();
            return true;
        });
    
        findViewById(R.id.file_delete).setOnClickListener(v -> {
            // Delete file, open "are you sure" dialog:
            new AlertDialog.Builder(getContext()).setTitle(R.string.delete)
                    .setMessage(getContext().getString(R.string.files_are_you_sure_to_delete_single_file, mFilenameText.getText().toString()))
                    .setPositiveButton(R.string.ok, (dialog, which) -> mFilesTab.deleteFile(mFilenameText.getText().toString()))
                    .setNegativeButton(R.string.cancel, ((dialog, which) -> closeFileSettings()))
                    .show();
        });
        
        mFilenameTextAnimation = ObjectAnimator.ofInt(mFilenameText, "textColor",
                mFilenameText.getCurrentTextColor(),
                ContextCompat.getColor(getContext(), R.color.keyInverseColor));
        mFilenameTextAnimation.setEvaluator(new ArgbEvaluator());
    
        post(() -> {
            // Hide file settings initially:
            mFileViewSettings.setX(mFileViewRow.getWidth());
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
    
}
