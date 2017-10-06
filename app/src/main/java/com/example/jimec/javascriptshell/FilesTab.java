package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jimec.javascriptshell.files.ExampleView;
import com.example.jimec.javascriptshell.files.FileView;
import com.example.jimec.javascriptshell.files.FilesManager;

public class FilesTab extends Fragment {
    
    public static final String TITLE = "Files";
    public static final int[] EXAMPLE_ARRAY_FILE_MAP = new int[]{
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
    private TabFragmentAdapter mAdapter;
    private FilesManager mFilesManager;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
    
        // Load user files:
        mFilesView = view.findViewById(R.id.user_file_list);
        mFilesManager = new FilesManager(getContext());
        for (String filename : mFilesManager.listFiles()) {
            mFilesView.addView(FileView.create(getContext(), mAdapter, mFilesManager, filename));
        }
    
        // Fill examples list from string array:
        mExamplesView = view.findViewById(R.id.example_list);
        int position = 0;
        for (String example : getResources().getStringArray(R.array.examples_array)) {
            ExampleView exampleView = new ExampleView(getContext());
        
            exampleView.setText(example);
            exampleView.setAdapter(mAdapter);
            exampleView.setRes(EXAMPLE_ARRAY_FILE_MAP[position++]);
        
            mExamplesView.addView(exampleView);
        }
        
        return view;
    }
    
    public void setAdapter(TabFragmentAdapter adapter) {
        mAdapter = adapter;
    }
    
    /*private class FileItemAdapter extends ArrayAdapter<String> {
        
        private String[] mFilenames;
        
        FileItemAdapter(Context context, String[] filenames) {
            super(context, R.layout.view_user_file_list_item, filenames);
            mFilenames = filenames;
        }
    
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.view_user_file_list_item, parent, false);
            ((TextView) view.findViewById(R.id.filename)).setText(mFilenames[position]);
            return view;
        }
    }*/
}
