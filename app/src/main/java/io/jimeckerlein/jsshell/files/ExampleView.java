package io.jimeckerlein.jsshell.files;

import android.content.Context;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.jimeckerlein.jsshell.R;
import io.jimeckerlein.jsshell.TabManager;

public class ExampleView extends FrameLayout {
    
    @RawRes
    private int mResId;
    private TabManager mTabManager;
    private TextView mExampleName;
    
    public ExampleView(Context context) {
        super(context);
        init();
    }
    
    public ExampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public ExampleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    public static ExampleView create(Context context, TabManager tabManager, @RawRes int resId, String exampleName) {
        ExampleView exampleView = new ExampleView(context);
        exampleView.mTabManager = tabManager;
        exampleView.mResId = resId;
        exampleView.mExampleName.setText(exampleName);
        return exampleView;
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_example, this);
        setOnClickListener((View) -> mTabManager.loadExample(mExampleName.getText().toString(), mResId));
        mExampleName = findViewById(R.id.example_list_item_title);
    }
    
}
