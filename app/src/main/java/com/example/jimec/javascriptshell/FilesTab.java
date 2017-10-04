package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    
    private ListView mExamplesListView;
    private TabFragmentAdapter mAdapter;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        mExamplesListView = view.findViewById(R.id.example_list);
        
        // Fill examples list from string array:
        mExamplesListView.setAdapter(ArrayAdapter.createFromResource(getContext(), R.array.examples_array, R.layout.view_examples_list_item));
        
        // Load example into editor when clicked:
        mExamplesListView.setOnItemClickListener(new OnExampleClickedListener());
        
        return view;
    }
    
    public void setAdapter(TabFragmentAdapter adapter) {
        mAdapter = adapter;
    }
    
    private class OnExampleClickedListener implements ListView.OnItemClickListener {
        
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mAdapter.loadExample(EXAMPLE_ARRAY_FILE_MAP[position]);
        }
    }
}
