package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jimec.javascriptshell.editor.EditorView;
import com.example.jimec.javascriptshell.keyboard_view.Keyboard;

public class EditorTab extends Fragment {
    
    public static final String TITLE = "Editor";
    private int mCurrentDemo = -1;
    private EditorView mEditor;
    private ViewPager mViewPager;
    
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
    
        // Keyboard:
        Keyboard keyboard = view.findViewById(R.id.keyboard);
        keyboard.setEditor(mEditor);
    
        // Load initial demo:
        loadDemo(R.raw.demo_demo);
    
        view.findViewById(R.id.run_code_key).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(TabFragmentAdapter.FRAGMENT_POSITION_RUN);
            }
        });
        
        return view;
    }
    
    private void loadDemo(@RawRes final int id) {
        if (id != mCurrentDemo) {
            mEditor.clear();
            mEditor.write(Util.readTextFile(getContext(), id));
            mEditor.setCursor(0);
            mCurrentDemo = id;
        }
    }
    
    public EditorView getEditor() {
        return mEditor;
    }
    
}
