package com.example.jimec.javascriptshell;

import android.app.Activity;
import android.support.annotation.RawRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.security.InvalidParameterException;

public class TabManager extends FragmentPagerAdapter implements TabLayout.OnTabSelectedListener {
    
    public static final int FRAGMENT_POSITION_FILES = 0;
    public static final int FRAGMENT_POSITION_EDITOR = 1;
    public static final int FRAGMENT_POSITION_RUN = 2;
    public static final int NUM_FRAGMENTS = 3;
    
    private final ViewPager mViewPager;
    private final FilesTab mFilesTab = new FilesTab();
    private final EditorTab mEditorTab = new EditorTab();
    private final RunTab mRunTab = new RunTab();
    private final Activity mActivity;
    
    public TabManager(FragmentManager fm, ViewPager viewPager, Activity activity) {
        super(fm);
        mViewPager = viewPager;
        mActivity = activity;
        mEditorTab.setPager(viewPager);
        mFilesTab.setTabManager(this);
    }
    
    public void loadExample(String exampleName, @RawRes int id) {
        mEditorTab.loadExample(mActivity.getString(R.string.files_example_title, exampleName), id);
        mViewPager.setCurrentItem(FRAGMENT_POSITION_EDITOR);
    }
    
    public void loadFile(String filename, String content) {
        mEditorTab.loadFile(filename, content);
        mViewPager.setCurrentItem(FRAGMENT_POSITION_EDITOR);
    }
    
    public void startMultipleFileDeletion() {
        mFilesTab.startMultipleFileDeletion();
    }
    
    public void editorFormat() {
        mEditorTab.getEditor().format();
    }
    
    public void editorClear() {
        mEditorTab.getEditor().clear();
    }
    
    public void editorUndo() {
        mEditorTab.getEditor().undo();
    }
    
    public String getEditorCode() {
        return mEditorTab.getEditor().toString();
    }
    
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case TabManager.FRAGMENT_POSITION_RUN:
                mRunTab.clearOutput();
                mRunTab.launchV8(mEditorTab.getEditor().toString());
        }
        mActivity.invalidateOptionsMenu();
    }
    
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            
            // Break execution:
            case TabManager.FRAGMENT_POSITION_RUN:
                mRunTab.stopV8();
                break;
            
            // Quit editor -> save file:
            case TabManager.FRAGMENT_POSITION_EDITOR:
                if (!mEditorTab.currentFileIsExample()) {
                    mFilesTab.writeFile(mEditorTab.getCurrentFileName(), getEditorCode());
                }
        }
    }
    
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
    
    @Override
    public int getCount() {
        return NUM_FRAGMENTS;
    }
    
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_POSITION_FILES:
                return mFilesTab;
            case FRAGMENT_POSITION_EDITOR:
                return mEditorTab;
            case FRAGMENT_POSITION_RUN:
                return mRunTab;
            default:
                throw new InvalidParameterException();
        }
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case FRAGMENT_POSITION_FILES:
                return mActivity.getString(R.string.files_tab_title);
            case FRAGMENT_POSITION_EDITOR:
                return mActivity.getString(R.string.editor_tab_title);
            case FRAGMENT_POSITION_RUN:
                return mActivity.getString(R.string.run_tab_title);
            default:
                throw new InvalidParameterException();
        }
    }
}
