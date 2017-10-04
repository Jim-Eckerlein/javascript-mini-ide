package com.example.jimec.javascriptshell;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
    
        mTabLayout.addOnTabSelectedListener(new TabSelectListener(mAdapter));
    
        mPager.setCurrentItem(TabFragmentAdapter.FRAGMENT_POSITION_EDITOR);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_toolbar, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
