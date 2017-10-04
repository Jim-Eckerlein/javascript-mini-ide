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
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_toolbar, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
        /*switch (item.getItemId()) {
    
            case R.id.action_run:
                Intent intent = new Intent(this, RunActivity.class);
                intent.putExtra(EXTRA_CODE, mEditor.toString());
                startActivity(intent);
                return true;
    
            case R.id.action_clear:
                mEditor.clear();
                return true;
    
            case R.id.action_format:
                mEditor.formatCode();
                return true;
            
            case R.id.action_help:
                mKeyboardHelper.setVisibility(View.VISIBLE);
                return true;
    
            case R.id.action_demo_demo:
                loadDemo(R.raw.demo_demo);
                return true;
    
            case R.id.action_demo_print:
                loadDemo(R.raw.demo_print);
                return true;
    
            case R.id.action_demo_array:
                loadDemo(R.raw.demo_array);
                return true;
    
            case R.id.action_demo_object:
                loadDemo(R.raw.demo_object);
                return true;
    
            case R.id.action_demo_function:
                loadDemo(R.raw.demo_function);
                return true;
    
            case R.id.action_demo_time:
                loadDemo(R.raw.demo_time);
                return true;
    
            case R.id.action_demo_typeof:
                loadDemo(R.raw.demo_typeof);
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }*/
    }
}
