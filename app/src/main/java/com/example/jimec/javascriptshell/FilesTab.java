package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.jimec.javascriptshell.files.ExampleView;
import com.example.jimec.javascriptshell.files.FileNameDialog;
import com.example.jimec.javascriptshell.files.FileView;
import com.example.jimec.javascriptshell.files.FilesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FilesTab extends Fragment implements FileView.OnSelectedListener {
    
    private static final int[] EXAMPLE_ARRAY_FILE_MAP = new int[]{
            R.raw.example_demo,
            R.raw.example_print,
            R.raw.example_array,
            R.raw.example_object,
            R.raw.example_function,
            R.raw.example_time,
            R.raw.example_typeof
    };
    private final HashMap<String, FileView> mFileViews = new HashMap<>();
    private final ArrayList<FileView> mSelectedFileViews = new ArrayList<>();
    private LinearLayout mFilesView;
    private TabManager mTabManager;
    private FloatingActionButton mFab;
    private boolean mActiveMultipleFileDeletion = false;
    private ViewGroup mMultipleFileDeletionBar;
    private TextView mMultipleFileDeletionCounter;
    private ScrollView mScroller;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        mScroller = view.findViewById(R.id.files_scroller);
        
        // Fill examples list from string array:
        LinearLayout examplesView = view.findViewById(R.id.example_list);
        int position = 0;
        for (String exampleName : getResources().getStringArray(R.array.files_examples_array)) {
            examplesView.addView(ExampleView.create(getContext(), mTabManager, EXAMPLE_ARRAY_FILE_MAP[position++], exampleName));
        }
    
        // Load user files:
        mFilesView = view.findViewById(R.id.user_file_list);
        for (String filename : FilesManager.getInstance().listFiles()) {
            FileView fileView = FileView.create(getContext(), this, filename, this);
            mFileViews.put(filename, fileView);
            mFilesView.addView(fileView);
        }
    
        // Initialize fab:
        mFab = view.findViewById(R.id.files_fab);
        mFab.setOnClickListener(v -> FileNameDialog.show(getContext(), this::createFile, () -> {}));
        
        // Multiple file deletion:
        mMultipleFileDeletionBar = view.findViewById(R.id.files_multiple_file_deletion_bar);
        mMultipleFileDeletionCounter = view.findViewById(R.id.files_multiple_file_deletion_counter);
        view.findViewById(R.id.files_multiple_file_deletion_cancel_button).setOnClickListener(v -> endMultipleFileDeletion());
        view.findViewById(R.id.files_multiple_file_deletion_delete_button).setOnClickListener(v -> {
            for (FileView fileView : mSelectedFileViews) {
                deleteFile(fileView.getFilename());
            }
            endMultipleFileDeletion();
        });
    
        view.post(() -> {
            // Hide multiple file selection bar initially:
            mMultipleFileDeletionBar.setY(mScroller.getHeight());
            
            // Load session file:
            mTabManager.loadSession();
        });
        
        return view;
    }
    
    public void setTabManager(TabManager tabManager) {
        mTabManager = tabManager;
    }
    
    public void openFile(String filename) {
        try {
            mTabManager.loadFile(filename, FilesManager.getInstance().read(filename));
        } catch (IOException e) {
            e.printStackTrace();
    
            // Create error notifier dialog
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.error)
                    .setMessage(R.string.files_error_cannot_open_file)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                    })
                    .show();
        }
    }
    
    public void openDefaultExample() {
        mTabManager.loadExample(getResources().getStringArray(R.array.files_examples_array)[0], R.raw.example_demo);
    }
    
    public void writeFile(String filename, String editorCode) {
        try {
            FilesManager.getInstance().write(filename, editorCode);
        } catch (IOException e) {
            e.printStackTrace();
            
            // Create error notifier dialog
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.error)
                    .setMessage(R.string.files_error_cannot_save_file)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                    })
                    .show();
        }
    }
    
    /**
     * Creates a new file.
     * Appends the .js extension.
     *
     * @param filename Name of file to be created
     */
    private void createFile(String filename) {
        try {
            FilesManager.getInstance().create(filename);
            FileView fileView = FileView.create(getContext(), this, filename, this);
            mFileViews.put(filename, fileView);
            mFilesView.addView(fileView);
        } catch (IOException e) {
            e.printStackTrace();
    
            // Create error notifier dialog
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.error)
                    .setMessage(R.string.files_error_cannot_create_file)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                    })
                    .show();
        }
    }
    
    public void deleteFile(String filename) {
        try {
            FilesManager.getInstance().delete(filename);
            mFileViews.remove(filename);
            mFilesView.removeView(mFileViews.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
            
            // Create error notifier dialog
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.error)
                    .setMessage(R.string.files_error_cannot_delete_file)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                    })
                    .show();
        }
    }
    
    public void renameFile(String oldFilename, String newFilename) {
        FileView fileView = mFileViews.get(oldFilename);
        mFileViews.remove(oldFilename);
        mFileViews.put(newFilename, fileView);
        
        try {
            FilesManager.getInstance().rename(oldFilename, newFilename);
        } catch (IOException e) {
            e.printStackTrace();
            
            // Create error notifier dialog
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.error)
                    .setMessage(R.string.files_error_cannot_rename_file)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                    })
                    .show();
        }
    }
    
    public void startMultipleFileDeletion() {
        if (mActiveMultipleFileDeletion) {
            return;
        }
        mActiveMultipleFileDeletion = true;
        
        // Hide fab:
        mFab.animate().setDuration(Util.ANIMATION_DURATION)
                .x(mScroller.getWidth())
                .withStartAction(() -> mFab.setEnabled(false));
        
        // Show selection bar:
        mMultipleFileDeletionBar.animate().setDuration(Util.ANIMATION_DURATION)
                .y(mScroller.getHeight() - mMultipleFileDeletionBar.getHeight());
        
        // Show selection box for each file view:
        for (FileView view : mFileViews.values()) {
            view.showFileSelectBox();
        }
        
        // Initialize counter text to 0:
        updateMultipleFileDeletionCounterText();
    }
    
    private void endMultipleFileDeletion() {
        if (!mActiveMultipleFileDeletion) {
            return;
        }
        mActiveMultipleFileDeletion = false;
        
        // Show fab:
        mFab.animate().setDuration(Util.ANIMATION_DURATION)
                .x(mScroller.getWidth() - mFab.getWidth() - ((ViewGroup.MarginLayoutParams) mFab.getLayoutParams()).rightMargin)
                .withEndAction(() -> mFab.setEnabled(true));
        
        // Hide selection bar:
        mMultipleFileDeletionBar.animate().setDuration(Util.ANIMATION_DURATION)
                .y(mScroller.getHeight());
        
        // Hide selection box for each file view:
        for (FileView view : mFileViews.values()) {
            view.hideFileSelectBox();
        }
        
        // Cleanup:
        mSelectedFileViews.clear();
    }
    
    private void updateMultipleFileDeletionCounterText() {
        int count = mSelectedFileViews.size();
        mMultipleFileDeletionCounter.setText(getResources().getQuantityString(R.plurals.files_multiple_files_selected, count, count));
    }
    
    @Override
    public void onFileViewSelected(FileView fileView) {
        // File view selected during multiple file deletion:
        mSelectedFileViews.add(fileView);
        updateMultipleFileDeletionCounterText();
    }
    
    @Override
    public void onFileViewDeselected(FileView fileView) {
        // File view deselected during multiple file deletion:
        mSelectedFileViews.remove(fileView);
        updateMultipleFileDeletionCounterText();
    }
}
