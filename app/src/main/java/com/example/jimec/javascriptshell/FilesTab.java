package com.example.jimec.javascriptshell;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jimec.javascriptshell.files.CreateFileDialogFab;
import com.example.jimec.javascriptshell.files.ExampleView;
import com.example.jimec.javascriptshell.files.FileView;
import com.example.jimec.javascriptshell.files.FilesManager;

import java.io.IOException;
import java.util.HashMap;

public class FilesTab extends Fragment {
    
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
    private LinearLayout mExamplesView;
    private LinearLayout mFilesView;
    private TabManager mTabManager;
    private FilesManager mFilesManager;
    private FloatingActionButton mFab;
    private CreateFileDialogFab mCreateFileDialog;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
    
        // Fill examples list from string array:
        mExamplesView = view.findViewById(R.id.example_list);
        int position = 0;
        for (String example : getResources().getStringArray(R.array.examples_array)) {
            ExampleView exampleView = new ExampleView(getContext());
        
            exampleView.setText(example);
            exampleView.setTabManager(mTabManager);
            exampleView.setRes(EXAMPLE_ARRAY_FILE_MAP[position++]);
        
            mExamplesView.addView(exampleView);
        }
    
        // Load user files:
        mFilesView = view.findViewById(R.id.user_file_list);
        mFilesManager = new FilesManager(getContext());
        for (String filename : mFilesManager.listFiles()) {
            FileView fileView = FileView.create(getContext(), this, filename);
            mFileViews.put(filename, fileView);
            mFilesView.addView(fileView);
        }
    
        // Initialize fab:
        mCreateFileDialog = new CreateFileDialogFab(getContext(), mFilesManager, this, getActivity()) {
            @Override
            public void onOk(String filename) {
                createFile(filename);
            }
        };
        mFab = view.findViewById(R.id.files_fab);
        mFab.setOnClickListener(mCreateFileDialog);
        
        return view;
    }
    
    public void setTabManager(TabManager tabManager) {
        mTabManager = tabManager;
    }
    
    public void openFile(String filename) {
        try {
            mTabManager.loadFile(filename, mFilesManager.readFile(filename));
        } catch (IOException e) {
            e.printStackTrace();
    
            // Create error notifier dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.error);
            builder.setMessage(R.string.files_error_cannot_open_file);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
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
            mFilesManager.createFile(filename);
            FileView fileView = FileView.create(getContext(), this, filename);
            mFileViews.put(filename, fileView);
            mFilesView.addView(fileView);
        } catch (IOException e) {
            e.printStackTrace();
    
            // Create error notifier dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.error);
            builder.setMessage(R.string.files_error_cannot_create_file);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
    }
    
    public void deleteFile(String filename) {
        try {
            mFilesManager.deleteFile(filename);
            mFilesView.removeView(mFileViews.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
            
            // Create error notifier dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.error);
            builder.setMessage(R.string.files_error_cannot_delete_file);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
    }
}
