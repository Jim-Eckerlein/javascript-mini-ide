package com.example.jimec.javascriptshell;

import android.support.design.widget.TabLayout;

public class TabSelectListener implements TabLayout.OnTabSelectedListener {
    
    private final TabFragmentAdapter mAdapter;
    private final MainActivity mMainActivity;
    
    public TabSelectListener(TabFragmentAdapter adapter, MainActivity mainActivity) {
        mAdapter = adapter;
        mMainActivity = mainActivity;
    }
    
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case TabFragmentAdapter.FRAGMENT_POSITION_RUN:
                mAdapter.runCode();
        }
        mMainActivity.invalidateOptionsMenu();
    }
    
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case TabFragmentAdapter.FRAGMENT_POSITION_RUN:
                mAdapter.stopRunningCode();
        }
    }
    
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
    
}
