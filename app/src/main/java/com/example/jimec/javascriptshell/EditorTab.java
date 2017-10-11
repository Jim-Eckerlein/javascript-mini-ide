package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jimec.javascriptshell.editor.HighlighterEditText;
import com.example.jimec.javascriptshell.keyboard.KeyboardView;

public class EditorTab extends Fragment {
    
    private HighlighterEditText mEditor;
    private ViewPager mViewPager;
    private TextView mCurrentFileName;
    private boolean mCurrentFileIsExample = false;
    private boolean mHasOpenedFile = false;
    private ConstraintSet mConstraintSetOriginal = new ConstraintSet();
    private ConstraintSet mConstraintSetHiddenKeyboard = new ConstraintSet();
    private ConstraintLayout mRoot;
    
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
    
        mRoot = view.findViewById(R.id.editor_root);
        mConstraintSetOriginal.clone(mRoot);
        mConstraintSetHiddenKeyboard.clone(getContext(), R.layout.fragment_editor_hidden);
        
        // Keyboard:
        KeyboardView keyboardView = view.findViewById(R.id.keyboard);
        keyboardView.setEditor(mEditor);
    
        keyboardView.setOnHideKeyboardListener(() -> {
            TransitionManager.beginDelayedTransition(mRoot);
            mConstraintSetHiddenKeyboard.applyTo(mRoot);
        });
        view.findViewById(R.id.show_keyboard).setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(mRoot);
            mConstraintSetOriginal.applyTo(mRoot);
        });
        
        view.findViewById(R.id.run_code_key).setOnClickListener(v -> mViewPager.setCurrentItem(TabManager.FRAGMENT_POSITION_RUN));
        
        return view;
    }
    
    public void loadExample(String exampleTitle, @RawRes final int id) {
        mCurrentFileName.setText(exampleTitle);
        mCurrentFileIsExample = true;
        mHasOpenedFile = true;
        mEditor.clear();
        mEditor.write(Util.readTextFile(getContext(), id));
        mEditor.moveCursorToStart();
    }
    
    public HighlighterEditText getEditor() {
        return mEditor;
    }
    
    public void loadFile(String filename, String content) {
        mCurrentFileName.setText(filename);
        mCurrentFileIsExample = false;
        mHasOpenedFile = true;
        mEditor.clear();
        mEditor.write(content);
        mEditor.moveCursorToStart();
    }
    
    public String getCurrentFileName() {
        return mCurrentFileName.getText().toString();
    }
    
    public boolean currentFileIsExample() {
        return mCurrentFileIsExample;
    }
    
    public boolean hasOpenedFile() {
        return mHasOpenedFile;
    }
}
