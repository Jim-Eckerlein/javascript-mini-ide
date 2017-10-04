package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.security.InvalidParameterException;

public class MainActivity extends AppCompatActivity {
    
    private ViewPager mPager;
    private TabFragmentAdapter mAdapter;
    private TabLayout mTabLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    
        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), mPager);
        mPager.setAdapter(mAdapter);
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mPager);
    
        mTabLayout.addOnTabSelectedListener(new TabSelectListener(mAdapter, this));
    
        mPager.setCurrentItem(TabFragmentAdapter.FRAGMENT_POSITION_EDITOR);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        @MenuRes int res;
    
        switch (mPager.getCurrentItem()) {
            case TabFragmentAdapter.FRAGMENT_POSITION_FILES:
                res = R.menu.toolbar_files;
                break;
            case TabFragmentAdapter.FRAGMENT_POSITION_EDITOR:
                res = R.menu.toolbar_editor;
                break;
            case TabFragmentAdapter.FRAGMENT_POSITION_RUN:
                res = R.menu.toolbar_run;
                break;
            default:
                throw new InvalidParameterException("No such tab");
        }
    
        getMenuInflater().inflate(res, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
