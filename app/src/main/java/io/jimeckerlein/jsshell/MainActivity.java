package io.jimeckerlein.jsshell;

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

import io.jimeckerlein.jsshell.files.FilesManager;

import java.security.InvalidParameterException;

public class MainActivity extends AppCompatActivity {
    
    private ViewPager mPager;
    private TabManager mTabManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        // Initialize files manager:
        FilesManager.initialize(this);
        
        // Toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    
        mPager = (ViewPager) findViewById(R.id.pager);
        mTabManager = new TabManager(getSupportFragmentManager(), mPager, this);
        mPager.setAdapter(mTabManager);
    
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mPager);
        tabLayout.addOnTabSelectedListener(mTabManager);
    
        mPager.setCurrentItem(TabManager.EDITOR_TAP_POSITION);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        @MenuRes int res;
    
        switch (mPager.getCurrentItem()) {
            case TabManager.FILES_TAB_POSITION:
                res = R.menu.toolbar_files;
                break;
            case TabManager.EDITOR_TAP_POSITION:
                res = R.menu.toolbar_editor;
                break;
            case TabManager.RUN_TAB_POSITION:
                res = R.menu.toolbar_run;
                break;
            default:
                throw new InvalidParameterException("No such tab");
        }
    
        getMenuInflater().inflate(res, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
        if (!mTabManager.backPressed()) {
            super.onBackPressed();
        }
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        
        // Save file:
        mTabManager.saveEditorFile();
        
        // Save session:
        mTabManager.saveSession();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        @IdRes int id = item.getItemId();
    
        switch (id) {
            case R.id.action_format_code:
                mTabManager.editorFormat();
                return true;
        
            case R.id.action_clear_code:
                mTabManager.editorClear();
                return true;
        
            case R.id.action_undo:
                mTabManager.editorUndo();
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, mTabManager.getEditorCode());
                sendIntent.setType("application/javascript");
                startActivity(sendIntent);
                return true;
        
            case R.id.action_delete_multiple_files:
                // Delete multiple files from file tab
                mTabManager.startMultipleFileDeletion();
                return true;
            
            default:
                return false;
        }
    }
}
