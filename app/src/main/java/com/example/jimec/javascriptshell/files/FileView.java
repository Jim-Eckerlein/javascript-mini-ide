package com.example.jimec.javascriptshell.files;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.jimec.javascriptshell.R;
import com.example.jimec.javascriptshell.TabFragmentAdapter;

public class FileView extends FrameLayout implements View.OnClickListener {
    
    private TabFragmentAdapter mAdapter;
    private TextView mFilenameText;
    
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
    
    private void init() {
        inflate(getContext(), R.layout.view_user_file_list_item, this);
        setOnClickListener(this);
        mFilenameText = findViewById(R.id.filename);
    }
    
    @Override
    public void onClick(View v) {
    }
    
    public void setAdapter(TabFragmentAdapter adapter) {
        mAdapter = adapter;
    }
    
    public void setFilename(String filename) {
        mFilenameText.setText(filename);
    }
}
