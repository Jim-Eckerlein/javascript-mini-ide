package com.example.jimec.javascriptshell.files;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.jimec.javascriptshell.FilesTab;
import com.example.jimec.javascriptshell.R;
import com.example.jimec.javascriptshell.Util;

public class FileView extends FrameLayout {
    
    private FilesTab mFilesTab;
    private TextView mFilenameText;
    private CheckBox mFileSelectBox;
    private ViewGroup mFileSettings;
    private ViewGroup mRoot;
    private ObjectAnimator mFilenameTextAnimation;
    private boolean mFileSettingsShown = false;
    private boolean mFileSelectBoxShown = false;
    private OnSelectedListener mOnSelectedListener;
    
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
    
    public static FileView create(Context context, FilesTab filesTab, String filenameTitle, OnSelectedListener selectedListener) {
        FileView view = new FileView(context);
        view.mFilenameText.setText(filenameTitle);
        view.mFilesTab = filesTab;
        view.mOnSelectedListener = selectedListener;
        return view;
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_file, this);
        mRoot = findViewById(R.id.file_row);
        mFilenameText = findViewById(R.id.file_name);
        mFileSettings = findViewById(R.id.file_settings);
        mFileSelectBox = findViewById(R.id.file_select_box);
    
        findViewById(R.id.file_settings_opener).setOnClickListener(v -> showFileSettings());
        findViewById(R.id.file_settings_closer).setOnClickListener(v -> hideFileSettings());
        
        // Listen when user clicks the file name to open that file:
        mRoot.setOnClickListener(v -> {
            if (!mFileSettingsShown) {
                // File settings are not opened currently, so open file:
                mFilesTab.openFile(getFilename());
            }
            else {
                // File settings are opened currently, so close the settings:
                hideFileSettings();
            }
        });
    
        // Long click on file opens file settings:
        mRoot.setOnLongClickListener(v -> {
            showFileSettings();
            return true;
        });
    
        // Delete button:
        findViewById(R.id.file_delete).setOnClickListener(v -> {
            // Delete file, open "are you sure" dialog:
            new AlertDialog.Builder(getContext()).setTitle(R.string.delete)
                    .setMessage(getContext().getString(R.string.files_are_you_sure_to_delete_single_file, getFilename()))
                    .setPositiveButton(R.string.ok, (dialog, which) -> mFilesTab.deleteFile(getFilename()))
                    .setNegativeButton(R.string.cancel, ((dialog, which) -> hideFileSettings()))
                    .show();
        });
        
        // Rename button:
        findViewById(R.id.file_rename).setOnClickListener(v -> {
            // Rename file:
            FileNameDialog.show(getContext(), filename -> {
                //FilesManager.getInstance().rename(getFilename(), filename);
                mFilesTab.renameFile(getFilename(), filename);
                mFilenameText.setText(filename);
                hideFileSettings();
            }, this::hideFileSettings);
        });
    
        // Pre-baked animations:
        mFilenameTextAnimation = ObjectAnimator.ofInt(mFilenameText, "textColor",
                mFilenameText.getCurrentTextColor(),
                ContextCompat.getColor(getContext(), R.color.keyInverseColor));
        mFilenameTextAnimation.setEvaluator(new ArgbEvaluator());
    
        // Select listeners:
        mFileSelectBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                mOnSelectedListener.onFileViewSelected(this);
            }
            else {
                mOnSelectedListener.onFileViewDeselected(this);
            }
        });
        
        post(() -> {
            // Hide file settings and select box initially:
            mFileSettings.setX(mRoot.getWidth());
            mFileSelectBox.setX(-mFileSelectBox.getWidth());
        });
    }
    
    private void showFileSettings() {
        if (!mFileSelectBoxShown && !mFileSettingsShown) {
            mFileSettings.animate().setDuration(Util.ANIMATION_DURATION).x(mRoot.getWidth() - mFileSettings.getWidth());
            ((TransitionDrawable) mRoot.getBackground()).startTransition(Util.ANIMATION_DURATION);
            mFilenameTextAnimation.start();
            mFileSettingsShown = true;
        }
    }
    
    public void hideFileSettings() {
        if (!mFileSelectBoxShown && mFileSettingsShown) {
            mFileSettings.animate().setDuration(Util.ANIMATION_DURATION).x(mRoot.getWidth());
            ((TransitionDrawable) mRoot.getBackground()).reverseTransition(Util.ANIMATION_DURATION);
            mFilenameTextAnimation.reverse();
            mFileSettingsShown = false;
        }
    }
    
    public void showFileSelectBox() {
        if (mFileSettingsShown) {
            hideFileSettings();
        }
        
        // Show select box:
        mFileSelectBox.animate().setDuration(Util.ANIMATION_DURATION)
                .x(((MarginLayoutParams) mFileSelectBox.getLayoutParams()).leftMargin);
        
        // Push filename text:
        mFilenameText.animate().setDuration(Util.ANIMATION_DURATION)
                .x(mFileSelectBox.getWidth() + ((MarginLayoutParams) mFileSelectBox.getLayoutParams()).leftMargin);
        
        mFileSelectBoxShown = true;
    }
    
    public void hideFileSelectBox() {
        // Hide select box:
        mFileSelectBox.animate().setDuration(Util.ANIMATION_DURATION).x(-mFileSelectBox.getWidth());
        mFileSelectBox.setChecked(false);
        
        // Pull filename text:
        mFilenameText.animate().setDuration(Util.ANIMATION_DURATION).x(0);
        
        mFileSelectBoxShown = false;
    }
    
    public String getFilename() {
        return mFilenameText.getText().toString();
    }
    
    public interface OnSelectedListener {
        
        void onFileViewSelected(FileView fileView);
        
        void onFileViewDeselected(FileView fileView);
        
    }
    
}
