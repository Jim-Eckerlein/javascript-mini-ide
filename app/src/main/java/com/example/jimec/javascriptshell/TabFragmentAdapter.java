package com.example.jimec.javascriptshell;

import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.security.InvalidParameterException;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    
    public static final int FRAGMENT_POSITION_FILES = 0;
    public static final int FRAGMENT_POSITION_EDITOR = 1;
    public static final int FRAGMENT_POSITION_RUN = 2;
    public static final int NUM_FRAGMENTS = 3;
    
    private final ViewPager mViewPager;
    private final FilesTab mFilesTab = new FilesTab();
    private final EditorTab mEditorTab = new EditorTab();
    private final RunTab mRunTab = new RunTab();
    
    public TabFragmentAdapter(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        mViewPager = viewPager;
        mEditorTab.setPager(viewPager);
        mFilesTab.setAdapter(this);
    }
    
    @Override
    public int getCount() {
        return NUM_FRAGMENTS;
    }
    
    public EditorTab getEditorTab() {
        return mEditorTab;
    }
    
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_POSITION_FILES:
                return mFilesTab;
            case FRAGMENT_POSITION_EDITOR: return mEditorTab;
            case FRAGMENT_POSITION_RUN: return mRunTab;
            default: throw new InvalidParameterException();
        }
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case FRAGMENT_POSITION_FILES:
                return FilesTab.TITLE;
            case FRAGMENT_POSITION_EDITOR: return EditorTab.TITLE;
            case FRAGMENT_POSITION_RUN: return RunTab.TITLE;
            default: throw new InvalidParameterException();
        }
    }
    
    public void loadExample(@RawRes int id) {
        mEditorTab.loadExample(id);
        mViewPager.setCurrentItem(FRAGMENT_POSITION_EDITOR);
    }
    
    public void loadFile(String content) {
        mEditorTab.loadFile(content);
        mViewPager.setCurrentItem(FRAGMENT_POSITION_EDITOR);
    }
    
    public void runCode() {
        mRunTab.clearOutput();
        mRunTab.launchV8(mEditorTab.getEditor().toString());
    }
    
    public void stopRunningCode() {
        mRunTab.stopV8();
    }
    
}
