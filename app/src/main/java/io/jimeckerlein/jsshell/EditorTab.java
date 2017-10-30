package io.jimeckerlein.jsshell;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.transition.ChangeBounds;
import android.transition.Transition;
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
    private boolean mIsKeyboardVisible = true;
    private Transition mKeyboardTransition = new ChangeBounds();
    private KeyboardView mKeyboardView;
    
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
        
        // Keyboard:
        mKeyboardView = view.findViewById(R.id.keyboard);
        mKeyboardView.setEditor(mEditor);
        mKeyboardView.setOnHideKeyboardListener(this::hideKeyboard);
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
        // todo: add animation
        mKeyboardView.setTranslationY(100);
        mIsKeyboardVisible = false;
    }
    
    public void showKeyboard() {
        if (mIsKeyboardVisible) {
            return;
        }
        // todo: add animation
        mKeyboardView.setTranslationY(0);
        mIsKeyboardVisible = true;
    }
}
