package com.example.jimec.javascriptshell;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.jimec.javascriptshell.code_lines.LineList;
import com.example.jimec.javascriptshell.js_editor_view.JSEditorView;
import com.example.jimec.javascriptshell.keyboard_view.Keyboard;

import java.security.InvalidParameterException;

public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_CODE = "com.example.jimec.javascriptshell.CODE";
    private final LineList mCodeLines = new LineList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final EditActivity activity = this;
        setContentView(R.layout.activity_edit);
    
        // Toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        JSEditorView editor = (JSEditorView) findViewById(R.id.editor);

        Keyboard keyboard = (Keyboard) findViewById(R.id.keyboard);
        keyboard.setLineList(mCodeLines);

        mCodeLines.addOnEditListener(editor);

        ((Spinner) findViewById(R.id.examples_spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mCodeLines.clear();
                int script;
                switch (pos) {
                    case 0:
                        script = R.raw.demo_example;
                        break;
                    case 1:
                        script = R.raw.print_example;
                        break;
                    case 2:
                        script = R.raw.array_example;
                        break;
                    case 3:
                        script = R.raw.object_example;
                        break;
                    case 4:
                        script = R.raw.function_example;
                        break;
                    case 5:
                        script = R.raw.time_example;
                        break;
                    case 6:
                        script = R.raw.typeof_example;
                        break;
                    default:
                        throw new InvalidParameterException("Unknown example at position " + pos);
                }
                mCodeLines.write(Util.readTextFile(activity, script));
                mCodeLines.moveCursorToStart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void run(View view) {
        Intent intent = new Intent(this, RunActivity.class);
        intent.putExtra(EXTRA_CODE, mCodeLines.toStringWithoutComments());
        startActivity(intent);
    }

    public void clearCode(View view) {
        mCodeLines.clear();
    }

}
