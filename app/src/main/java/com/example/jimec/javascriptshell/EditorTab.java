package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jimec.javascriptshell.editor.EditorView;
import com.example.jimec.javascriptshell.keyboard_view.Keyboard;

public class EditorTab extends Fragment {
    
    public static final String TITLE = "Editor";
    private int mCurrentDemo = -1;
    private TextView mKeyboardHelper;
    private EditorView mEditor;
    
    public static EditorTab newInstance() {
        return new EditorTab();
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editor, container, false);
    
        // Editor:
        mEditor = (EditorView) view.findViewById(R.id.editor);
    
        // Keyboard:
        Keyboard keyboard = (Keyboard) view.findViewById(R.id.keyboard);
        keyboard.setEditor(mEditor);
    
        // Load initial demo:
        loadDemo(R.raw.demo_demo);
    
        mKeyboardHelper = (TextView) view.findViewById(R.id.keyboard_helper);
        
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
    
    public void closeKeyboardHelper(View view) {
        mKeyboardHelper.setVisibility(View.GONE);
    }
}
