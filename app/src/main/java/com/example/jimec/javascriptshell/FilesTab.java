package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jimec.javascriptshell.files.CreateFileDialogFab;
import com.example.jimec.javascriptshell.files.ExampleView;
import com.example.jimec.javascriptshell.files.FileView;
import com.example.jimec.javascriptshell.files.FilesManager;

import java.io.IOException;
import java.security.InvalidParameterException;

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
        updateFileList(view);
    
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
            // todo: replace with user popup
            throw new RuntimeException();
        }
    }
    
    private void updateFileList(View rootView) {
        mFilesView = rootView.findViewById(R.id.user_file_list);
        mFilesView.removeAllViews();
        mFilesManager = new FilesManager(getContext());
        for (String filename : mFilesManager.listFiles()) {
            mFilesView.addView(FileView.create(getContext(), this, filename));
        }
    }
    
    /**
     * Creates a new file.
     * Appends the .js extension.
     *
     * @param filename Name of file to be created
     */
    private void createFile(String filename) {
        if (mCreateFileDialog.isValidFilename(filename) != CreateFileDialogFab.FILE_NAME_NO_ERROR) {
            throw new InvalidParameterException("Not a valid filename: " + filename);
        }
        try {
            mFilesManager.createFile(filename + getString(R.string.files_extension));
        } catch (IOException e) {
            e.printStackTrace();
            // todo: replace with user popup
            throw new RuntimeException();
        }
        
        updateFileList(getView());
    }
    
}
