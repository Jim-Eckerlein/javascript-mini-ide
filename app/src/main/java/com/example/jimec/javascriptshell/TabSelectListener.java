package com.example.jimec.javascriptshell;

import android.support.design.widget.TabLayout;

public class TabSelectListener implements TabLayout.OnTabSelectedListener {
    
    private final TabFragmentAdapter mAdapter;
    
    public TabSelectListener(TabFragmentAdapter adapter) {
        mAdapter = adapter;
    }
    
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case TabFragmentAdapter.FRAGMENT_POSITION_RUN:
                mAdapter.runCode();
        }
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
