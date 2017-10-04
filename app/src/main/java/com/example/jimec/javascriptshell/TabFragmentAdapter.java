package com.example.jimec.javascriptshell;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.security.InvalidParameterException;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    
    public static final int FRAGMENT_POSITION_EDITOR = 0;
    public static final int FRAGMENT_POSITION_RUN = 1;
    public static final int NUM_FRAGMENTS = 2;
    private final EditorTab mEditorTab = new EditorTab();
    private final RunTab mRunTab = new RunTab();
    
    public TabFragmentAdapter(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        mEditorTab.setPager(viewPager);
    }
    
    @Override
    public int getCount() {
        return NUM_FRAGMENTS;
    }
    
    public EditorTab getEditorTab() {
        return mEditorTab;
    }
    
    public RunTab getRunTab() {
        return mRunTab;
    }
    
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_POSITION_EDITOR: return mEditorTab;
            case FRAGMENT_POSITION_RUN: return mRunTab;
            default: throw new InvalidParameterException();
        }
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case FRAGMENT_POSITION_EDITOR: return EditorTab.TITLE;
            case FRAGMENT_POSITION_RUN: return RunTab.TITLE;
            default: throw new InvalidParameterException();
        }
    }
}
