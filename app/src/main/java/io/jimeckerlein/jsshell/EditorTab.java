package io.jimeckerlein.jsshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import io.jimeckerlein.jsshell.editor.CodeEditorView;
import io.jimeckerlein.jsshell.keyboard.KeyboardView;

public class EditorTab extends Fragment {
    
    private CodeEditorView mEditor;
    private ViewPager mViewPager;
    private TextView mCurrentFileName;
    private boolean mCurrentFileIsExample = false;
    private boolean mHasOpenedFile = false;
    private ConstraintSet mConstraintSetOriginal = new ConstraintSet();
    private ConstraintSet mConstraintSetHiddenKeyboard = new ConstraintSet();
    private ConstraintLayout mRoot;
    private boolean mIsKeyboardVisible = true;
    private Transition mKeyboardTransition = new ChangeBounds();
    
    public void setPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        mKeyboardTransition.setDuration(200);
        mKeyboardTransition.setInterpolator(new AccelerateDecelerateInterpolator());
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
        keyboardView.setOnHideKeyboardListener(this::hideKeyboard);
        view.findViewById(R.id.show_keyboard).setOnClickListener(v -> showKeyboard());
    
        view.findViewById(R.id.run_code_key).setOnClickListener(v -> mViewPager.setCurrentItem(TabManager.RUN_TAB_POSITION));
        view.findViewById(R.id.run_code_key_shortcut).setOnClickListener(v -> mViewPager.setCurrentItem(TabManager.RUN_TAB_POSITION));
        
        return view;
    }
    
    public void loadExample(String exampleTitle, @RawRes final int id) {
        mCurrentFileName.setText(exampleTitle);
        mCurrentFileIsExample = true;
        mHasOpenedFile = true;
        mEditor.clear();
        mEditor.write(Util.readTextFile(getContext(), id), true);
    }
    
    public CodeEditorView getEditor() {
        return mEditor;
    }
    
    public void loadFile(String filename, String content) {
        mCurrentFileName.setText(filename);
        mCurrentFileIsExample = false;
        mHasOpenedFile = true;
        mEditor.clear();
        mEditor.write(content, true);
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
    
    public boolean isKeyboardVisible() {
        return mIsKeyboardVisible;
    }
    
    public void hideKeyboard() {
        if (!mIsKeyboardVisible) {
            return;
        }
        TransitionManager.beginDelayedTransition(mRoot, mKeyboardTransition);
        mConstraintSetHiddenKeyboard.applyTo(mRoot);
        mIsKeyboardVisible = false;
    }
    
    public void showKeyboard() {
        if (mIsKeyboardVisible) {
            return;
        }
        TransitionManager.beginDelayedTransition(mRoot, mKeyboardTransition);
        mConstraintSetOriginal.applyTo(mRoot);
        mIsKeyboardVisible = true;
    }
}
