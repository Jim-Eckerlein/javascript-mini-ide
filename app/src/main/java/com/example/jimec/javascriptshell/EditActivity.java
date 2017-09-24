package com.example.jimec.javascriptshell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jimec.javascriptshell.code_lines.LineList;
import com.example.jimec.javascriptshell.js_editor_view.JSEditorView;
import com.example.jimec.javascriptshell.keyboard_view.Keyboard;

public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_CODE = "com.example.jimec.javascriptshell.CODE";
    private final LineList mCodeLines = new LineList();
    private int mCurrentDemo = -1;
    private TextView mKeyboardHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    
        // Toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    
        // Editor:
        JSEditorView JSEditorView = (com.example.jimec.javascriptshell.js_editor_view.JSEditorView) findViewById(R.id.editor);
        mCodeLines.addOnEditListener(JSEditorView);
    
        // Keyboard:
        Keyboard keyboard = (Keyboard) findViewById(R.id.keyboard);
        keyboard.setLineList(mCodeLines);
    
        // Load initial demo:
        loadDemo(R.raw.demo_demo);
        
        mKeyboardHelper = (TextView) findViewById(R.id.keyboard_helper);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_toolbar, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
    
            case R.id.action_run:
                Intent intent = new Intent(this, RunActivity.class);
                intent.putExtra(EXTRA_CODE, mCodeLines.toStringWithoutComments());
                startActivity(intent);
                return true;
    
            case R.id.action_clear:
                mCodeLines.clear();
                return true;
            
            case R.id.action_help:
                mKeyboardHelper.setVisibility(View.VISIBLE);
    
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
        }
    }
    
    private void loadDemo(@RawRes int id) {
        if (id != mCurrentDemo) {
            mCodeLines.clear();
            mCodeLines.write(Util.readTextFile(this, id));
            mCodeLines.moveCursorToStart();
            mCurrentDemo = id;
        }
    }
    
    public void closeKeyboardHelper(View view) {
        mKeyboardHelper.setVisibility(View.GONE);
    }
}
