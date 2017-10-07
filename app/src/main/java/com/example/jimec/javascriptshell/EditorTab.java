package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jimec.javascriptshell.editor.EditorView;
import com.example.jimec.javascriptshell.keyboard_view.Keyboard;

public class EditorTab extends Fragment {
    
    private EditorView mEditor;
    private ViewPager mViewPager;
    private TextView mCurrentFileName;
    
    public void setPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editor, container, false);
    
        // Editor:
        mEditor = view.findViewById(R.id.editor);
        mCurrentFileName = view.findViewById(R.id.current_file_name);
    
        // Keyboard:
        Keyboard keyboard = view.findViewById(R.id.keyboard);
        keyboard.setEditor(mEditor);
    
        // Load initial demo:
        loadExample(R.raw.example_demo);
    
        view.findViewById(R.id.run_code_key).setOnClickListener(v -> mViewPager.setCurrentItem(TabManager.FRAGMENT_POSITION_RUN));
        
        return view;
    }
    
    public void loadExample(@RawRes final int id) {
        mEditor.clear();
        mEditor.write(Util.readTextFile(getContext(), id));
        mEditor.moveCursorToStart();
    }
    
    public EditorView getEditor() {
        return mEditor;
    }
    
    public void loadFile(String content) {
        mEditor.clear();
        mEditor.write(content);
        mEditor.moveCursorToStart();
    }
    
    public void setCurrentFileName(String filename) {
        mCurrentFileName.setText(filename);
    }
}
