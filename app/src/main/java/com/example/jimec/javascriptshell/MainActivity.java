package com.example.jimec.javascriptshell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
    private TabManager mTabManager;
    private TabLayout mTabLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    
        mPager = (ViewPager) findViewById(R.id.pager);
        mTabManager = new TabManager(getSupportFragmentManager(), mPager, this);
        mPager.setAdapter(mTabManager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mPager);
    
        mTabLayout.addOnTabSelectedListener(new TabSelectListener(mTabManager, this));
    
        // TODO: uncomment
        //mPager.setCurrentItem(TabManager.FRAGMENT_POSITION_EDITOR);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        @MenuRes int res;
    
        switch (mPager.getCurrentItem()) {
            case TabManager.FRAGMENT_POSITION_FILES:
                res = R.menu.toolbar_files;
                break;
            case TabManager.FRAGMENT_POSITION_EDITOR:
                res = R.menu.toolbar_editor;
                break;
            case TabManager.FRAGMENT_POSITION_RUN:
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
        @IdRes int id = item.getItemId();
    
        switch (id) {
            case R.id.action_format_code:
                mTabManager.getEditorTab().getEditor().format();
                return true;
        
            case R.id.action_clear_code:
                mTabManager.getEditorTab().getEditor().clear();
                return true;
    
            case R.id.action_undo:
                mTabManager.getEditorTab().getEditor().undo();
                return true;
    
            case R.id.action_about:
                // Start About activity:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
    
            case R.id.action_help:
                // Start Help activity:
                Intent helpIntent = new Intent(this, HelpActivity.class);
                startActivity(helpIntent);
                return true;
    
            case R.id.action_share:
                // Share code:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mTabManager.getEditorTab().getEditor().toString());
                sendIntent.setType("application/javascript");
                startActivity(sendIntent);
        
            default:
                return false;
        }
    }
}
