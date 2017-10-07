package com.example.jimec.javascriptshell;

import android.support.design.widget.TabLayout;

public class TabSelectListener implements TabLayout.OnTabSelectedListener {
    
    private final TabManager mTabManager;
    private final MainActivity mMainActivity;
    
    public TabSelectListener(TabManager tabManager, MainActivity mainActivity) {
        mTabManager = tabManager;
        mMainActivity = mainActivity;
    }
    
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case TabManager.FRAGMENT_POSITION_RUN:
                mTabManager.runCode();
        }
        mMainActivity.invalidateOptionsMenu();
    }
    
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case TabManager.FRAGMENT_POSITION_RUN:
                mTabManager.stopRunningCode();
        }
    }
    
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
    
}
